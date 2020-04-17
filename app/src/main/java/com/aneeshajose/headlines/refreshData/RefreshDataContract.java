package com.aneeshajose.headlines.refreshData;

import android.content.Context;

import com.aneeshajose.headlines.base.contract.BasePresenterImp;
import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.headlines.db.LocalDataSource;
import com.aneeshajose.headlines.network.RemoteDataSource;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public interface RefreshDataContract {

    interface View extends BaseView {
        void onArticlesLoaded();
    }

    abstract class Presenter extends BasePresenterImp {

        public Presenter(BaseView baseView, Context context, LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
            super(baseView, context, localDataSource, remoteDataSource);
        }

        abstract public void getArticles();

        abstract void getArticlesFromServer();

        abstract void getSavedArticles();

    }
}
