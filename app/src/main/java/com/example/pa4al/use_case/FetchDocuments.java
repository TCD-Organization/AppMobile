package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.Document;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchDocuments {
    public static void FetchDocuments(final Context context,
                         final FetchDocumentsCallBack callBack){
        Call<List<Document>> call = RetrofitClient
                .getInstance().getApi().getDocuments(context.getSharedPreferences("userPrefs",
                        Context.MODE_PRIVATE).getString("Token",null));

        call.enqueue(new Callback<List<Document>>() {
            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
                if(response.isSuccessful()){
                    callBack.onSuccess(context, response.body());
                }
                else{
                    ResponseHandler responseHandler = new ResponseHandler(R.array.fetchDocumentsErrors);
                    String errorMessage = responseHandler.handle(response.code());
                    callBack.onFailure(context, new Exception(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {
                callBack.onFailure(context, new Exception(t));
            }
        });
    }

    public interface FetchDocumentsCallBack {
        void onSuccess(Context context, List<Document> documents);
        void onFailure(Context context, Exception e);
    }
}
