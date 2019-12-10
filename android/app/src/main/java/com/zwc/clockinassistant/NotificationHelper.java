package com.zwc.clockinassistant;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper {

    static Thread thread = null;
    static int count;

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
            count++;
            notificationManager.notify(notificationId, buildNotification(context, notificationText + " " + count));
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

    public static void startNotificationThread(Context context) {
        if (thread != null && thread.isAlive()) {
            return;
        }

        final Context _this = context;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    String text = NotificationHelper.updateNotification(_this);

                    // 没有提醒, 退出线程
                    if (text.equals("")) {
                        break;
                    }

                    // 定期提醒
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        thread.start();
    }
}
