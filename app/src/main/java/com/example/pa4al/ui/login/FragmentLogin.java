package com.example.pa4al.ui.login;

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
import com.example.pa4al.api.RetrofitClient;
import com.example.pa4al.model.LoginDTO;
import com.example.pa4al.activities.StartCallbackFragment;
import com.example.pa4al.ui.MainFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends MainFragment {

    private Button btnLogin, btnRegister;
    private EditText etUsername, etPassword;
    private StartCallbackFragment startCallbackFragment;
    private String username, password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                Login(username, password);
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

    public void Login(String username, String password){

        // TODO: Afficher un Toast si username ou password est vide (puis return;)
        if(username.isEmpty()){
            Toast.makeText(getActivity(), "Username required",
                    Toast.LENGTH_LONG).show();
            etUsername.requestFocus();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(getActivity(), "Password required",
                    Toast.LENGTH_LONG).show();
            etPassword.requestFocus();
            return;
        }

        Call<Void> call = RetrofitClient
                .getInstance().getApi().userLogin(new LoginDTO(username, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO: Créer un objet pour déléguer la réponse de connexion : logginResponseHandler(response)
                //  Concrètement cet objet regarderai le type de retour et enregistre le token ou bien affiche un
                //  message d'erreur
                if(response.code() == 404){
                    Toast.makeText(getActivity(), "This user does not exist",
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 403){
                    Toast.makeText(getActivity(), "Incorrect username or password",
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code() > 299){
                    Toast.makeText(getActivity(), "Error while logging in",
                            Toast.LENGTH_LONG).show();
                } else {
                    userPrefsEditor.putString("Token", response.headers().get("Authorization")).commit();
                    startCallbackFragment.startMainActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setStartCallbackFragment(StartCallbackFragment startCallbackFragment) {
        this.startCallbackFragment = startCallbackFragment;
    }
}
