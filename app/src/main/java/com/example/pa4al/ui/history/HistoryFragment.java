package com.example.pa4al.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pa4al.R;
import com.example.pa4al.activities.MainActivity;
import com.example.pa4al.activities.StartActivity;
import com.example.pa4al.activities.StartCallbackFragment;
import com.example.pa4al.api.RetrofitClient;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.model.DocumentDTO;
import com.example.pa4al.ui.MainFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends MainFragment {

    ListView listView;
    Button new_analysis;
    private StartCallbackFragment startCallbackFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        listView = view.findViewById(R.id.listView);
        new_analysis = view.findViewById(R.id.new_analysis);
        new_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCallbackFragment.loadAnalysisFragment();
            }
        });
        final ArrayList<Analysis> analyses = new ArrayList<Analysis>();
        Call<Void> call = RetrofitClient
                .getInstance().getApi().getAnalysis(userSharedPreferences.getString("Token", null));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                analyses.equals(response.body());
                ArrayAdapter<Analysis> adapter = new ArrayAdapter<Analysis>(getActivity(),
                        android.R.layout.simple_list_item_1, analyses);
                listView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        return view;
    }

    public void setStartCallbackFragment(StartCallbackFragment startCallbackFragment) {
        this.startCallbackFragment = startCallbackFragment;
    }

}