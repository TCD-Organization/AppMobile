package com.example.pa4al.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnalysisResult {
    private String flag;
    private int a_id;
    private String start;
    private String end;
    private String delay;
    private String result;

    public String toString() { // TODO: 14/07/2020 Use resources to translate
        return "Flag:" + flag + "\n" +
                //"a_id: " + a_id + "\n" +
                "start: " + start + "\n" +
                "end: " + end + "\n" +
                "delay: " + delay + "\n" +
                "result: " + result + "\n\n";
    }
}