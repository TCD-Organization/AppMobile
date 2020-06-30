package com.example.pa4al.amqp;

import android.widget.ProgressBar;

public class FetchAnalysisProgressionParameter {
    public final String id;
    public final ProgressBar progressBar;
    public FetchAnalysisProgressionParameter(String id, ProgressBar progressBar) {
        this.id = id;
        this.progressBar = progressBar;
    }
}