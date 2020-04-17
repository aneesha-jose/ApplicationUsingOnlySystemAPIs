package com.aneeshajose.headlines;

import android.content.Context;

import com.aneeshajose.headlines.base.AppComponent;
import com.aneeshajose.headlines.base.modules.ActivityModule;
import com.aneeshajose.headlines.base.modules.BaseViewModule;
import com.aneeshajose.headlines.base.qualifiers.ActivityContext;
import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.base.scopes.BaseActivityScope;
import com.aneeshajose.headlines.db.LocalDataSource;
import com.aneeshajose.headlines.displayarticles.DisplayNewsArticlesActivity;
import com.aneeshajose.headlines.network.RemoteDataSource;
import com.aneeshajose.headlines.refreshData.RefreshDataIntentService;
import com.aneeshajose.headlines.splash.SplashActivity;
import com.aneeshajose.headlines.webview.WebViewActivity;
import com.bumptech.glide.RequestManager;

import dagger.Component;

/**
 * Created by Aneesha Jose on 2020-04-05.
 */
@BaseActivityScope
@Component(modules = {ActivityModule.class, BaseViewModule.class}, dependencies = {AppComponent.class})
public interface DependencyInjector {

    @ApplicationContext
    Context getContext();

    @ActivityContext
    Context getActivityContext();

    RequestManager getGlide();

    LocalDataSource getLocalDataSource();

    RemoteDataSource getRemoteDatasource();

    void injectDependencies(SplashActivity activity);

    void injectDependencies(WebViewActivity activity);

    void injectDependencies(DisplayNewsArticlesActivity activity);

    void injectDependencies(RefreshDataIntentService service);
}
