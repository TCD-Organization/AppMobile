package com.example.pa4al.ui.analyses;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.amqp.FetchAnalysisProgressionParameter;
import com.example.pa4al.amqp.FetchAnalysisProgressionTask;
import com.example.pa4al.model.Analysis;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onBindViewHolder(final AnalysesViewHolder holder, final int position) {
        holder.mAnalysisItem = mAnalyses.get(position);
        holder.mAnalysisName.setText(mAnalyses.get(position).getName());
        holder.mDocumentType.setText(mAnalyses.get(position).getType());
        holder.mAnalysisDocumentName.setText(mAnalyses.get(position).getDocument_name());
        holder.mAnalysisStatus.setText(mAnalyses.get(position).getStatus());
        holder.mProgressBar.setMax(mAnalyses.get(position).getTotal_steps());
        System.out.println("fetching progression for : " + mAnalyses.get(position).getId());

        // TODO : Do not listen for progression if Status == FINISHED
        if (!mAnalyses.get(position).getStatus().equals("FINISHED")) {
            fetchAnalysisProgression(holder, position);
        }

        holder.analysisLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on" + mAnalyses.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        analysisProgressionTasks.forEach(asyncTask -> asyncTask.cancel(true));
        System.out.println("OnDetached");
        super.onDetachedFromRecyclerView(recyclerView);

    }

    private void fetchAnalysisProgression(final AnalysesViewHolder holder, final int position) {
        FetchAnalysisProgressionParameter parameter = new FetchAnalysisProgressionParameter(mAnalyses.get(position).getId(),
                holder.mProgressBar);
        AsyncTask<FetchAnalysisProgressionParameter, Analysis, Analysis> analysisProgressAsyncTask =
                new FetchAnalysisProgressionTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parameter);
        analysisProgressionTasks.add(analysisProgressAsyncTask);
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
        final RelativeLayout analysisLayout;
        public Analysis mAnalysisItem;

        public AnalysesViewHolder(View documentView) {
            super(documentView);
            mViewItem = documentView;
            mAnalysisName = documentView.findViewById(R.id.analysis_name);
            mDocumentType = documentView.findViewById(R.id.analysis_type);
            mAnalysisDocumentName = documentView.findViewById(R.id.analysis_document_name);
            mAnalysisStatus = documentView.findViewById(R.id.analysis_status);
            mProgressBar = documentView.findViewById(R.id.progressBar);
            analysisLayout = documentView.findViewById(R.id.analysis_layout);
        }
    }
}
