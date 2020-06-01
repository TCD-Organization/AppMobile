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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().userLogin(user,psd);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if(!loginResponse.isError()){
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        
    }


}

