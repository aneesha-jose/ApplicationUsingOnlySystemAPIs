package com.aneeshajose.headlines.refreshData;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.aneeshajose.headlines.DaggerDependencyInjector;
import com.aneeshajose.headlines.DependencyInjector;
import com.aneeshajose.headlines.base.App;
import com.aneeshajose.headlines.base.modules.ActivityModule;
import com.aneeshajose.headlines.base.modules.BaseViewModule;

import java.security.SecureRandom;

import javax.inject.Inject;

import static com.aneeshajose.headlines.common.Constants.REQ_CODE_INTENT;

/**
 * Created by Aneesha Jose on 2020-04-07.
 */
public class RefreshDataIntentService extends IntentService implements RefreshDataContract.View {

    @Inject
    RefreshDataPresenterImp presenterImp;

    public RefreshDataIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initialiseDaggerDependencies();


    }

    private void initialiseDaggerDependencies() {
        callDependencyInjector(initialiseDaggerInjector());
    }

    private DependencyInjector initialiseDaggerInjector() {
        return DaggerDependencyInjector.builder()
                .appComponent(((App) getApplication()).getComponent())
                .activityModule(new ActivityModule(this))
                .baseViewModule(new BaseViewModule(this))
                .build();
    }

    private void callDependencyInjector(DependencyInjector injectorComponent) {
        injectorComponent.injectDependencies(this);
    }

    @Override
    public void onArticlesLoaded() {
        scheduleForRefresh(getApplicationContext());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String toastMessage, boolean showErrorView) {

    }

    public static void scheduleForRefresh(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmMgr == null)
            return;

        Intent intent = new Intent(context, RefreshDataIntentService.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQ_CODE_INTENT, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + getHourDelay(), alarmIntent);
    }

    public static void cancelRefresh(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmMgr == null)
            return;

        Intent intent = new Intent(context, RefreshDataIntentService.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQ_CODE_INTENT, intent, 0);

        alarmIntent.cancel();
    }

    private static long getHourDelay() {
        return (new SecureRandom().nextInt(5) + 1) * 60 * 60 * 1000; //Converting to millis
    }
}
