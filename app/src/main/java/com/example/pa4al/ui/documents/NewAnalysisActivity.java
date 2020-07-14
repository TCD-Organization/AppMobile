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
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.AnalysisDTO;
import com.example.pa4al.model.Document;
import com.example.pa4al.use_case.CreateAnalysis;

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

        CreateAnalysis.CreateAnalysis(nameAnalysis, doc_id, this, new CreateAnalysis.CreateAnalysisCallBack() {
            @Override
            public void onSuccess(Context context) {
                Toast.makeText(context, R.string.new_analysis_created, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Context context, Exception e) {
                Toast.makeText(context, context.getResources().getString(R.string.error, e.getMessage()),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
