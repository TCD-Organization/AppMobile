package com.example.pa4al.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeToStringFormatter {
    public static String timeToString(Long lastingTime) {
        return String.format(Locale.FRANCE, "%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(lastingTime),
                TimeUnit.MILLISECONDS.toSeconds(lastingTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lastingTime)));
    }
}
