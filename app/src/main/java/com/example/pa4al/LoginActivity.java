package com.example.pa4al;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pa4al.model.LoginResponse;
import com.example.pa4al.model.User;

import java.util.UUID;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button login;
    SharedPreferences sharedPreferences;
    Api api;
    CheckBox saveLoginCheck;
    Intent activityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_validate_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });

    }

    public void Login(){
        Intent mainActivity = new Intent(this, MainActivity.class);

        String user = username.getText().toString();
        String psd = password.getText().toString();

        // TODO: Afficher un Toast si username ou password est vide (puis return;)
        if(user.isEmpty()){
            username.setText("username required");
            username.requestFocus();
            return;
        }
        if(psd.isEmpty()){
            password.setText("password required");
            password.requestFocus();
            return;
        }

        Call<Void> call = RetrofitClient
                .getInstance().getApi().userLogin(new LoginDTO(user, psd));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO: Créer un objet pour déléguer la réponse de connexion : logginResponseHandler(response)
                //  Concrètement cet objet regarderai le type de retour et enregistre le token ou bien affiche un
                //  message d'erreur
                if(response.code() == 404){
                    Toast.makeText(LoginActivity.this, "This user does not exist",
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 403){
                    Toast.makeText(LoginActivity.this, "Incorrect username or password",
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code() > 299){
                    Toast.makeText(LoginActivity.this, "Error while logging in",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Logged-in ! :"+response.headers().get("Authorization"),
                            Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        
    }


}

