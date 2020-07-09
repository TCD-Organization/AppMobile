package com.example.pa4al.ui.documents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.model.Document;

import java.util.Collections;
import java.util.List;

import static com.example.pa4al.use_case.DeleteDocument.DeleteDocument;
import static com.example.pa4al.use_case.DeleteDocument.DeleteDocumentCallBack;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder> {

    private final List<Document> mDocuments;
    private final Context mContext;
    public SharedPreferences userSharedPreferences;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            holder.mDeleteButton.setBackground(mContext.getDrawable(R.drawable.ic_delete));

        holder.mDeleteButton.setOnClickListener(v -> showDeleteAlert(position));

        holder.documentLayout.setOnClickListener(view -> {
            Intent newAnalysisIntent = new Intent(view.getContext(), NewAnalysisActivity.class);
            newAnalysisIntent.putExtra("document", mDocuments.get(position));
            mContext.startActivity(newAnalysisIntent);
        });
    }

    @Override
    public int getItemCount() {
        if (mDocuments == null) {
            return 0;
        }
        return mDocuments.size();
    }

    private void showDeleteAlert(int position) {
        Document document = mDocuments.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.document_delete_title);
        builder.setMessage(mContext.getString(R.string.document_delete_message) + document.getName() +"\" ?");
        builder.setIcon(R.drawable.ic_delete);
        builder.setPositiveButton(R.string.yes, (dialog, id) -> {
            ProgressDialog mProgress = new ProgressDialog(mContext);
            mProgress.setTitle(mContext.getResources().getString(R.string.analysis_delete_progress_title));
            mProgress.setMessage(mContext.getResources().getString(R.string.analysis_delete_progress_message));
            mProgress.setCancelable(false);
            mProgress.setIndeterminate(true);
            mProgress.show();

            DeleteDocument(document.getId(), mContext, new DeleteDocumentCallBack() {
                @Override
                public void onSuccess(Context context) {
                    Toast.makeText(context, "Analysis Successfuly deleted", Toast.LENGTH_SHORT).show();
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
        mDocuments.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDocuments.size());
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
