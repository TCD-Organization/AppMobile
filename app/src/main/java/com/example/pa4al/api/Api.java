package com.example.pa4al.api;

import com.example.pa4al.model.LoginDTO;
import com.example.pa4al.model.RegisterDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface Api {
    @POST("/register")
    Call<Void> userRegister(@Body RegisterDTO body);

    @POST("/login")
    Call<Void> userLogin(@Body LoginDTO body);
}
