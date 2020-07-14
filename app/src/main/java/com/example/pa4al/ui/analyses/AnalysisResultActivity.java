package com.example.pa4al.ui.analyses;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pa4al.R;
import com.example.pa4al.gson.GsonCustom;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.model.AnalysisResult;
import com.example.pa4al.utils.TimeToStringFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class AnalysisResultActivity extends AppCompatActivity {
    Button downloadResultButton;
    TextView selectedAnalysisTitle;
    EditText selectedAnalysisResultContent;
    File analysisFile;
    PdfDocument document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_result);

        final Analysis analysis = (Analysis) getIntent().getSerializableExtra("analysis");

        if (analysis == null)
            finish();
        else {
            selectedAnalysisTitle = findViewById(R.id.selectedAnalysisTitle);
            selectedAnalysisTitle.setText(analysis.getName());

            selectedAnalysisResultContent = findViewById(R.id.selectedAnalysisContent);

            String resultContent = formatResult(analysis.getResult());
            selectedAnalysisResultContent.setText(resultContent);
            selectedAnalysisResultContent.setKeyListener(null);

            downloadResultButton = findViewById(R.id.downloadResultButton);
            downloadResultButton.setOnClickListener(v -> downloadResult(resultContent));
        }
    }

    private void downloadResult(String result) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {

                TextPaint textPaint = new TextPaint();
                textPaint.setAntiAlias(true);
                textPaint.setTextSize(24 * getResources().getDisplayMetrics().density);
                textPaint.setColor(Color.BLACK);
                int width = (int) textPaint.measureText(result);
                Rect rect = new Rect();
                textPaint.getTextBounds(result, 0, result.length(), rect);


                StaticLayout staticLayout = new StaticLayout(result, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0
                        , false);

                PdfDocument.PageInfo pageInfo =
                        new PdfDocument.PageInfo.Builder(width,  staticLayout.getHeight() , 1).create();

                document = new PdfDocument();

                PdfDocument.Page page = document.startPage(pageInfo);

                Canvas firstPage = page.getCanvas();
                staticLayout.draw(firstPage);

                document.finishPage(page);

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TCD-Analyses";

                File dir = new File(path);
                if(!dir.exists())
                    dir.mkdirs();

                analysisFile = new File(path, selectedAnalysisTitle.getText() + "-analysis-" + new Date() +".pdf");
                System.out.println("analysisFile path : "+ analysisFile.getAbsolutePath());
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveFile();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }

            // close the document
            document.close();
            } catch (IllegalStateException e) {
                Toast.makeText(this, R.string.analysis_result_download_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                saveFile();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveFile() {
        try {
            if (analysisFile != null) {
                FileOutputStream fos = new FileOutputStream(analysisFile);
                document.writeTo(fos);
                document.close();
                fos.close();
                Toast.makeText(this, getString(R.string.analysis_result_downloaded_message, analysisFile.getName()),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            throw new RuntimeException(getString(R.string.analysis_result_creating_error), e);
        }
    }

    private String formatResult(String jsonResult) {
        AnalysisResult[] analysisResults = new GsonCustom().create().fromJson(jsonResult, AnalysisResult[].class);
        StringBuilder result = new StringBuilder();
        for (AnalysisResult analysisResult : analysisResults) {
            analysisResult.setDelay(TimeToStringFormatter.timeToStringWithMillis(Long.valueOf(analysisResult.getDelay())));
            result.append(analysisResult.toString());
        }

        return result.toString();
    }
}
