package com.aneeshajose.headlines.refreshData;

import android.content.Context;

import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.db.LocalDataSource;
import com.aneeshajose.headlines.models.Article;
import com.aneeshajose.headlines.network.CallTags;
import com.aneeshajose.headlines.network.RemoteDataSource;
import com.aneeshajose.miscellaneous.utils.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public class RefreshDataPresenterImp extends RefreshDataContract.Presenter {

    private RefreshDataContract.View view;
    private List<Article> savedArticles;
    private List<String> callTracker = new ArrayList<>();

    @Inject
    public RefreshDataPresenterImp(BaseView baseView, @ApplicationContext Context context, LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        super(baseView, context, localDataSource, remoteDataSource);
        view = (RefreshDataContract.View) baseView;
    }

    @Override
    public void getArticles() {
        getSavedArticles();
    }

    @Override
    void getArticlesFromServer() {
        callTracker.add(CallTags.GET_ARTICLES);
        remoteDataSource.getArticles(this, null);
    }

    @Override
    void getSavedArticles() {
        callTracker.add(CallTags.GET_SAVED_ARTICLES);
        localDataSource.getAllSavedArticles(this, null);
    }

    @Override
    public <T> void onSuccess(String callTag, T response, Map<String, Object> extras) {
        callTracker.remove(callTag);
        switch (callTag) {
            case CallTags.GET_ARTICLES:
                goToNextScene((List<Article>) response);
                break;
            case CallTags.GET_SAVED_ARTICLES:
                handleSavedArticlesResponse((List<Article>) response);
                break;
        }
    }

    private void handleSavedArticlesResponse(List<Article> response) {
        if (response == null || response.isEmpty()) {
            view.onArticlesLoaded();
            return;
        }
        savedArticles = response;
        getArticlesFromServer();
    }

    @Override
    public void onError(String callTag, Throwable e, Map<String, Object> extras) {
        callTracker.remove(callTag);
        view.onArticlesLoaded();
        super.onError(callTag, e, extras);
    }

    private void goToNextScene(List<Article> articles) {
        if (callTracker.isEmpty()) {
            handleArticleSync(Optional.orElse(savedArticles, new ArrayList<Article>()).get(),
                    Optional.orElse(articles, new ArrayList<Article>()).get());
        }
    }

    private void handleArticleSync(List<Article> savedArticles, List<Article> fetchedFromServer) {
        for (Article savedArticle : savedArticles) {
            if (fetchedFromServer.contains(savedArticle)) {
                localDataSource.updateArticle(fetchedFromServer.get(fetchedFromServer.indexOf(savedArticle)), this, null);
            }
        }
        view.onArticlesLoaded();
    }
}
