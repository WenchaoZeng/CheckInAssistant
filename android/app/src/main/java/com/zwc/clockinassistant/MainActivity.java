package com.zwc.clockinassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    final String wifiKey = "wifiNames";
    final String ignoreBeforeLegalClockOutTimeKey = "ignoreBeforeLegalClockOutTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化当前状态
        Global.initializeToday();

        // 读取配置
        if (Global.wifiNames == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
            Set<String> set = sharedPreferences.getStringSet(wifiKey, new HashSet<String>());
            Global.wifiNames = new ArrayList<>(set);

            Global.ignoreBeforeLegalClockOutTime = sharedPreferences.getBoolean(ignoreBeforeLegalClockOutTimeKey, true);
        }

        // 开启服务
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 保存配置
        if (Global.changed) {
            SharedPreferences sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(wifiKey, new HashSet<>(Global.wifiNames));
            editor.putBoolean(ignoreBeforeLegalClockOutTimeKey, Global.ignoreBeforeLegalClockOutTime);
            editor.commit();

            WifiHelper.checkWifi(this);
        }

        showStatus();
    }

    public void showStatus() {

        // 显示状态
        TextView statusTextView = findViewById(R.id.statusTextView);
        String text = "";
        if (Global.checkedIn) {
            text += "上班: 已打卡 \n\n";
        } else if (Global.shouldCheckIn) {
            text += "上班: 需要打卡 \n\n";
        } else {
            text += "上班: - \n\n";
        }

        if (Global.checkedOut) {
            text += "下班: 已打卡 \n\n";
        } else if (Global.shouldCheckOut) {
            text += "下班: 需要打卡 \n\n";
        } else {
            text += "下班: - \n\n";
        }

        statusTextView.setText(text);

        // 显示按钮
        boolean enabled = Global.shouldCheckIn || Global.shouldCheckOut;
        findViewById(R.id.ignoreButton).setEnabled(enabled);
        findViewById(R.id.checkButton).setEnabled(enabled);
        findViewById(R.id.ignoreOneHourButton).setEnabled(enabled);
        findViewById(R.id.cancelIgnoreButton).setEnabled(System.currentTimeMillis() < Global.ignoreTime);
    }

    public void openSettingPage(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void check(View view) {
        if (Global.shouldCheckIn) {
            Global.checkedIn = true;
            Global.shouldCheckIn = false;
        } else if (Global.shouldCheckOut) {
            Global.checkedOut = true;
            Global.shouldCheckOut = false;
        }

        showStatus();
        NotificationHelper.updateNotification(this);
    }

    public void ignore(View view) {
        if (Global.shouldCheckIn) {
            Global.shouldCheckIn = false;
        } else if (Global.shouldCheckOut) {
            Global.shouldCheckOut = false;
        }

        showStatus();
        NotificationHelper.updateNotification(this);
    }

    public void ignoreOneHour(View view) {
        Global.ignoreTime = System.currentTimeMillis() + (60 * 60 * 1000);
        ignore(view);
    }

    public void cancelIgnore(View view) {
        Global.ignoreTime = 0;
        WifiHelper.checkWifi(this);
        showStatus();
    }
}
