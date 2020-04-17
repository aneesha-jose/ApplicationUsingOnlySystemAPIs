package com.aneeshajose.headlines.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public class TaskRunner {
    private final Executor writeExecutor = Executors.newSingleThreadExecutor();
    private final Executor executor = new ThreadPoolExecutor(5, 128, 10,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void onComplete(R result);
    }

    public <R> void executeAsyncWrite(Callable<R> callable, Callback<R> callback, Callback<Throwable> errorCallback) {
        writeExecutor.execute(() -> {
            final R result;
            try {
                result = callable.call();
                handler.post(() -> callback.onComplete(result));
            } catch (Exception e) {
                handler.post(() -> errorCallback.onComplete(e));
            }
        });
    }

    public void executeAsyncWrite(Runnable runnable, Runnable onComplete, Callback<Throwable> errorCallback) {
        writeExecutor.execute(() -> {
            try {
                runnable.run();
                handler.post(onComplete);
            } catch (Exception e) {
                handler.post(() -> errorCallback.onComplete(e));
            }
        });
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback, Callback<Throwable> errorCallback) {
        executor.execute(() -> {
            final R result;
            try {
                result = callable.call();
                handler.post(() -> callback.onComplete(result));
            } catch (Exception e) {
                handler.post(() -> errorCallback.onComplete(e));
            }
        });
    }

    public void executeAsync(Runnable runnable, Runnable onComplete, Callback<Throwable> errorCallback) {
        executor.execute(() -> {
            try {
                runnable.run();
                handler.post(onComplete);
            } catch (Exception e) {
                handler.post(() -> errorCallback.onComplete(e));
            }
        });
    }
}
