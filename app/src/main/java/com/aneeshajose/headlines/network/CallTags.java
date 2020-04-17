package com.aneeshajose.headlines.network;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Aneesha on 13/10/17.
 */

public interface CallTags {
    String GET_ARTICLES = "getArticles";
    String GET_SAVED_ARTICLES = "getSavedArticles";
    String INSERT_ARTICLE = "insertArticle";
    String DELETE_ARTICLE = "deleteArticle";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GET_ARTICLES, GET_SAVED_ARTICLES, INSERT_ARTICLE, DELETE_ARTICLE})
    @interface CallIdentifiers {
    }
}
