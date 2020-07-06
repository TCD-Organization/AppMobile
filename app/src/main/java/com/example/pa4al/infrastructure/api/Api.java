package com.example.pa4al.infrastructure.api;

import com.example.pa4al.model.Analysis;
import com.example.pa4al.model.AnalysisDTO;
import com.example.pa4al.model.Document;
import com.example.pa4al.model.LoginDTO;
import com.example.pa4al.model.RegisterDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {
    @POST("/register")
    Call<Void> userRegister(@Body RegisterDTO body);

    @POST("/login")
    Call<Void> userLogin(@Body LoginDTO body);

    @FormUrlEncoded
    @POST("/document")
    Call<Void> createDocument(@Header("Authorization") String token,
        @Field("name") String name,
        @Field("genre") String genre,
        @Field("content_type") String contentType,
        @Field("content") String content);

    @GET("/analysis/all")
    Call<List<Analysis>> getAnalysis(@Header("Authorization") String token);

    @GET("/document/all")
    Call<List<Document>> getDocuments(@Header("Authorization") String token);

    @POST("/analysis")
    Call<Void> createAnalysis(@Header("Authorization") String token, @Body AnalysisDTO body);
}
