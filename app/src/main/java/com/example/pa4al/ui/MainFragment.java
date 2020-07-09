package com.example.pa4al.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

public abstract class MainFragment extends Fragment {
    public SharedPreferences userSharedPreferences;
    public SharedPreferences.Editor userPrefsEditor;

    @Override
    public void onAttach(Context context) {
        userSharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        userPrefsEditor = userSharedPreferences.edit();
        super.onAttach(context);
    }
}
