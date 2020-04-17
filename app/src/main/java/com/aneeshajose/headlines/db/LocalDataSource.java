package com.aneeshajose.headlines.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.base.scopes.ApplicationScope;
import com.aneeshajose.headlines.common.TaskRunner;
import com.aneeshajose.headlines.db.models.ArticleContract;
import com.aneeshajose.headlines.models.Article;
import com.aneeshajose.headlines.network.ApiCallbacks;
import com.aneeshajose.headlines.network.CallTags;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
@ApplicationScope
public class LocalDataSource {

    private Context context;
    private SQLiteDatabase db;

    @Inject
    public LocalDataSource(@ApplicationContext Context context, AppSqlHelper sqlHelper) {
        this.context = context;
        this.db = sqlHelper.getWritableDatabase();
    }

    public void getAllSavedArticles(ApiCallbacks callbacks, Map<String, Object> extras) {
        new TaskRunner().executeAsync(() -> ArticleContract.getInstance().readAllEntries(db),
                result -> onSavedArticlesFetched(result, callbacks, extras),
                t -> callbacks.onError(CallTags.GET_SAVED_ARTICLES, t, extras));
    }

    private void onSavedArticlesFetched(Cursor cursor, ApiCallbacks apiCallbacks, Map<String, Object> extras) {
        List<Article> articles = ArticleContract.getInstance().getArticles(cursor);
        apiCallbacks.onSuccess(CallTags.GET_SAVED_ARTICLES, articles, extras);
    }

    public void insertArticle(Article article, ApiCallbacks callbacks, Map<String, Object> extras) {
        article.setSaved(true);
        new TaskRunner().executeAsyncWrite(() -> ArticleContract.getInstance().insert(article, db),
                () -> {
                },
                t -> callbacks.onError(CallTags.INSERT_ARTICLE, t, extras));
    }

    public void deleteArticle(Article article, ApiCallbacks callbacks, Map<String, Object> extras) {
        article.setSaved(true);
        new TaskRunner().executeAsyncWrite(() -> ArticleContract.getInstance().delete(article, db),
                () -> {
                },
                t -> callbacks.onError(CallTags.INSERT_ARTICLE, t, extras));
    }

    public void updateArticle(Article article, ApiCallbacks callbacks, Map<String, Object> extras) {
        article.setSaved(true);
        new TaskRunner().executeAsyncWrite(() -> ArticleContract.getInstance().update(article, db),
                () -> {
                },
                t -> callbacks.onError(CallTags.INSERT_ARTICLE, t, extras));
    }
}
