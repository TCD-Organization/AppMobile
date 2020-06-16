package com.example.pa4al.api;

import com.example.pa4al.model.AnalysisDTO;
import com.example.pa4al.model.DocumentDTO;
import com.example.pa4al.model.Document;
import com.example.pa4al.model.LoginDTO;
import com.example.pa4al.model.RegisterDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {
    @POST("/register")
    Call<Void> userRegister(@Body RegisterDTO body);

    @POST("/login")
    Call<Void> userLogin(@Body LoginDTO body);

    @POST("/document") // TODO: @Header("Authorization") instead ?
    Call<Void> createDocument(@Header("Authorization") String token, @Body DocumentDTO body);

    @GET("/analysis/all") // TODO: @Header("Authorization") instead ?
    Call<Void> getAnalysis(@Header("Authorization") String token);

    @GET("/analysis-types") // TODO: @Header("Authorization") instead ?
    Call<List<String>> getType(@Header("Authorization") String token);

    @GET("/document/all") // TODO: @Header("Authorization") instead ?
    Call<List<Document>> getDoc(@Header("Authorization") String token);

    @POST("/analysis")
    Call<Void> createAnalysis(@Header("Authorization") String token, @Body AnalysisDTO body);
}
