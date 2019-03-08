package com.zwc.clockinassistant;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Date;

public class WifiHelper {
    public static void checkWifi(Context context) {

        // 忽略一段时间
        if (System.currentTimeMillis() < Global.ignoreTime) {
            return;
        }

        // 初始化当前状态
        Global.initializeToday();

        // 获取WIFI名称
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String wifiName  = info.getSSID();
        wifiName = wifiName.replace("\"", "");

        // 检测是否需要打卡
        if (Global.wifiNames.contains(wifiName)) {
            if (!Global.checkedIn) {
                Global.shouldCheckIn = true;
                NotificationHelper.startNotificationThread(context);
            }

            // 取消本次的下班提醒
            Global.shouldCheckOut = false;
        } else {
            if (Global.checkedIn && !Global.checkedOut && !shouldIgnore(new Date())) {
                Global.shouldCheckOut = true;
                NotificationHelper.startNotificationThread(context);
            }
        }
    }

    static boolean shouldIgnore(Date date) {
        double todayHours = ((double)date.getTime() / (1000 * 60 * 60) + 8) % 24;
        if (Global.ignoreBeforeLegalClockOutTime) {
            return todayHours < 18.5;
        }
        return false;
    }
}
