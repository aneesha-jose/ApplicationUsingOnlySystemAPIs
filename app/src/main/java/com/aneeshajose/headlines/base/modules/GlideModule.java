package com.aneeshajose.headlines.base.modules;

import android.content.Context;

import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.base.scopes.ApplicationScope;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
@Module
public class GlideModule {

    @ApplicationScope
    @Provides
    public RequestManager glideRequestManager(@ApplicationContext Context context) {
        return Glide.with(context);
    }

}
