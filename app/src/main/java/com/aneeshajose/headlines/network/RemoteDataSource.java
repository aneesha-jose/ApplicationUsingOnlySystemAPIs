package com.aneeshajose.headlines.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.aneeshajose.headlines.base.qualifiers.ApplicationContext;
import com.aneeshajose.headlines.base.qualifiers.BaseUrl;
import com.aneeshajose.headlines.base.scopes.ApplicationScope;
import com.aneeshajose.headlines.common.Constants;
import com.aneeshajose.headlines.common.TaskRunner;
import com.aneeshajose.headlines.models.ArticleWrapper;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import javax.inject.Inject;

import static com.aneeshajose.headlines.network.CallTags.GET_ARTICLES;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
@ApplicationScope
public class RemoteDataSource {

    private static final String ARTICLES_ENDPOINT = "news-api-feed/staticResponse.json";

    private Context context;
    private String baseUrl;

    @Inject
    public RemoteDataSource(@ApplicationContext Context context, @BaseUrl String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    public void getArticles(ApiCallbacks callbacks, Map<String, Object> extras) {
        if (isNetworkNotConnected()) {
            callbacks.onError(GET_ARTICLES, new RuntimeException("No Network connection found."), extras);
            return;
        }
        new TaskRunner().executeAsync(new NetworkCallHandler<>(baseUrl.concat(ARTICLES_ENDPOINT), new TypeToken<ArticleWrapper>() {
                }, Constants.GET),
                result -> {
                    callbacks.onSuccess(GET_ARTICLES, result.getArticles(), extras);
                },
                t -> callbacks.onError(GET_ARTICLES, t, extras));
    }

    private boolean isNetworkNotConnected() {
        NetworkInfo networkInfo = getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE);
    }

    private NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
    }
}
