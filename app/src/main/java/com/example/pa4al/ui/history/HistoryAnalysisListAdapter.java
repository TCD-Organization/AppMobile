package com.example.pa4al.ui.history;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.amqp.FetchAnalysisProgressionTask;
import com.example.pa4al.model.Analysis;

import java.util.List;

public class HistoryAnalysisListAdapter extends RecyclerView.Adapter<HistoryAnalysisListAdapter.AnalysesViewHolder> {

    private final List<Analysis> mAnalyses;
    private final Context mContext;

    public HistoryAnalysisListAdapter(Context context, List<Analysis> analyses) {
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
        AsyncTask fetchAnalysisProgressionTask = new FetchAnalysisProgressionTask();
        fetchAnalysisProgressionTask.execute(mAnalyses.get(position).getId());

        holder.analysisLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on"+ mAnalyses.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
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
        final RelativeLayout analysisLayout;
        public Analysis mAnalysisItem;

        public AnalysesViewHolder(View documentView) {
            super(documentView);
            mViewItem = documentView;
            mAnalysisName = documentView.findViewById(R.id.analysis_name);
            mDocumentType = documentView.findViewById(R.id.analysis_type);
            mAnalysisDocumentName = documentView.findViewById(R.id.analysis_document_name);
            mAnalysisStatus = documentView.findViewById(R.id.analysis_status);
            analysisLayout = documentView.findViewById(R.id.analysis_layout);
        }
    }
}
