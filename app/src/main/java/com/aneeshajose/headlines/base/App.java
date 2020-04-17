package com.aneeshajose.headlines.base;

import android.app.Application;

import com.aneeshajose.headlines.base.modules.AppModule;
import com.aneeshajose.headlines.refreshData.RefreshDataIntentService;

/**
 * Created by Aneesha Jose on 2020-04-05.
 */
public class App extends Application {

    protected AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        buildAppComponent();
        RefreshDataIntentService.cancelRefresh(this);
    }

    public void buildAppComponent() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RefreshDataIntentService.scheduleForRefresh(this);
        component.getAppSqlHelper().close();
    }
}
