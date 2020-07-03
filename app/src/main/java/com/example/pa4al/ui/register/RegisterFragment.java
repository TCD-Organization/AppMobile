package com.example.pa4al.ui.register;

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
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.RegisterDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private Button btnLogin, btnRegister;
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
        btnRegister = view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                Register(username, password);
            }
        });
        return view;
    }


    public void Register(String username, String password){

        // TODO: Afficher un Toast si username ou password est vide (puis return;)
        if(username.isEmpty()){
            Toast.makeText(getActivity(), R.string.login_message_username_required,
                    Toast.LENGTH_LONG).show();
            etUsername.requestFocus();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(getActivity(), R.string.login_message_password_required,
                    Toast.LENGTH_LONG).show();
            etPassword.requestFocus();
            return;
        }
        RegisterDTO registerDTO = new RegisterDTO(username, password);
        Call<Void> call = RetrofitClient
                .getInstance().getApi().userRegister(registerDTO);

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
                else if(response.code() == 409){
                    Toast.makeText(getActivity(), R.string.register_user_already_exists_message,
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code() > 299){
                    Toast.makeText(getActivity(), "Error while Registering" + response.code(),
                            Toast.LENGTH_LONG).show();
                } else {
                    etUsername.setText("");
                    etPassword.setText("");
                    Toast.makeText(getActivity(), R.string.register_user_created_message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
