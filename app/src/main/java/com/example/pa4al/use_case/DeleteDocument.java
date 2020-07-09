package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteDocument {
    public static void DeleteDocument(String documentId, final Context context,
                             final DeleteDocumentCallBack callBack){
        Call<Void> call = RetrofitClient
                .getInstance().getApi().deleteDocument(context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE).getString("Token",
                        null), documentId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    callBack.onSuccess(context);
                }
                else{
                    ResponseHandler responseHandler = new ResponseHandler(R.array.deleteDocumentErrors);
                    String errorMessage = responseHandler.handle(response.code());
                    callBack.onFailure(context, new Exception(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onFailure(context, new Exception(t));
            }
        });
    }

    public interface DeleteDocumentCallBack {
        void onSuccess(Context context);
        void onFailure(Context context, Exception e);
    }
}
