package com.aneeshajose.headlines.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aneeshajose.headlines.DependencyInjector;
import com.aneeshajose.headlines.R;
import com.aneeshajose.headlines.base.activity.BaseActivity;
import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.miscellaneous.utils.Optional;

import butterknife.BindView;

import static com.aneeshajose.headlines.common.Constants.WEB_TITLE;
import static com.aneeshajose.headlines.common.Constants.WEB_URL;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.lSwipeRefresh)
    SwipeRefreshLayout lSwipeRefresh;

    private String url = "";

    public static Intent createInstance(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.putExtra(WEB_TITLE, title);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
        setUpWebView();
    }

    public void initView() {
        String title = "";
        if (getIntent() != null) {
            url = getIntent().getStringExtra(WEB_URL);
            title = Optional.orElse(getIntent().getStringExtra(WEB_TITLE), getString(R.string.app_name), String::isEmpty).get();
        }
        tvToolbarTitle.setText(title);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView() {
        final WebView webview = findViewById(R.id.webView);
        lSwipeRefresh.setOnRefreshListener(webview::reload);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setSupportMultipleWindows(true);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                shouldOverrideUrlLoading(view, request.getUrl().toString());
                return true;
            }
        });
        if (checkUrl()) {
            webview.loadUrl(url);
        }
    }

    private boolean checkUrl() {
        if (url == null || url.isEmpty()) {
            showError(getString(R.string.something_went_wrong), false);
            return false;
        }
        return true;
    }

    @Override
    protected BaseView getBaseView() {
        return null;
    }

    @Override
    protected void callDependencyInjector(DependencyInjector injectorComponent) {
        injectorComponent.injectDependencies(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.exit_to_right);
    }
}
