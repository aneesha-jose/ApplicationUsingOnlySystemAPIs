package com.aneeshajose.headlines.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aneeshajose.headlines.db.models.ArticleContract;

import static com.aneeshajose.headlines.common.Constants.APP_DB_NAME;

/**
 * Created by Aneesha Jose on 2020-04-05.
 */
public class AppSqlHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public AppSqlHelper(Context context) {
        super(context, APP_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ArticleContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void clearAllData(){
        getReadableDatabase().execSQL(ArticleContract.SQL_DELETE_ENTRIES);
    }
}
