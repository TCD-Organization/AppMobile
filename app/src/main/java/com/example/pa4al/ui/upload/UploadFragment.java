package com.example.pa4al.ui.upload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.example.pa4al.R;
import com.example.pa4al.api.RetrofitClient;
import com.example.pa4al.model.DocumentDTO;
import com.example.pa4al.ui.MainFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFragment extends MainFragment {

    EditText name;
    EditText genre;
    RadioButton file;
    RadioButton link;
    RadioButton text;
    EditText content;
    Button upload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.upload_fragment, container, false);
        name = view.findViewById(R.id.name);
        genre = view.findViewById(R.id.genre);
        file = view.findViewById(R.id.file);
        link = view.findViewById(R.id.link);
        text = view.findViewById(R.id.text);
        content = view.findViewById(R.id.content);
        upload = view.findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload();

            }
        });
        return view;
    }

    private void Upload(){
        String fileName = name.getText().toString();
        String fileGenre = genre.getText().toString();
        String fileContent = content.getText().toString();
        String contentType = "";
        if(file.isChecked()){
             contentType = "file";
        }
        else if(link.isChecked()){
            contentType = "link";
        }
        else if(text.isChecked()){
            contentType = "text";
        }
        // TODO: Afficher un Toast si username ou password est vide (puis return;)
        if(fileName.isEmpty()){
            name.setText("name required");
            name.requestFocus();
            return;
        }
        if(fileGenre.isEmpty()){
            genre.setText("password required");
            genre.requestFocus();
            return;
        }

        Call<Void> call = RetrofitClient
                .getInstance().getApi().createDocument(userSharedPreferences.getString("Token", null),
                        new DocumentDTO(fileName, fileGenre, contentType, fileContent));

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