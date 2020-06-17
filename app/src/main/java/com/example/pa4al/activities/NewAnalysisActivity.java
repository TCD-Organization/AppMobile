package com.example.pa4al.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pa4al.R;
import com.example.pa4al.api.RetrofitClient;
import com.example.pa4al.model.AnalysisDTO;
import com.example.pa4al.model.AnalysisType;
import com.example.pa4al.model.Document;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAnalysisActivity extends AppCompatActivity {
    EditText etName;
    Spinner mSpinnerAnalysisTypes;
    Button mBtnAnalyze;
    public SharedPreferences userSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_analysis);
        userSharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        etName = findViewById(R.id.etName);
        mSpinnerAnalysisTypes = findViewById(R.id.spinnerAnalysisTypes);
        mBtnAnalyze = findViewById(R.id.btnAnalyze);

        addItemsOnSpinnerType();
        final Document document = (Document) getIntent().getSerializableExtra("document");
        mBtnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAnalysis(document);
            }
        });
    }

    public void addItemsOnSpinnerType() {

        Call<List<AnalysisType>> call = RetrofitClient
                .getInstance().getApi().
                        getAnalysisTypes(
                        userSharedPreferences.getString("Token", null
                        ));
        System.out.println("Hello there");
        call.enqueue(new Callback<List<AnalysisType>>() {
            @Override
            public void onResponse(Call<List<AnalysisType>> call, Response<List<AnalysisType>> response) {
                System.out.println("General Kenobi");
                System.out.println(response.body());
                List<String> types = new ArrayList<>();
                for (AnalysisType analysisType: response.body()) {
                    types.add(analysisType.getName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(NewAnalysisActivity.this,
                        android.R.layout.simple_spinner_item, types);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerAnalysisTypes.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<List<AnalysisType>> call, Throwable t) {
                System.out.println("General Grievous" + t.getMessage());
                finish();
            }
        });
    }

    private void createAnalysis(Document document){
        String nameAnalysis = etName.getText().toString();
        String type = String.valueOf(mSpinnerAnalysisTypes.getSelectedItem());
        String doc_id = document.getId();

        Call<Void> call = RetrofitClient
                .getInstance().getApi().createAnalysis(userSharedPreferences.getString("Token", null),
                        new AnalysisDTO(nameAnalysis, type, doc_id));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO: Créer un objet pour déléguer la réponse de connexion : logginResponseHandler(response)
                //  Concrètement cet objet regarderai le type de retour et enregistre le token ou bien affiche un
                //  message d'erreur
                if(response.code() > 299) {
                    Toast.makeText(NewAnalysisActivity.this, "Error " + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewAnalysisActivity.this, "Analysis Created ! " + response.code(),
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
