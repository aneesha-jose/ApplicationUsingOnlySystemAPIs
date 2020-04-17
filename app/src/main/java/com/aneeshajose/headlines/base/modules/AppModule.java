package com.aneeshajose.headlines.base.modules;

import android.content.Context;

import com.aneeshajose.headlines.base.App;
import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.base.scopes.ApplicationScope;
import com.aneeshajose.headlines.db.AppSqlHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @ApplicationContext
    @ApplicationScope
    @Provides
    public Context getContext() {
        return this.app;
    }

    @ApplicationScope
    @Provides
    public AppSqlHelper getAppSqlHelper() {
        return new AppSqlHelper(app);
    }

}
