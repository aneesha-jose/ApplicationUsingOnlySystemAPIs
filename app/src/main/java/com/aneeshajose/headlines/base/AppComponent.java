package com.aneeshajose.headlines.base;

import android.content.Context;

import com.aneeshajose.headlines.base.modules.AppModule;
import com.aneeshajose.headlines.base.modules.GlideModule;
import com.aneeshajose.headlines.base.modules.NetworkModule;
import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.base.scopes.ApplicationScope;
import com.aneeshajose.headlines.db.AppSqlHelper;
import com.aneeshajose.headlines.db.LocalDataSource;
import com.aneeshajose.headlines.network.RemoteDataSource;
import com.bumptech.glide.RequestManager;

import dagger.Component;

/**
 * Created by Aneesha on 12/10/17.
 */
@ApplicationScope
@Component(modules = {AppModule.class, NetworkModule.class, GlideModule.class})
public interface AppComponent {

    @ApplicationContext
    Context getContext();

    RequestManager getGlide();

    AppSqlHelper getAppSqlHelper();

    LocalDataSource getLocalDataSource();

    RemoteDataSource getRemoteDatasource();
}
