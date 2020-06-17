package com.example.pa4al.ui.documents;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pa4al.R;
import com.example.pa4al.ui.analysis.NewAnalysisActivity;
import com.example.pa4al.model.Document;

import java.util.List;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder> {

    private final List<Document> mDocuments;
    private final Context mContext;

    public DocumentListAdapter(Context context, List<Document> documents) {
        mDocuments = documents;
        mContext = context;
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.document_item_layout, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DocumentViewHolder holder, final int position) {
        holder.mItem = mDocuments.get(position);
        holder.mDocumentName.setText(mDocuments.get(position).name);
        holder.mDocumentGenre.setText(mDocuments.get(position).genre);
        holder.mDocumentContent.setText(mDocuments.get(position).content);

        holder.documentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on"+ mDocuments.get(position).toString(), Toast.LENGTH_SHORT).show();
                Intent newAnalysisIntent = new Intent(view.getContext(), NewAnalysisActivity.class);
                newAnalysisIntent.putExtra("document", mDocuments.get(position));
                mContext.startActivity(newAnalysisIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDocuments.size();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {
        final View mViewItem;
        final TextView mDocumentName;
        final TextView mDocumentGenre;
        final TextView mDocumentContent;
        final RelativeLayout documentLayout;
        public Document mItem;

        public DocumentViewHolder(View documentView) {
            super(documentView);
            mViewItem = documentView;
            mDocumentName = documentView.findViewById(R.id.document_name);
            mDocumentGenre = documentView.findViewById(R.id.document_genre);
            mDocumentContent = documentView.findViewById(R.id.document_content);
            documentLayout = documentView.findViewById(R.id.document_layout);
        }
    }
}
