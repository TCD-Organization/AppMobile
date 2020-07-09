package com.example.pa4al.ui.documents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.model.Document;
import com.example.pa4al.ui.MainFragment;
import com.example.pa4al.use_case.FetchAnalyses;
import com.example.pa4al.use_case.FetchDocuments;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pa4al.use_case.FetchDocuments.*;

public class DocumentListFragment extends MainFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    @SuppressWarnings("unused")
    public static DocumentListFragment newInstance(int columnCount) {
        DocumentListFragment fragment = new DocumentListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_list_fragment, container, false);

        loadDocumentList(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    private void loadDocumentList(final View view) {
        FetchDocuments(getContext(), new FetchDocumentsCallBack() {
            @Override
            public void onSuccess(Context context, List<Document> documents) {
                initDocumentsListAdapter(view, documents);
            }

            @Override
            public void onFailure(Context context, Exception e) {
                Toast.makeText(getActivity(), R.string.error + " : "+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDocumentsListAdapter(View view, List<Document> documents) {
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new DocumentListAdapter(view.getContext(), documents));
        }
    }
}
