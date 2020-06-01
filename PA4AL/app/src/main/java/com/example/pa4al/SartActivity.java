package com.example.pa4al;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent activityIntent;

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if(sharedPreferences.getString("username", null) != null){

            activityIntent = new Intent(this, MainActivity.class);
        }else{

            activityIntent = new Intent(this, LoginActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}

