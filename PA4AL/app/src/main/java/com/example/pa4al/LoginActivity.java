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

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button login;
    SharedPreferences sharedPreferences;
    CheckBox saveLoginCheck;
    Intent activityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_validate_button);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });

        username.setText(sharedPreferences.getString("username", null));
    }

    public void Login(){
        Intent mainActivity = new Intent(this, MainActivity.class);

        String user = username.getText().toString();
        String psd = password.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(editor != null){
            editor.putString("username", user);
            editor.putString("password", psd);
            editor.putString("uuid", UUID.randomUUID().toString());
            editor.apply();
            startActivity(mainActivity);
            finish();

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}

