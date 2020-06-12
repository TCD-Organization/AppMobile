package com.example.pa4al;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pa4al.login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent activityIntent;

        SharedPreferences userPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        if (userPreferences.getString("token", null) != null) {

        }

        activityIntent = new Intent(this, LoginActivity.class);

        startActivity(activityIntent);
        finish();
    }
}

