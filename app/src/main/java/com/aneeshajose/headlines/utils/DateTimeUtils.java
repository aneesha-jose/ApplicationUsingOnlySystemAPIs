package com.aneeshajose.headlines.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String USER_FRIENDLY_DATE = "dd MMM, yyyy";

    public static Date stringToDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Date dateNew = new Date();
        if (date.endsWith("Z")) {
            date = date.replace("Z", "+00:00");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
        } else {
            format.setTimeZone(TimeZone.getDefault());
        }
        try {
            dateNew = format.parse(date);
            System.out.println(dateNew);
        } catch (ParseException e) {
            dateNew.setTime(0);
            Log.e(TAG, e.getMessage(), e);
        }
        return dateNew;
    }

    public static String getFormattedDate(String time, String inputPattern, String outputPattern) {
        if (!TextUtils.isEmpty(time)) {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
            try {
                if (time.endsWith("Z")) {
                    time = time.replace("Z", "+00:00");
                    inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                } else {
                    inputFormat.setTimeZone(TimeZone.getDefault());
                }

                outputFormat.setTimeZone(TimeZone.getDefault());

                Date date = inputFormat.parse(time);
                return date == null ? "" : outputFormat.format(date);

            } catch (ParseException e) {
                Log.d(TAG, e.getMessage(), e);
            }
        }
        return "";
    }

}
