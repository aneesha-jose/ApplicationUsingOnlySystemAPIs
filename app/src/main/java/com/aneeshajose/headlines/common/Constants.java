package com.aneeshajose.headlines.common;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Aneesha Jose on 2020-04-05.
 */
public interface Constants {
    String APP_DB_NAME = "HeadLines.db";
    long SPLASH_DELAY = 1000L;
    String BASE_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/";
    String WEB_URL = "WebURL";
    String WEB_TITLE = "WebTitle";
    String ITEM_POSITION = "ItemPosition";
    int REQ_CODE_INTENT = 23432;

    int READ_TIMEOUT = 4000;
    int CONNECTION_TIMEOUT = 4000;

    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GET, POST, PUT})
    @interface NetworkCallTypes {
    }
}
