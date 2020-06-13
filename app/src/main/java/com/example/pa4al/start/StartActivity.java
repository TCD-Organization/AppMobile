package com.example.pa4al.start;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pa4al.R;
import com.example.pa4al.start.login.FragmentLogin;
import com.example.pa4al.start.register.FragmentRegister;

public class StartActivity extends AppCompatActivity implements CallbackFragment {
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginFragment();

        /*
        Intent activityIntent;

        SharedPreferences userPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        activityIntent = new Intent(this, LoginActivity.class);

        startActivity(activityIntent);
        finish();
        */
    }

    public void loginFragment() {
        FragmentLogin fragment = new FragmentLogin();
        fragment.setCallbackFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fcStart, fragment);
        fragmentTransaction.commit();
    }

    public void registerFragment() {
        fragment = new FragmentRegister();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fcStart, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void changeFragment() {

    }
}

