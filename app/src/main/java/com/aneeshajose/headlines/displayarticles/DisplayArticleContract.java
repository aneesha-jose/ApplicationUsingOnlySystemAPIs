package com.aneeshajose.headlines.displayarticles;

import android.app.Activity;
import android.content.Context;

import com.aneeshajose.headlines.base.contract.BasePresenterImp;
import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.headlines.db.LocalDataSource;
import com.aneeshajose.headlines.models.Article;
import com.aneeshajose.headlines.network.RemoteDataSource;

import java.util.List;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public interface DisplayArticleContract {

    interface View extends BaseView {
        void onArticlesLoaded(List<Article> articles);

        void updateSavedStatus(int position, boolean saved);
    }

    abstract class DisplayArticlePresenter extends BasePresenterImp {

        public DisplayArticlePresenter(BaseView baseView, Context context, LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
            super(baseView, context, localDataSource, remoteDataSource);
        }

        abstract public void getArticles(Activity activity);

        abstract void getArticlesFromServer(Activity activity);

        abstract void getSavedArticles();

        abstract public void manageArticle(Article element, int position);

        abstract public List<Article> getFilteredArticles(List<Article> masterList, String publisher);

        abstract public List<Article> sortArticles(List<Article> masterList, boolean isAsc);

        abstract public List<String> getListPublishers(List<Article> articles);

    }
}
