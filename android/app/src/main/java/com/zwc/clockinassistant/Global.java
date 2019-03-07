package com.zwc.clockinassistant;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Global {

    // 设置
    public static List<String> wifiNames;
    public static boolean ignoreBeforeLegalClockOutTime;
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
}
