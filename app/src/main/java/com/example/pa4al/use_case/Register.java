package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.RegisterDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register {
    public static void Register(String username, String password, final Context context,
                                final RegisterCallBack callBack) {
        Call<Void> call = RetrofitClient
                .getInstance().getApi().userRegister(new RegisterDTO(username, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    callBack.onSuccess(context);
                }
                else{
                    ResponseHandler responseHandler = new ResponseHandler(R.array.loginErrors);
                    String errorMessage = responseHandler.handle(response.code());
                    callBack.onFailure(context, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onError(context, new Exception(t));
            }
        });
    }

    public interface RegisterCallBack {
        void onSuccess(Context context);
        void onFailure(Context context, String message);
        void onError(Context context, Exception e);
    }
}
