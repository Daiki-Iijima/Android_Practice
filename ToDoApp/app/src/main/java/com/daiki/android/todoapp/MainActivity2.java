package com.daiki.android.todoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("タイトル");
        bar.setSubtitle("サブタイトル");
        bar.setDisplayShowHomeEnabled(true);    // アイコンやロゴ、戻るボタンなどを表示する場合は有効にする
        //bar.setDisplayUseLogoEnabled(true);     //  ロゴを表示する
        bar.setDisplayHomeAsUpEnabled(true);    //  戻るボタン
        bar.setIcon(android.R.drawable.ic_media_next);
        //bar.setLogo(android.R.drawable.ic_dialog_alert);
    }
}