package com.example.pa4al;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button register;
    SharedPreferences sharedPreferences;
    Api api;
    CheckBox saveLoginCheck;
    Intent activityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        username = findViewById(R.id.username);
        email = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();

            }
        });

    }

    public void Register(){
        Intent mainActivity = new Intent(this, LoginActivity.class);

        String mail = email.getText().toString();
        String user = username.getText().toString();
        String psd = password.getText().toString();

        // TODO: Afficher un Toast si username ou password est vide (puis return;)
        if(mail.isEmpty()){
            email.setText("email required");
            email.requestFocus();
            return;
        }
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
                .getInstance().getApi().createUser(new RegisterDTO(mail, user, psd));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO: Créer un objet pour déléguer la réponse de connexion : logginResponseHandler(response)
                //  Concrètement cet objet regarderai le type de retour et enregistre le token ou bien affiche un
                //  message d'erreur
                if(response.code() == 404){
                    Toast.makeText(RegisterActivity.this, "This user does not exist",
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 403){
                    Toast.makeText(RegisterActivity.this, "Incorrect username or password",
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code() > 299){
                    Toast.makeText(RegisterActivity.this, "Error while logging in",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Logged-in ! :"+response.headers().get("Authorization"),
                            Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}

