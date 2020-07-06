package com.example.pa4al.ui.documents;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pa4al.R;
import com.example.pa4al.model.Document;

import java.util.Collections;
import java.util.List;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder> {

    private final List<Document> mDocuments;
    private final Context mContext;

    public DocumentListAdapter(Context context, List<Document> documents) {
        Collections.reverse(documents);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mDeleteButton.setBackground(mContext.getDrawable(R.drawable.ic_delete));
        }

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAlert(holder.mItem);
            }
        });
        holder.documentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Click on"+ mDocuments.get(position).toString(), Toast.LENGTH_SHORT).show();
                Intent newAnalysisIntent = new Intent(view.getContext(), NewAnalysisActivity.class);
                newAnalysisIntent.putExtra("document", mDocuments.get(position));
                mContext.startActivity(newAnalysisIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDocuments == null) {
            return 0;
        }
        return mDocuments.size();
    }

    private void showDeleteAlert(Document document) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.document_remove_title);
        builder.setMessage(mContext.getString(R.string.document_delete_message) + document.getName() +"\" ?");
        builder.setIcon(R.drawable.ic_delete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                ;    // stop chronometer here

            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {
        final View mViewItem;
        final TextView mDocumentName;
        final TextView mDocumentGenre;
        final TextView mDocumentContent;
        final Button mDeleteButton;
        final RelativeLayout documentLayout;
        public Document mItem;

        public DocumentViewHolder(View documentView) {
            super(documentView);
            mViewItem = documentView;
            mDocumentName = documentView.findViewById(R.id.document_name);
            mDocumentGenre = documentView.findViewById(R.id.document_genre);
            mDocumentContent = documentView.findViewById(R.id.document_content);
            mDeleteButton = documentView.findViewById(R.id.deleteDocumentButton);
            documentLayout = documentView.findViewById(R.id.document_layout);
        }
    }


}
