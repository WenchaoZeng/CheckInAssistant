package com.zwc.clockinassistant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Date;

public class MainService extends Service {

    Thread thread = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // 监听网络变动
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkWifi(intent);
            }
        }, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));

        // 防止进程被杀
        startForeground(1, NotificationHelper.buildNotification(this, "打卡助手"));

        startNotificationThread();
    }

    void startNotificationThread() {
        if (thread != null && thread.isAlive()) {
            return;
        }

        final MainService _this = this;
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

    void checkWifi(Intent intent) {

        // 忽略一段时间
        if (System.currentTimeMillis() < Global.ignoreTime) {
            return;
        }

        // 初始化当前状态
        Global.initializeToday();

        // 获取WIFI名称
        String wifiName = "";
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (ConnectivityManager.TYPE_WIFI == netInfo.getType ()) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();
                wifiName  = info.getSSID();
                wifiName = wifiName.replace("\"", "");
            }
        }

        // 检测是否需要打卡
        if (Global.wifiNames.contains(wifiName)) {
            if (!Global.checkedIn) {
                Global.shouldCheckIn = true;
                startNotificationThread();
            }
        } else {
            if (Global.checkedIn && !Global.checkedOut && !isLunchTime(new Date())) {
                Global.shouldCheckOut = true;
                startNotificationThread();
            }
        }

    }

    boolean isLunchTime(Date date) {
        double todayHours = ((double)date.getTime() / (1000 * 60 * 60) + 8) % 24;
        return todayHours >= 11.5 && todayHours <= 14;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        thread.interrupt();
        thread = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
