package com.example.pa4al;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button register;
    SharedPreferences sharedPreferences;
    JsonPlaceHolderApi jsonPlaceHolderApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        username = findViewById(R.id.username);
        email = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm);
        register = findViewById(R.id.register);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();

            }
        });

        username.setText(sharedPreferences.getString("username", null));
    }

    public void Register(){
        Intent mainActivity = new Intent(this, MainActivity.class);

        String user = username.getText().toString();
        String mail = email.getText().toString();
        String psd = password.getText().toString();
        String confirm = confirmPassword.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://51.178.18.199:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        AddUser addUser = new AddUser();
        addUser.onAdd(user,mail,psd,jsonPlaceHolderApi);
        if(editor != null){
            if(psd==confirm) {

                editor.putString("username", user);
                editor.putString("password", psd);
                editor.putString("uuid", UUID.randomUUID().toString());
                editor.apply();

                startActivity(mainActivity);
                finish();
            }
            else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}


