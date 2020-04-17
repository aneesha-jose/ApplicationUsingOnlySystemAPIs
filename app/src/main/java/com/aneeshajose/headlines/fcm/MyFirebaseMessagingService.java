package com.aneeshajose.headlines.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.aneeshajose.headlines.R;
import com.aneeshajose.headlines.splash.SplashActivity;
import com.aneeshajose.miscellaneous.utils.Optional;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by Aneesha Jose on 2020-04-06.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        Log.i(TAG, "From : " + remoteMessage.getFrom());
        Log.i(TAG, "Remote message : " + new Gson().toJson(data));

        StringBuilder content = new StringBuilder();

        for (String key : data.keySet()) {
            content.append(key).append(": ").append(Optional.orElse(data.get(key), "").get()).append("\n");
        }

        sendNotification(getString(R.string.app_name), content.toString());
    }

    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("isNotification", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

    @Override
    public void onNewToken(@NotNull String refreshedToken) {
        super.onNewToken(refreshedToken);
        //send refreshed token to server against logged in user for further use
    }
}
