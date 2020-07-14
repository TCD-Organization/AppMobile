package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.AnalysisDTO;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAnalysis {
    public static void CreateAnalysis(@NonNull String nameAnalysis, @NonNull String doc_id, final Context context,
                                      final CreateAnalysisCallBack callBack){
        Call<Void> call = RetrofitClient
                .getInstance().getApi().createAnalysis(context.getSharedPreferences("userPrefs",
                        Context.MODE_PRIVATE).getString("Token",null), new AnalysisDTO(nameAnalysis, doc_id));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    callBack.onSuccess(context);
                }
                else{
                    ResponseHandler responseHandler = new ResponseHandler(R.array.createAnalysisErrors);
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

    public interface CreateAnalysisCallBack {
        void onSuccess(Context context);
        void onFailure(Context context, Exception e);
    }
}
