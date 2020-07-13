package com.example.pa4al.ui.analyses;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pa4al.R;
import com.example.pa4al.model.Analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.val;

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
            selectedAnalysisResultContent.setText(analysis.getResult());
            selectedAnalysisResultContent.setKeyListener(null);

            downloadResultButton = findViewById(R.id.downloadResultButton);
            downloadResultButton.setOnClickListener(v -> downloadResult());
        }
    }

    private void downloadResult() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            document = new PdfDocument();
            String content = selectedAnalysisResultContent.getText().toString();
            content = "sau\nsa\nges\nare\nvery\ngood\nfor\nme";
            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(24 * getResources().getDisplayMetrics().density);
            textPaint.setColor(Color.BLACK);
            int width = (int) textPaint.measureText(content);
            Rect rect = new Rect();
            textPaint.getTextBounds(content, 0, content.length(), rect);



            // crate a page description
            PdfDocument.PageInfo pageInfo =
                    new PdfDocument.PageInfo.Builder(width,  rect.height() , 1).create();

            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);



            float originalTextSize = selectedAnalysisResultContent.getTextSize();
            System.out.println("Original text size: " + originalTextSize);
            //selectedAnalysisResultContent.setTextSize(originalTextSize - 70f);
            System.out.println("New text size: " + originalTextSize);

            // draw something on the page
            Canvas firstPage = page.getCanvas();
            /*Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            firstPage.drawPaint(paint);
             */
            StaticLayout staticLayout = new StaticLayout(content, textPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
            staticLayout.draw(firstPage);

            /*paint.setColor(Color.BLACK);
            paint.setTextSize(20);*/
            //firstPage.drawText(selectedAnalysisResultContent.getText().toString(), 10, 25, textPaint);
            //content.draw(page.getCanvas());

            // finish the page
            document.finishPage(page);

            // write the document content
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TCD-Analyses";
            //File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            analysisFile = new File(path, selectedAnalysisTitle.getText()+".pdf");
            System.out.println("analysisFile path : "+ analysisFile.getAbsolutePath());
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                saveFile();
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }

            // close the document
            document.close();

        }

        //List<AnalysisResult> analysisResults = getListOfResults(analysis.getResult());

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
                Toast.makeText(this, selectedAnalysisTitle.getText() + " downloaded", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
    }
}
