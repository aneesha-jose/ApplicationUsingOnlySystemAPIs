package com.aneeshajose.headlines.base.modules;

import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.headlines.base.scopes.BaseActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aneesha on 13/10/17.
 */
@Module
public class BaseViewModule {

    private final BaseView baseView;

    public BaseViewModule(BaseView baseView) {
        this.baseView = baseView;
    }

    @BaseActivityScope
    @Provides
    public BaseView getBaseView() {
        return baseView;
    }

}
