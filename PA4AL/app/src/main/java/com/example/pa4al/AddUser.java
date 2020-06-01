package com.example.pa4al;

import com.example.pa4al.model.User;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser {
    public static void onAdd(String username, String email, String password , JsonPlaceHolderApi jsonPlaceHolderApi){
            User user = new User(username,email,password);
            Call<User> call = jsonPlaceHolderApi.createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(!response.isSuccessful()){
                        System.out.println("Code: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });


    }
}
