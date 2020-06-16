package com.example.pa4al.ui.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.pa4al.R;
import com.example.pa4al.activities.StartCallbackFragment;
import com.example.pa4al.api.RetrofitClient;
import com.example.pa4al.model.AnalysisDTO;
import com.example.pa4al.model.DocumentDTO;
import com.example.pa4al.model.Document;
import com.example.pa4al.ui.MainFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalysisFragment extends MainFragment {
        EditText name;
        Spinner spinnerType, spinnerDoc;
        Button analysis;
        private StartCallbackFragment startCallbackFragment;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_analysis, container, false);
            spinnerType = view.findViewById(R.id.spinner);
            spinnerDoc = view.findViewById(R.id.spinnerDoc);
            addItemsOnSpinnerType();
            addItemsOnSpinnerDoc();
            analysis = view.findViewById(R.id.analysis);
            analysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Analysis();

                }
            });
            return view;
        }
    public void addItemsOnSpinnerType() {

        Call<List<String>> call = RetrofitClient
                .getInstance().getApi().getType(userSharedPreferences.getString("Token", null));
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, response.body());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }
    public void addItemsOnSpinnerDoc() {

        Call<List<Document>> call = RetrofitClient
                .getInstance().getApi().getDoc(userSharedPreferences.getString("Token", null));
        call.enqueue(new Callback<List<Document>>() {
            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
                ArrayAdapter<Document> dataAdapter = new ArrayAdapter<Document>(getActivity(),
                        android.R.layout.simple_spinner_item, response.body());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDoc.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {

            }
        });
    }
    private void Analysis(){
        String nameAnalysis = name.getText().toString();
        String type = String.valueOf(spinnerType.getSelectedItem());
        String doc_id = String.valueOf(spinnerDoc.getSelectedItem().getClass());

        Call<Void> call = RetrofitClient
                .getInstance().getApi().createAnalysis(userSharedPreferences.getString("Token", null),
                        new AnalysisDTO(nameAnalysis, type, doc_id));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO: Créer un objet pour déléguer la réponse de connexion : logginResponseHandler(response)
                //  Concrètement cet objet regarderai le type de retour et enregistre le token ou bien affiche un
                //  message d'erreur

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
