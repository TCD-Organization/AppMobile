package com.example.pa4al.use_case;

import android.content.Context;

import com.example.pa4al.R;
import com.example.pa4al.infrastructure.api.ResponseHandler;
import com.example.pa4al.infrastructure.api.RetrofitClient;
import com.example.pa4al.model.DocumentContentType;

import java.io.File;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocument {
    static MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    public static void UploadDocument(String documentName, String documentGenre, DocumentContentType contentType,
                                      String documentContent,
                                      File file, Boolean fileUpload, final Context context,
                             final UploadDocumentCallBack callBack){


        Call<Void> call;
        if (fileUpload) {
            System.out.println("FileUpload : " + contentType);
            RequestBody fileBody = prepareFileBody(file);

            if (fileBody == null) {
                callBack.onFailure(context,
                        new Exception(context.getResources().getString(R.string.upload_document_file_must_be_format)));
                return;
            }

            Map<String, RequestBody> map = new HashMap<>();
            map.put("name", RequestBody.create(MEDIA_TYPE_TEXT, documentName));
            map.put("genre", RequestBody.create(MEDIA_TYPE_TEXT, documentGenre));
            map.put("content_type", RequestBody.create(MEDIA_TYPE_TEXT, String.valueOf((contentType))));
            map.put("content", RequestBody.create(MEDIA_TYPE_TEXT, "none"));
            map.put("file\"; filename=\""+ file.getName(), fileBody);
            call = RetrofitClient
                    .getInstance().getApi().createDocumentFromFile(context.getSharedPreferences("userPrefs",
                            Context.MODE_PRIVATE).getString("Token",null), map);
        } else {
            System.out.println("No fileupload : " + contentType);
            call = RetrofitClient
                    .getInstance().getApi().createDocument(context.getSharedPreferences("userPrefs",
                            Context.MODE_PRIVATE).getString("Token",null),
                            documentName, documentGenre, contentType, documentContent);
        }


        if (call != null) {
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        System.out.println(response.code());
                        callBack.onSuccess(context);
                    } else {
                        System.out.println("Error message: " + response.message());
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
        } else {
            callBack.onFailure(context, new Exception("Call is null"));
        }
    }

    public static RequestBody prepareFileBody(File file) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        System.out.println("MimeType: "+mimeType);
        if (mimeType.equals("application/pdf") || mimeType.equals("application/txt")) {
            return RequestBody.create(MediaType.parse(mimeType), file);
        }
        return null;
    }

    public interface UploadDocumentCallBack {
        void onSuccess(Context context);
        void onFailure(Context context, Exception e);
    }
}
