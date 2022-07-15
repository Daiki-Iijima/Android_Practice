package com.daiki.android.coordinatorlayoutsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  xmlで追加したToolbarの取得
        Toolbar toolbar = findViewById(R.id.toolbar);
        //  ツールバーにアイテムを追加
        //  ロゴ
        toolbar.setLogo(R.mipmap.ic_launcher);
        //  タイトル文字列と色を設定
        toolbar.setTitle(R.string.toolbar_title);
        toolbar.setTitleTextColor(Color.WHITE);
        //  サブタイトルの文字列と色を設定
        toolbar.setSubtitle(R.string.toolbar_subtitle);
        toolbar.setSubtitleTextColor(Color.LTGRAY);

        //  アクションバーにツールバーを設定
        setSupportActionBar(toolbar);
    }
}