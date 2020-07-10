package com.example.pa4al.ui.upload;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.DocumentContentType;
import com.example.pa4al.ui.MainFragment;
import com.example.pa4al.use_case.UploadDocument;
import com.example.pa4al.utils.FileUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.pa4al.model.DocumentContentType.*;
import static com.example.pa4al.use_case.UploadDocument.*;

public class UploadFragment extends MainFragment {

    EditText name;
    EditText genre;
    RadioButton fileRadio;
    RadioButton linkRadio;
    RadioButton textRadio;
    RadioGroup radioGroup;
    EditText content;
    Button fileBtn;
    Button upload;
    TextView fileNameLabel;
    TextView fileName;
    Uri fileUri = null;
    File file;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.upload_fragment, container, false);
        name = view.findViewById(R.id.etName);
        genre = view.findViewById(R.id.genre);
        fileRadio = view.findViewById(R.id.file);
        linkRadio = view.findViewById(R.id.link);
        textRadio = view.findViewById(R.id.text);
        radioGroup = view.findViewById(R.id.radioGroup);
        content = view.findViewById(R.id.content);
        upload = view.findViewById(R.id.upload);
        fileBtn = view.findViewById(R.id.fileSelectBtn);
        fileNameLabel = view.findViewById(R.id.documentFileNameLabel);
        fileName = view.findViewById(R.id.documentFileName);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.file) {

                    fileBtn.setVisibility(View.VISIBLE);
                    fileNameLabel.setVisibility(View.VISIBLE);
                    fileName.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                } else {
                    fileBtn.setVisibility(View.GONE);
                    fileNameLabel.setVisibility(View.GONE);
                    fileName.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload();
            }
        });


        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            if (selectedfile != null) {
                fileUri = selectedfile;
                String path = FileUtil.getRealPath(getContext(), fileUri);
                if (path != null) {
                    file = new File(path);
                    fileName.setText(file.getName());
                }
            }
        }
    }

    private void Upload(){
        String documentName = name.getText().toString();
        String documentGenre = genre.getText().toString();
        String documentContent = content.getText().toString();

        DocumentContentType contentType = NONE;
        if(fileRadio.isChecked()){
             contentType = FILE;
        }
        else if(linkRadio.isChecked()){
            contentType = LINK;
        }
        else if(textRadio.isChecked()){
            contentType = TEXT;
        }

        if (documentName.isEmpty()) {
            Toast.makeText(getActivity(), R.string.upload_document_name_required_message,
                    Toast.LENGTH_LONG).show();
            name.requestFocus();
            return;
        }
        if (documentGenre.isEmpty()) {
            Toast.makeText(getActivity(), R.string.upload_document_genre_required_message,
                    Toast.LENGTH_LONG).show();
            genre.requestFocus();
            return;
        }

        if (fileRadio.isChecked()) {
            if (fileUri == null) {
                Toast.makeText(getActivity(), R.string.upload_document_file_required_message,
                        Toast.LENGTH_LONG).show();
                fileBtn.requestFocus();
                return;
            }

        } else if (documentContent.isEmpty()) {
            Toast.makeText(getActivity(), R.string.upload_document_content_required_message,
                    Toast.LENGTH_LONG).show();
            fileBtn.requestFocus();
            return;
        }

        UploadDocument(documentName, documentGenre, contentType, documentContent, file,
                fileRadio.isChecked(), getContext(), new UploadDocumentCallBack() {
                    @Override
                    public void onSuccess(Context context) {
                        name.setText("");
                        genre.setText("");
                        content.setText("");
                        Toast.makeText(getActivity(), R.string.document_created_message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Context context, Exception e) {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.error, e.getMessage()),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}