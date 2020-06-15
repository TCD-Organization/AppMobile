package com.example.pa4al.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pa4al.R;
import com.example.pa4al.activities.StartCallbackFragment;
import com.example.pa4al.api.RetrofitClient;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.ui.MainFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends MainFragment {

    ListView listView;
    private StartCallbackFragment startCallbackFragment;
    private int mColumnCount = 1;
    private static final String ARG_COLUMN_COUNT = "column-count";

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
                System.out.println(response.code());
                System.out.println(response.body());
                List<Analysis> analyses = response.body();
                /*ArrayAdapter<Analysis> adapter = new ArrayAdapter<Analysis>(getActivity(),
                        android.R.layout.simple_list_item_1, analyses);
                listView.setAdapter(adapter);*/
                System.out.println(analyses);
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
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new HistoryAnalysisListAdapter(view.getContext(), analyses));
        }
    }



    public void NewAnalysis(){
        startCallbackFragment.loadAnalysisFragment();
    }

    public void setStartCallbackFragment(StartCallbackFragment startCallbackFragment) {
        this.startCallbackFragment = startCallbackFragment;
    }
}