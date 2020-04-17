package com.aneeshajose.headlines.base.modules;


import com.aneeshajose.headlines.base.qualifiers.BaseUrl;
import com.aneeshajose.headlines.base.scopes.ApplicationScope;
import com.aneeshajose.headlines.common.Constants;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;


/**
 * Created by Aneesha on 12/10/17.
 */

@Module
public class NetworkModule {

    @ApplicationScope
    @Provides
    public Gson gson() {
        return new Gson();
    }

    @BaseUrl
    @ApplicationScope
    @Provides
    public String getBaseUrl() {
        return Constants.BASE_URL;
    }

}
