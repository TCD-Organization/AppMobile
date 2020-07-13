package com.example.pa4al.ui.analyses;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.amqp.FetchAnalysisProgressionTask;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.ui.documents.NewAnalysisActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.pa4al.use_case.DeleteAnalysis.DeleteAnalysis;
import static com.example.pa4al.use_case.DeleteAnalysis.DeleteAnalysisCallBack;
import static com.example.pa4al.utils.TimeToStringFormatter.timeToString;

public class AnalysisListAdapter extends RecyclerView.Adapter<AnalysisListAdapter.AnalysesViewHolder> {

    private final List<Analysis> mAnalyses;
    private List<AsyncTask<AnalysisListAdapter.AnalysesViewHolder, Analysis, Analysis>> analysisProgressionTasks =
            new ArrayList<>();
    private final Context mContext;

    public AnalysisListAdapter(Context context, List<Analysis> analyses) {
        Collections.reverse(analyses);
        mAnalyses = analyses;
        mContext = context;
    }

    @Override
    public AnalysesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.analysis_item_layout, parent, false);
        return new AnalysesViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final AnalysesViewHolder holder, final int position) {
        final Analysis currentAnalysis = mAnalyses.get(position);
        final Long lastingTime = currentAnalysis.getLasting_time();
        final String stepName = currentAnalysis.getStep_name();
        holder.mAnalysisItem = currentAnalysis;
        holder.mAnalysisName.setText(currentAnalysis.getName());
        holder.mAnalysisDocumentName.setText(currentAnalysis.getDocument_name());
        holder.mAnalysisStatus.setText(currentAnalysis.getStatus());
        holder.mProgressBar.setMax(currentAnalysis.getTotal_steps());
        holder.mProgressBar.setProgress(currentAnalysis.getStep_number());
        holder.mStepNumber.setText(String.valueOf(currentAnalysis.getStep_number()));
        holder.mStepMax.setText(String.valueOf(currentAnalysis.getTotal_steps()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            holder.mDeleteButton.setBackground(mContext.getDrawable(R.drawable.ic_delete));

        holder.mDeleteButton.setOnClickListener(v -> showDeleteAlert(position));

        if(stepName == null) {
            holder.mStepName.setText(R.string.analysis_status_not_started);
        } else {
            holder.mStepName.setText(stepName);
        }

        if(lastingTime == null) {
            holder.mLastingTime.setText(R.string.analysis_lasting_time_not_started);
        } else {
            holder.mLastingTime.setText(timeToString(lastingTime));
        }

        System.out.println("fetching progression for : " + mAnalyses.get(position).getId());

        if (!currentAnalysis.getStatus().equals("FINISHED") && !currentAnalysis.getStatus().equals("CANCELED")) {
            fetchAnalysisProgression(holder);
        }

        holder.analysisLayout.setOnClickListener(view -> {
            if (currentAnalysis.getStatus().equals("FINISHED")) {
                if (currentAnalysis.getResult() != null && !currentAnalysis.getResult().isEmpty()) {
                    Intent analysisResultIntent = new Intent(view.getContext(), AnalysisResultActivity.class);
                    analysisResultIntent.putExtra("analysis", currentAnalysis);
                    mContext.startActivity(analysisResultIntent);
                    Toast.makeText(mContext, currentAnalysis.getResult(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "No Result", Toast.LENGTH_SHORT).show();
                }
            } else if (currentAnalysis.getStatus().equals("CANCELED")) {
                Toast.makeText(mContext, "Analysis canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Analysis not finished", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void showDeleteAlert(int position) {
        Analysis analysis = mAnalyses.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.analysis_delete_title);
        builder.setMessage(mContext.getString(R.string.analysis_delete_message) + analysis.getName() +
                "\" ?");
        builder.setIcon(R.drawable.ic_delete);

        builder.setPositiveButton(R.string.yes, (dialog, id) -> {
            ProgressDialog mProgress = new ProgressDialog(mContext);
            mProgress.setTitle(mContext.getResources().getString(R.string.analysis_delete_progress_title));
            mProgress.setMessage(mContext.getResources().getString(R.string.analysis_delete_progress_message));
            mProgress.setCancelable(false);
            mProgress.setIndeterminate(true);
            mProgress.show();

            DeleteAnalysis(analysis.getId(), mContext,
                    new DeleteAnalysisCallBack() {

                        @Override
                        public void onSuccess(Context context) {
                            Toast.makeText(context, R.string.analysis_deleted_message, Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            removeAt(position);
                        }

                        @Override
                        public void onFailure(Context context, Exception e) {
                            Toast.makeText(context, context.getResources().getString(R.string.error, e.getMessage()),
                                    Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                        }
                    });

            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void removeAt(int position) {
        mAnalyses.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mAnalyses.size());
    }

    private void fetchAnalysisProgression(final AnalysesViewHolder holder) {
        AsyncTask<AnalysisListAdapter.AnalysesViewHolder, Analysis, Analysis> analysisProgressAsyncTask =
                new FetchAnalysisProgressionTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, holder);
        analysisProgressionTasks.add(analysisProgressAsyncTask);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        analysisProgressionTasks.forEach(asyncTask -> asyncTask.cancel(true));
        System.out.println("OnDetached");
        super.onDetachedFromRecyclerView(recyclerView);

    }

    @Override
    public int getItemCount() {
        if (mAnalyses == null) { // Because
            return 0;
        }
        return mAnalyses.size();
    }

    public class AnalysesViewHolder extends RecyclerView.ViewHolder {
        final View mViewItem;
        final TextView mAnalysisName;
        final TextView mAnalysisDocumentName;
        public TextView mAnalysisStatus;
        public ProgressBar mProgressBar;
        public TextView mStepNumber;
        public TextView mStepMax;
        public TextView mStepName;
        public TextView mLastingTime;
        public Button mDeleteButton;
        final ConstraintLayout analysisLayout;
        public Analysis mAnalysisItem;

        public AnalysesViewHolder(View analysisView) {
            super(analysisView);
            mViewItem = analysisView;
            mAnalysisName = analysisView.findViewById(R.id.analysis_name);
            mAnalysisDocumentName = analysisView.findViewById(R.id.analysis_document_name);
            mAnalysisStatus = analysisView.findViewById(R.id.analysis_status);
            mProgressBar = analysisView.findViewById(R.id.progressBar);
            mStepNumber = analysisView.findViewById(R.id.progressStepNumber);
            mStepMax = analysisView.findViewById(R.id.progressMaxStep);
            mStepName = analysisView.findViewById(R.id.stepNameTextView);
            mDeleteButton = analysisView.findViewById(R.id.deleteAnalysisButton);
            mLastingTime = analysisView.findViewById(R.id.lastingTimeTextView);
            analysisLayout = analysisView.findViewById(R.id.analysis_layout);
        }
    }
}
