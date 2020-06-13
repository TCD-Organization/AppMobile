package com.example.pa4al.api;

import com.example.pa4al.start.login.LoginDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface Api {
    @POST("/register")
    Call<ResponseBody> createUser ( // TODO : Comme le login ci-dessous, utiliser un @Body
            @Field("username") String name,
            @Field("email") String email,
            @Field("password") String password

    );

    @POST("/login")
    Call<Void> userLogin(@Body LoginDTO body);
}
