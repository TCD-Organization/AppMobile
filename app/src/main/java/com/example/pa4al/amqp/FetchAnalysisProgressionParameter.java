package com.example.pa4al.amqp;


import android.widget.TextView;

public class FetchAnalysisProgressionParameter {
    public final String id;
    public final TextView tv;
    public FetchAnalysisProgressionParameter(String id, TextView tv) {
        this.id = id;
        this.tv = tv;
    }
}