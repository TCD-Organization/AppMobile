package com.example.pa4al.ui.analyses;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.amqp.FetchAnalysisProgressionParameter;
import com.example.pa4al.amqp.FetchAnalysisProgressionTask;
import com.example.pa4al.model.Analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnalysisListAdapter extends RecyclerView.Adapter<AnalysisListAdapter.AnalysesViewHolder> {

    private final List<Analysis> mAnalyses;
    private List<AsyncTask<FetchAnalysisProgressionParameter, Analysis, Analysis>> analysisProgressionTasks =
            new ArrayList<>();
    private final Context mContext;

    public AnalysisListAdapter(Context context, List<Analysis> analyses) {
        mAnalyses = analyses;
        mContext = context;
    }

    @Override
    public AnalysesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.analysis_item_layout, parent, false);
        return new AnalysesViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final AnalysesViewHolder holder, final int position) {
        final Analysis currentAnalysis = mAnalyses.get(position);
        final Long lastingTime = currentAnalysis.getLasting_time();
        holder.mAnalysisItem = currentAnalysis;
        holder.mAnalysisName.setText(currentAnalysis.getName());
        holder.mDocumentType.setText(currentAnalysis.getType());
        holder.mAnalysisDocumentName.setText(currentAnalysis.getDocument_name());
        holder.mAnalysisStatus.setText(currentAnalysis.getStatus());
        holder.mProgressBar.setMax(currentAnalysis.getTotal_steps());
        holder.mProgressBar.setProgress(currentAnalysis.getStep_number());
        holder.mStepNumber.setText(String.valueOf(currentAnalysis.getStep_number()));
        holder.mStepMax.setText(String.valueOf(currentAnalysis.getTotal_steps()));
        holder.mLastingTime.setText(String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(lastingTime),
                TimeUnit.MILLISECONDS.toSeconds(lastingTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lastingTime))
        ));
        System.out.println("fetching progression for : " + mAnalyses.get(position).getId());

        if (!mAnalyses.get(position).getStatus().equals("FINISHED")) {
            fetchAnalysisProgression(holder);
        }

        holder.analysisLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Click on" + mAnalyses.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAnalysisProgression(final AnalysesViewHolder holder) {
        FetchAnalysisProgressionParameter parameter = new FetchAnalysisProgressionParameter(holder.mAnalysisItem.getId(),
                holder.mProgressBar, holder.mStepNumber);
        AsyncTask<FetchAnalysisProgressionParameter, Analysis, Analysis> analysisProgressAsyncTask =
                new FetchAnalysisProgressionTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parameter);
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
        final TextView mDocumentType;
        final TextView mAnalysisDocumentName;
        final TextView mAnalysisStatus;
        final ProgressBar mProgressBar;
        final TextView mStepNumber;
        final TextView mStepMax;
        final TextView mLastingTime;
        final ConstraintLayout analysisLayout;
        public Analysis mAnalysisItem;

        public AnalysesViewHolder(View analysisView) {
            super(analysisView);
            mViewItem = analysisView;
            mAnalysisName = analysisView.findViewById(R.id.analysis_name);
            mDocumentType = analysisView.findViewById(R.id.analysis_type);
            mAnalysisDocumentName = analysisView.findViewById(R.id.analysis_document_name);
            mAnalysisStatus = analysisView.findViewById(R.id.analysis_status);
            mProgressBar = analysisView.findViewById(R.id.progressBar);
            mStepNumber = analysisView.findViewById(R.id.progressStepNumber);
            mStepMax = analysisView.findViewById(R.id.progressMaxStep);
            mLastingTime = analysisView.findViewById(R.id.lastingTimeTextView);
            analysisLayout = analysisView.findViewById(R.id.analysis_layout);
        }
    }
}
