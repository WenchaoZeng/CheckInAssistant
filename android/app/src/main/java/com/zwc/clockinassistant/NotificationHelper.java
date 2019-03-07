package com.zwc.clockinassistant;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper {
    public static String updateNotification(Context context) {
        String notificationText = "";
        if (Global.shouldCheckIn) {
            notificationText = "上班打卡";
        } else if (Global.shouldCheckOut) {
            notificationText = "下班打卡";
        }

        int notificationId = 2;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);

        if (notificationText != "") {
            notificationManager.notify(notificationId, buildNotification(context, notificationText));
        }

        return notificationText;
    }

    public static Notification buildNotification(Context context, String text) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "clock")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(text)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder.build();
    }
}
