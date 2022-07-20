package com.daiki.android.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(android.R.id.home == item.getItemId()) {
            finish();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.i("MainActivity2","戻るボタン押された");
        super.onBackPressed();
    }
}