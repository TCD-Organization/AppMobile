package com.example.pa4al.use_case;

import android.content.Context;
import android.widget.EditText;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.LoginDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login {
    public static void Login(String username, String password, EditText usernameField,
                             EditText passwordField, final Context context,
                             final LoginCallBack callBack){

        // TODO: Afficher un Toast si username ou password est vide (puis return;)
        if(username.isEmpty()){
            callBack.onWarning(context,
                    new Exception(context.getString(R.string.login_message_username_required)), usernameField);
            return;
        }
        if(password.isEmpty()){
            callBack.onWarning(context,
                    new Exception(context.getString(R.string.login_message_password_required)), passwordField);
            return;
        }

        Call<Void> call = RetrofitClient
                .getInstance().getApi().userLogin(new LoginDTO(username, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    callBack.onSuccess(context, response.headers().get("Authorization"));
                } else {
                    String errorMessage;

                    errorMessage = response.code() == 404 ? "Error, user not found" :
                              response.code() == 403 ? context.getString(R.string.login_message_incorrect_username_or_password) :
                              response.errorBody().toString();
                    callBack.onFailure(context, new Exception(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onFailure(context, new Exception(t));
            }
        });
    }

    public interface LoginCallBack {
        void onSuccess(Context context, String token);
        void onFailure(Context context, Exception e);
        void onWarning(Context context, Exception e, EditText field);
    }
}
