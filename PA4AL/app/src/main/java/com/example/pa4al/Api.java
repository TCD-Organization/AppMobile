package com.example.pa4al;

import com.example.pa4al.model.LoginResponse;
import com.example.pa4al.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("/register")
    Call<ResponseBody> createUser (
            @Field("username") String name,
            @Field("email") String email,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("/login")
    Call <LoginResponse> userLogin(
            @Field("username") String name,
            @Field("password") String password
    );
}
