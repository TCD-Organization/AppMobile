package com.example.pa4al.start.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pa4al.MainActivity;
import com.example.pa4al.R;
import com.example.pa4al.api.Api;
import com.example.pa4al.api.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
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

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(user, mail, psd);
         call.enqueue(new Callback<ResponseBody>() {
         @Override
         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 try{
                     String s = response.body().string();
                     Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                 }catch (IOException e){
                     e.printStackTrace();
                 }
                 ;
             }

         @Override
         public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
             }
         });



    }

}


