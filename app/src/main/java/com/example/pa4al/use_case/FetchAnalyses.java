package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.Analysis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchAnalyses {
    public static void FetchAnalyses(final Context context,
                         final FetchAnalysesCallBack callBack){
        Call<List<Analysis>> call = RetrofitClient
                .getInstance().getApi().getAnalysis(context.getSharedPreferences("userPrefs",
                        Context.MODE_PRIVATE).getString("Token",null));

        call.enqueue(new Callback<List<Analysis>>() {
            @Override
            public void onResponse(Call<List<Analysis>> call, Response<List<Analysis>> response) {
                if(response.isSuccessful()){
                    callBack.onSuccess(context, response.body());
                }
                else{
                    ResponseHandler responseHandler = new ResponseHandler(R.array.loginErrors);
                    String errorMessage = responseHandler.handle(response.code());
                    callBack.onFailure(context, new Exception(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<List<Analysis>> call, Throwable t) {
                callBack.onFailure(context, new Exception(t));
            }
        });
    }

    public interface FetchAnalysesCallBack {
        void onSuccess(Context context, List<Analysis> analyses);
        void onFailure(Context context, Exception e);
    }
}
