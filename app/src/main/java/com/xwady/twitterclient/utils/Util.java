package com.xwady.twitterclient.utils;

import android.text.format.DateUtils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Util {
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return shortFormat(relativeDate);
    }

    public static String shortFormat(String input) {
        String[] parts = input.split(" ");

        String numericPart ="";
        String timePart ="";

        if (parts[0].equalsIgnoreCase("in") && StringUtils.isNumeric(parts[1])) {
            numericPart = parts[1];
            if (parts[2].contains("minute")) {
                timePart = "m";
            } else if (parts[2].contains("hour")) {
                timePart = "h";
            } else if (parts[2].contains("second")) {
                timePart = "s";
            }
            return "in " + numericPart + timePart;
        }

        if (StringUtils.isNumeric(parts[0])) {
            numericPart = parts[0];
        }

        if (parts[1].contains("minute")) {
            timePart = "m";
        } else if (parts[1].contains("hour")) {
            timePart = "h";
        } else if (parts[1].contains("second")) {
            timePart = "s";
        }

        if (!StringUtils.isEmpty(numericPart) && !StringUtils.isEmpty(timePart)) {
            return numericPart + timePart;
        }
        return input;
    }

}
