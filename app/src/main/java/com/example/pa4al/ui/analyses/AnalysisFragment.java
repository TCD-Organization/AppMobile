package com.example.pa4al.ui.analyses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.ui.MainFragment;
import com.example.pa4al.use_case.FetchAnalyses;

import java.util.List;

public class AnalysisFragment extends MainFragment {
    private int mColumnCount = 1;
    private static final String ARG_COLUMN_COUNT = "column-count";
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.analysis_list_fragment, container, false);

        loadAnalysisList(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private void loadAnalysisList(final View view) {
        FetchAnalyses.FetchAnalyses(getContext(), new FetchAnalyses.FetchAnalysesCallBack() {
            @Override
            public void onSuccess(Context context, List<Analysis> analyses) {
                initAnalysisListAdapter(view, analyses);
            }

            @Override
            public void onFailure(Context context, Exception e) {
                Toast.makeText(getActivity(), R.string.error + " : "+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initAnalysisListAdapter(View view, List<Analysis> analyses) {
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new AnalysisListAdapter(view.getContext(), analyses));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(recyclerView != null) {
            recyclerView.setAdapter(null);
        }
    }
}