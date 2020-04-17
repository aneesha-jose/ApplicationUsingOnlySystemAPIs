package com.aneeshajose.headlines.utils;

import android.database.Cursor;

import com.aneeshajose.miscellaneous.utils.Optional;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public class OptionalUtils {

    public static String orEmpty(String input) {
        return Optional.orElse(input, "").get();
    }

    public static String getStringFromCursor(Cursor cursor, int index) {
        return index < 0 ? "" : orEmpty(cursor.getString(index));
    }
}
