package com.example.pa4al.api;

import com.example.pa4al.model.AnalysisDTO;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.model.AnalysisType;
import com.example.pa4al.model.Document;
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

    @POST("/document")
    Call<Void> createDocument(@Header("Authorization") String token, @Body DocumentDTO body);

    @GET("/analysis/all")
    Call<List<Analysis>> getAnalysis(@Header("Authorization") String token);

    @GET("/analysis-types/all")
    Call<List<AnalysisType>> getAnalysisTypes(@Header("Authorization") String token);

    @GET("/document/all")
    Call<List<Document>> getDocuments(@Header("Authorization") String token);

    @POST("/analysis")
    Call<Void> createAnalysis(@Header("Authorization") String token, @Body AnalysisDTO body);
}
