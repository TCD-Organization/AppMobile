package com.example.pa4al.ui.documents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pa4al.R;
import com.example.pa4al.api.RetrofitClient;
import com.example.pa4al.model.AnalysisDTO;
import com.example.pa4al.model.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAnalysisActivity extends AppCompatActivity {
    EditText mNewAnalysisName;
    Button mNewAnalysisButton;
    TextView mSelectedDocumentTitle;
    EditText mSelectedDocumentContent;
    public SharedPreferences userSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_analysis);
        userSharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        final Document document = (Document) getIntent().getSerializableExtra("document");

        if (document == null)
            finish();

        mSelectedDocumentTitle = findViewById(R.id.selectedDocumentTitle);
        mSelectedDocumentTitle.setText(document.getName());
        mSelectedDocumentContent = findViewById(R.id.selectedDocumentContent);
        mSelectedDocumentContent.setText(document.getContent());
        mNewAnalysisName = findViewById(R.id.newAnalysisName);
        mNewAnalysisButton = findViewById(R.id.newAnalysisButton);

        mNewAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAnalysis(document);
            }
        });
    }

    private void createAnalysis(Document document){
        String nameAnalysis = mNewAnalysisName.getText().toString();
        String doc_id = document.getId();

        Call<Void> call = RetrofitClient
                .getInstance().getApi().createAnalysis(userSharedPreferences.getString("Token", null),
                        new AnalysisDTO(nameAnalysis, doc_id));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO: Créer un objet pour déléguer la réponse de connexion : logginResponseHandler(response)
                //  Concrètement cet objet regarderai le type de retour et enregistre le token ou bien affiche un
                //  message d'erreur
                if(response.code() > 299) {
                    Toast.makeText(NewAnalysisActivity.this, "Error " + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewAnalysisActivity.this, R.string.new_analysis_created,
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
