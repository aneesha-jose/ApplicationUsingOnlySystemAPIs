package com.aneeshajose.headlines.base.modules;

import android.content.Context;

import com.aneeshajose.headlines.base.qualifiers.ActivityContext;
import com.aneeshajose.headlines.base.scopes.BaseActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
@Module
public class ActivityModule {

    private Context activity;

    public ActivityModule(Context activity) {
        this.activity = activity;
    }

    @ActivityContext
    @BaseActivityScope
    @Provides
    public Context activity() {
        return activity;
    }


}
