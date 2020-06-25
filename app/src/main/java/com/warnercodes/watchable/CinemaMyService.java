package com.warnercodes.watchable;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.warnercodes.watchable.activity.MovieDetailActivity;

public class CinemaMyService extends FirebaseMessagingService {
    private static final String TAG = "SERVICE";
    private NotificationManager mNotificationManager;

    public CinemaMyService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int movieId = Integer.parseInt(remoteMessage.getData().get("movieId"));
            intent.putExtra("movieId", movieId);
            int requestCode = ("someString" + System.currentTimeMillis()).hashCode();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setFullScreenIntent(pendingIntent, false);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
                channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                channel.setVibrationPattern(new long[10]);
                manager.createNotificationChannel(channel);
            }

            manager.notify(requestCode, builder.build());


        }
    }

    private void sendNotification(int movieId) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class); // Here pass your activity where you want to redirect.
        intent.putExtra("movieId", movieId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            currentapiVersion = R.drawable.ic_local_movies_black_24dp;
        } else {
            currentapiVersion = R.mipmap.ic_launcher;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(currentapiVersion)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("msg"))
                .setContentText("msg")
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
