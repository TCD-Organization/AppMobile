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
import com.example.pa4al.use_case.Login;
import com.example.pa4al.ui.MainFragment;

public class LoginFragment extends MainFragment {

    private Button btnLogin, btnRegister;
    private EditText etUsername, etPassword;
    private StartCallbackFragment startCallbackFragment;
    private String username, password;
    private ProgressDialog mProgress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        //mProgress = view.findViewById(R.id.loginProgressBar);
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(getString(R.string.login_progress_title));
        mProgress.setMessage(getString(R.string.login_progress_message));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                mProgress.show();
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                Login.Login(username, password, etUsername, etPassword, LoginFragment.this.getContext(),
                        new Login.LoginCallBack() {
                            @Override
                            public void onSuccess(Context context, String token) {
                                mProgress.dismiss();

                                userPrefsEditor.putString("Token", token).commit();
                                startCallbackFragment.startMainActivity();
                                System.out.println("Success");
                                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Context context, Exception e) {
                                mProgress.dismiss();
                                System.out.println(e.getMessage());
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onWarning(Context context, Exception e, EditText field) {
                                mProgress.dismiss();
                                System.out.println(e.getMessage());
                                Toast.makeText(getActivity(), R.string.login_message_username_required,
                                        Toast.LENGTH_LONG).show();
                                field.requestFocus();
                            }
                        });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startCallbackFragment != null) {
                    startCallbackFragment.loadRegisterFragment();
                }
            }
        });

        return view;
    }



    public void setStartCallbackFragment(StartCallbackFragment startCallbackFragment) {
        this.startCallbackFragment = startCallbackFragment;
    }
}
