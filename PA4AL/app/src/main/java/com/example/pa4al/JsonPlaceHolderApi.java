package com.example.pa4al;

import com.example.pa4al.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @POST("/register")
    Call<User> createUser (@Body User user);
}
