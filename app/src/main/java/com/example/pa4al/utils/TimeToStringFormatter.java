package com.example.pa4al.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeToStringFormatter {
    public static String timeToString(Long lastingTime) {
        return String.format(Locale.FRANCE, "%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(lastingTime),
                TimeUnit.MILLISECONDS.toSeconds(lastingTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lastingTime)));
    }

    public static String timeToStringWithMillis(Long lastingTime) {
        return String.format(Locale.FRANCE, "%02d min, %02d sec, %02d mil",
                TimeUnit.MILLISECONDS.toMinutes(lastingTime),
                TimeUnit.MILLISECONDS.toSeconds(lastingTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lastingTime)),
                TimeUnit.MILLISECONDS.toMillis(lastingTime) - TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toSeconds(lastingTime))
        );
    }
}
