package com.aneeshajose.headlines.displayarticles;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.aneeshajose.headlines.R;
import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.common.Constants;
import com.aneeshajose.headlines.db.LocalDataSource;
import com.aneeshajose.headlines.models.Article;
import com.aneeshajose.headlines.network.CallTags;
import com.aneeshajose.headlines.network.RemoteDataSource;
import com.aneeshajose.headlines.utils.DateTimeUtils;
import com.aneeshajose.headlines.utils.OptionalUtils;
import com.aneeshajose.miscellaneous.utils.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public class DisplayArticlePresenterImp extends DisplayArticleContract.DisplayArticlePresenter {

    private static final Comparator<Article> OLDEST_TO_NEWEST = (article, t1) -> DateTimeUtils.stringToDate(OptionalUtils.orEmpty(article.getPublishedAt()), DateTimeUtils.UTC_FORMAT)
            .compareTo(DateTimeUtils.stringToDate(OptionalUtils.orEmpty(t1.getPublishedAt()), DateTimeUtils.UTC_FORMAT));

    private static final Comparator<Article> NEWEST_TO_OLDEST = (article, t1) -> DateTimeUtils.stringToDate(OptionalUtils.orEmpty(t1.getPublishedAt()), DateTimeUtils.UTC_FORMAT)
            .compareTo(DateTimeUtils.stringToDate(OptionalUtils.orEmpty(article.getPublishedAt()), DateTimeUtils.UTC_FORMAT));

    private DisplayArticleContract.View view;
    private List<Article> tempArticleHolder;
    private List<String> callTracker = new ArrayList<>();

    @Inject
    public DisplayArticlePresenterImp(BaseView baseView, @ApplicationContext Context context, LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        super(baseView, context, localDataSource, remoteDataSource);
        view = (DisplayArticleContract.View) baseView;
    }

    @Override
    public void getArticles(Activity activity) {
        view.showProgress();
        getArticlesFromServer(activity);
        getSavedArticles();
    }

    @Override
    void getArticlesFromServer(Activity activity) {
        callTracker.add(CallTags.GET_ARTICLES);
        remoteDataSource.getArticles(this, null);
    }

    @Override
    void getSavedArticles() {
        callTracker.add(CallTags.GET_SAVED_ARTICLES);
        localDataSource.getAllSavedArticles(this, null);
    }

    @Override
    public void manageArticle(Article element, int position) {
        Map<String, Object> extras = new HashMap<>();
        extras.put(Constants.ITEM_POSITION, position);
        if (element.isSaved()) {
            localDataSource.insertArticle(element, this, extras);
        } else {
            localDataSource.deleteArticle(element, this, extras);
        }
    }

    @Override
    public List<Article> getFilteredArticles(List<Article> masterList, String publisher) {
        List<Article> filteredList = new ArrayList<>();
        for (Article article : masterList) {
            if (article.getSource() != null && OptionalUtils.orEmpty(article.getSource().getName()).equalsIgnoreCase(publisher)) {
                filteredList.add(article);
            }
        }
        return filteredList;
    }

    @Override
    public List<Article> sortArticles(List<Article> masterList, boolean isAsc) {
        Collections.sort(masterList, isAsc ? OLDEST_TO_NEWEST : NEWEST_TO_OLDEST);
        return masterList;
    }

    @Override
    public <T> void onSuccess(String callTag, T response, Map<String, Object> extras) {
        callTracker.remove(callTag);
        switch (callTag) {
            case CallTags.GET_ARTICLES:
                goToNextScene((List<Article>) response, false);
                break;
            case CallTags.GET_SAVED_ARTICLES:
                goToNextScene((List<Article>) response, true);
                break;
        }
    }

    @Override
    public void onError(String callTag, Throwable e, Map<String, Object> extras) {
        callTracker.remove(callTag);
        switch (callTag) {
            case CallTags.INSERT_ARTICLE:
                view.showMessage(context.getString(R.string.oops_something_went_wrong), false);
                view.updateSavedStatus(Optional.orElse((int) extras.get(Constants.ITEM_POSITION), -1).get(), false);
                return;
            case CallTags.DELETE_ARTICLE:
                view.showMessage(context.getString(R.string.oops_something_went_wrong), false);
                view.updateSavedStatus(Optional.orElse((int) extras.get(Constants.ITEM_POSITION), -1).get(), true);
                return;
            case CallTags.GET_ARTICLES:
                handleFetchArticlesFromServerError();
                return;
            case CallTags.GET_SAVED_ARTICLES:
                goToNextScene(new ArrayList<>(), true);
                return;
        }
        super.onError(callTag, e, extras);
    }

    private void handleFetchArticlesFromServerError() {
        //context.getString(tempArticleHolder == null || tempArticleHolder.isEmpty() ? R.string.alien_blocking_signal : R.string.alien_blocking_signal_try_again)
            view.showMessage(context.getString(isTemoArticleHolderEmpty() ? R.string.alien_blocking_signal : R.string.alien_blocking_signal_try_again), callTracker.isEmpty() && isTemoArticleHolderEmpty());
        goToNextScene(new ArrayList<>(), false);
    }

    private boolean isTemoArticleHolderEmpty(){
        return tempArticleHolder == null || tempArticleHolder.isEmpty();
    }

    private void goToNextScene(List<Article> articles, boolean isSavedArticles) {
        if (callTracker.isEmpty()) {
            view.hideProgress();
            handleArticleSync(Optional.orElse(isSavedArticles ? articles : tempArticleHolder, new ArrayList<Article>()).get(),
                    Optional.orElse(isSavedArticles ? tempArticleHolder : articles, new ArrayList<Article>()).get());
            return;
        }
        tempArticleHolder = articles == null ? new ArrayList<>() : articles;
    }

    private void handleArticleSync(List<Article> savedArticles, List<Article> fetchedFromServer) {
        if (fetchedFromServer.isEmpty()) {
            fetchedFromServer.addAll(savedArticles);
        } else {
            for (Article savedArticle : savedArticles) {
                if (fetchedFromServer.contains(savedArticle)) {
                    fetchedFromServer.get(fetchedFromServer.indexOf(savedArticle)).setSaved(true);
                } else fetchedFromServer.add(savedArticle);
            }
        }
        view.onArticlesLoaded(fetchedFromServer);
    }

    public List<String> getListPublishers(List<Article> articles) {
        List<String> publishers = new ArrayList<>();
        for (Article article : articles) {
            if (article.getSource() != null && !TextUtils.isEmpty(article.getSource().getName()) && !publishers.contains(article.getSource().getName()))
                publishers.add(article.getSource().getName());
        }
        return publishers;
    }
}
