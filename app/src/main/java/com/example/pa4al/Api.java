package com.example.pa4al;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {
    @POST("/register")
    Call<Void> createUser (@Body RegisterDTO body);

    @POST("/login")
    Call<Void> userLogin(@Body LoginDTO body);

    @POST("/document")
    Call<Void> createDocument(@Header ("Dynamic-Header") token token,@Body DocumentDTO body);
}
