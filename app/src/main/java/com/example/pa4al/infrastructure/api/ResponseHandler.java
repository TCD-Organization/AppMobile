package com.example.pa4al.infrastructure.api;

import android.content.Context;

import com.example.pa4al.AndroidApplication;

public class ResponseHandler {
    String[] stringArray;
    public int stringArrayId;
    private Context context;

    public ResponseHandler ( int stringArrayId){
        this.stringArrayId = stringArrayId;
        this.context = AndroidApplication.getAppContext();
    }

    public String handle(int error){
        stringArray = context.getResources().getStringArray(stringArrayId);
        String message = null;
        System.out.println("Error code : "+error);
        if(error==403){
            message = stringArray[0];
        }
        else if (error==404){
            message = stringArray[1];
        }
        else if (error==409){
            message = stringArray[2];
        }
        else if (error==500){
            message =  stringArray[3];
        }
        return message;
    }
}
