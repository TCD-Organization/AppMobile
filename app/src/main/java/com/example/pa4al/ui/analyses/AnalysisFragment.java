package com.example.pa4al.ui.analyses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.ui.MainFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Call<List<Analysis>> call = RetrofitClient
                .getInstance().getApi().getAnalysis(userSharedPreferences.getString("Token", null));
        call.enqueue(new Callback<List<Analysis>>() {
            @Override
            public void onResponse(Call<List<Analysis>> call, Response<List<Analysis>> response) {
                /*if(response.isSuccessful()){
                    callBack.onSuccess(context, response.body());
                }
                else{
                    ResponseHandler responseHandler = new ResponseHandler(R.array.analysisFetchingErrors);
                    String errorMessage = responseHandler.handle(response.code());
                    callBack.onFailure(context, new Exception(errorMessage));
                }*/
                // TODO: Move into a service

                List<Analysis> analyses = response.body();
                initAnalysisListAdapter(view, analyses);
            }

            @Override
            public void onFailure(Call<List<Analysis>> call, Throwable t) {

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
        recyclerView.setAdapter(null);
    }
}