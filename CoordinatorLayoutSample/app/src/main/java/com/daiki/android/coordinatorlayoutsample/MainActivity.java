package com.daiki.android.coordinatorlayoutsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  xmlで追加したToolbarの取得
        Toolbar toolbar = findViewById(R.id.toolbar);
        //  ツールバーロゴを設定
        toolbar.setLogo(R.mipmap.ic_launcher);
        //  アクションバーにツールバーを設定
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        //  タイトル文字列を設定
        toolbarLayout.setTitle(getString(R.string.toolbar_title));
        //  タイトル文字列の色を設定
        //  普通サイズ時
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        //  縮小サイズ時
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

    }
}