package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteAnalysis {
    public static void DeleteAnalysis(String analysisId, final Context context,
                             final DeleteAnalysisCallBack callBack){

        if(analysisId.isEmpty()){
            callBack.onError(context, new Exception(context.getString(R.string.analysis_delete_id_empty_error)));
            return;
        }

        Call<Void> call = RetrofitClient
                .getInstance().getApi().deleteAnalysis(context.getSharedPreferences("userPrefs",
                        Context.MODE_PRIVATE).getString("Token",null), analysisId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println(response.code());
                    callBack.onSuccess(context);
                }
                else{
                    ResponseHandler responseHandler = new ResponseHandler(R.array.loginErrors);
                    String errorMessage = responseHandler.handle(response.code());
                    callBack.onFailure(context, new Exception(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onError(context, new Exception(t));
            }
        });
    }

    public interface DeleteAnalysisCallBack {
        void onSuccess(Context context);
        void onFailure(Context context, Exception e);
        void onError(Context context, Exception e);
    }
}
