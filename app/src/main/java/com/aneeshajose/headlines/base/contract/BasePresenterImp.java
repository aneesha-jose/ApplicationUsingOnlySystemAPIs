package com.aneeshajose.headlines.base.contract;

import android.content.Context;
import android.util.Log;

import androidx.annotation.CallSuper;

import com.aneeshajose.headlines.db.LocalDataSource;
import com.aneeshajose.headlines.network.ApiCallbacks;
import com.aneeshajose.headlines.network.CallTags;
import com.aneeshajose.headlines.network.RemoteDataSource;

import java.util.Map;

/**
 * Created by swapnull on 06/12/16.
 */

public abstract class BasePresenterImp implements ApiCallbacks {

    protected final Context context;
    protected final LocalDataSource localDataSource;
    protected final RemoteDataSource remoteDataSource;

    public BasePresenterImp(BaseView baseView, Context context, LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.context = context;
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @CallSuper
    @Override
    public void onError(@CallTags.CallIdentifiers String callTag, Throwable e, Map<String, Object> extras) {
        Log.d(callTag, "onNext: Failed -- " + callTag + " : " + e.getMessage());
    }
}
