package com.aneeshajose.headlines.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.aneeshajose.headlines.DependencyInjector;
import com.aneeshajose.headlines.R;
import com.aneeshajose.headlines.base.activity.BaseActivity;
import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.headlines.displayarticles.DisplayNewsArticlesActivity;

import static com.aneeshajose.headlines.common.Constants.SPLASH_DELAY;


public class SplashActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        baseToolbar.setVisibility(View.GONE);
        initView();
    }

    private void initView() {
        new Handler().postDelayed(this::displayNewsArticles, SPLASH_DELAY);
    }

    private void displayNewsArticles() {
        startActivity(new Intent(this, DisplayNewsArticlesActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.stay);
        finish();
    }

    @Override
    protected BaseView getBaseView() {
        return null;
    }

    @Override
    protected void callDependencyInjector(DependencyInjector injectorComponent) {
        injectorComponent.injectDependencies(this);
    }
}
