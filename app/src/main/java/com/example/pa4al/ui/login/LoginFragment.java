package com.example.pa4al.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pa4al.R;
import com.example.pa4al.activities.StartCallbackFragment;
import com.example.pa4al.ui.MainFragment;

import static com.example.pa4al.use_case.Login.*;

public class LoginFragment extends MainFragment {

    private Button btnLogin, btnRegister;
    private EditText etUsername, etPassword;
    private StartCallbackFragment startCallbackFragment;
    private ProgressDialog mProgress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(getString(R.string.login_progress_title));
        mProgress.setMessage(getString(R.string.login_progress_message));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        btnLogin.setOnClickListener(returnedView -> {
            mProgress.show();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            login(username, password);
        });

        btnRegister.setOnClickListener(returnedView -> {
            if (startCallbackFragment != null) {
                startCallbackFragment.loadRegisterFragment();
            }
        });

        return view;
    }

    private void login(String username, String password) {
        if(username.isEmpty()){
            Toast.makeText(getContext(), R.string.login_message_username_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(getContext(), R.string.login_message_password_required, Toast.LENGTH_SHORT).show();
            return;
        }

        Login(username, password, getContext(),
                new LoginCallBack() {
                    @Override
                    public void onSuccess(Context context, String token) {
                        userPrefsEditor.putString("Token", token).commit();
                        mProgress.dismiss();
                        startCallbackFragment.startMainActivity();
                    }

                    @Override
                    public void onFailure(Context context, String message) {
                        mProgress.dismiss();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.error, e.getMessage()),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void setStartCallbackFragment(StartCallbackFragment startCallbackFragment) {
        this.startCallbackFragment = startCallbackFragment;
    }
}
