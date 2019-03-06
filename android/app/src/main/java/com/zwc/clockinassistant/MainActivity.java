package com.zwc.clockinassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        String wifiKey = "wifiNames";

        // 读取配置
        if (Global.wifiNames == null) {
            Set<String> set = sharedPreferences.getStringSet(wifiKey, new HashSet<String>());
            Global.wifiNames = new ArrayList<>(set);
        }

        // 保存配置
        if (Global.changed) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(wifiKey, new HashSet<>(Global.wifiNames));
            editor.commit();
        }

        // 显示状态
        TextView statusTextView = findViewById(R.id.statusTextView);
        String text = "";
        text += "上班: 20:13:19 急速打卡 \n\n";
        text += "下班: 手动 \n\n";
        statusTextView.setText(text);
    }


    public void openSettingPage(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        finish();
    }

    public void check(View view) {

    }
}
