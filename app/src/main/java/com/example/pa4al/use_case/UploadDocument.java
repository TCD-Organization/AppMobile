package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.DocumentContentType;

import java.io.File;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocument {
    public static void UploadDocument(String documentName, String documentGenre, DocumentContentType contentType,
                                      String documentContent,
                                      File file, Boolean fileUpload, final Context context,
                             final UploadDocumentCallBack callBack){

        MultipartBody.Part body = null;
        RequestBody filePartName = null;

        Call<Void> call = null;
        if (fileUpload) {
            body = prepareFilePart("data", file);
            filePartName = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            call = RetrofitClient
                    .getInstance().getApi().createDocumentFromFile(context.getSharedPreferences("userPrefs",
                            Context.MODE_PRIVATE).getString("Token",null),
                            filePartName, body, documentName, documentGenre, contentType, documentContent);
        } else {
            call = RetrofitClient
                    .getInstance().getApi().createDocument(context.getSharedPreferences("userPrefs",
                            Context.MODE_PRIVATE).getString("Token",null),
                            documentName, documentGenre, contentType, documentContent);
        }


        if (call == null) {
            callBack.onFailure(context, new Exception("Call is null"));
        }
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.code());
                    callBack.onSuccess(context);
                } else {
                    ResponseHandler responseHandler = new ResponseHandler(R.array.documentUploadErrors);
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

    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        System.out.println("MimeType: "+mimeType);
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public interface UploadDocumentCallBack {
        void onSuccess(Context context);
        void onFailure(Context context, Exception e);
    }
}
