package com.example.pa4al.model;

import android.content.res.Resources;

import com.example.pa4al.AndroidApplication;
import com.example.pa4al.R;

import lombok.Data;

@Data
public class AnalysisResult {
    private String flag;
    private int a_id;
    private String start;
    private String end;
    private String delay;
    private String result;

    public String toString() {
        Resources resources = AndroidApplication.getAppContext().getResources();
        return resources.getString(R.string.analysis_result_flag) + flag + "\n" +
                resources.getString(R.string.analysis_result_start) + start + "\n" +
                resources.getString(R.string.analysis_result_end) + end + "\n" +
                resources.getString(R.string.analysis_result_delay) + delay + "\n" +
                resources.getString(R.string.analysis_result_result) + result + "\n\n";
    }
}