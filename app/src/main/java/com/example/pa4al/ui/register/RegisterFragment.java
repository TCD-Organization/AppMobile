package com.example.pa4al.ui.register;

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
import androidx.fragment.app.Fragment;

import com.example.pa4al.R;

import static com.example.pa4al.use_case.Register.Register;
import static com.example.pa4al.use_case.Register.RegisterCallBack;

public class RegisterFragment extends Fragment {

    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        Button btnRegister = view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(returnedView -> {
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            register(username, password);
        });

        return view;
    }


    public void register(String username, String password){
        if(username.isEmpty()){
            Toast.makeText(getContext(), R.string.login_message_username_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(getContext(), R.string.login_message_password_required, Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(getString(R.string.login_progress_title));
        mProgress.setMessage(getString(R.string.login_progress_message));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        Register(username, password, getContext(), new RegisterCallBack() {
            @Override
            public void onSuccess(Context context) {
                mProgress.dismiss();
                etUsername.setText("");
                etPassword.setText("");
                Toast.makeText(getActivity(), R.string.register_user_created_message, Toast.LENGTH_SHORT).show();            }

            @Override
            public void onFailure(Context context, String message) {
                mProgress.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Context context, Exception e) {
                mProgress.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.error, e.getMessage()),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
