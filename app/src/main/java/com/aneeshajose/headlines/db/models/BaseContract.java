package com.aneeshajose.headlines.db.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aneesha Jose on 2020-04-05.
 */
public interface BaseContract<T> {

    void insert(T element, SQLiteDatabase db);

    void update(T element, SQLiteDatabase db);

    void delete(T element, SQLiteDatabase db);

    Cursor readAllEntries(SQLiteDatabase db);
}
