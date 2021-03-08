package com.zwc.clockinassistant;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Global {

    // 设置
    public static List<String> wifiNames;
    private static Date checkoutTime;
    private static Thread thread = null;
    public static boolean changed;

    // 当日打卡信息
    static int todayDay;
    public static boolean shouldCheckIn;
    public static boolean checkedIn;
    public static boolean shouldCheckOut;
    public static boolean checkedOut;
    public static long ignoreTime;

    public static void initializeToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (nowDay != todayDay) {
            todayDay = nowDay;
            shouldCheckIn = false;
            checkedIn = false;
            shouldCheckOut = false;
            checkedOut = false;
            ignoreTime = 0;
        }
    }

    public static String getCheckoutTimeAsString() {
        if (checkoutTime == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(checkoutTime);
    }

    public static void setCheckoutTimeFromString(String input, Context context) {
        input = String.format("2000-01-01 %s", input);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            checkoutTime = format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 初始化自动打卡线程
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        thread = new Thread(() -> {
            while (true) {
                long milliseconds = diffToCheckoutTime(new Date());
                if (milliseconds >= 0) {
                    milliseconds = milliseconds - 24L * 3600 * 1000;
                }
                milliseconds = -milliseconds;
                milliseconds += 60 * 1000;
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    break;
                }

                WifiHelper.checkWifi(context);
            }
        });
        thread.start();
    }

    public static long diffToCheckoutTime(Date now) {
        if (checkoutTime == null) {
            return 24 * 3600 * 1000;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime().getTime() - checkoutTime.getTime();
    }
}
