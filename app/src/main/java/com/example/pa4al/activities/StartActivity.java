package com.example.pa4al.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pa4al.R;
import com.example.pa4al.ui.MainActivity;
import com.example.pa4al.ui.login.FragmentLogin;
import com.example.pa4al.ui.register.FragmentRegister;

public class StartActivity extends AppCompatActivity implements StartCallbackFragment {
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loadLoginFragment();
    }

    public void loadLoginFragment() {
        FragmentLogin fragment = new FragmentLogin();
        fragment.setStartCallbackFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fcStart, fragment);
        fragmentTransaction.commit();
    }

    public void loadRegisterFragment() {
        fragment = new FragmentRegister();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fcStart, fragment);
        fragmentTransaction.commit();
    }

    public void startMainActivity() {
        Intent activityIntent = new Intent(this, MainActivity.class);
        startActivity(activityIntent);
        finish();
    }
}

