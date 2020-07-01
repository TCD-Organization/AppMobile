package com.example.pa4al.amqp;

import android.widget.ProgressBar;
import android.widget.TextView;

public class FetchAnalysisProgressionParameter {
    public final String id;
    public final ProgressBar progressBar;
    public final TextView stepNumber;
    public FetchAnalysisProgressionParameter(String id, ProgressBar progressBar, TextView stepNumber) {
        this.id = id;
        this.progressBar = progressBar;
        this.stepNumber = stepNumber;
    }
}