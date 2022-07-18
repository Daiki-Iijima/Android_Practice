package com.daiki.android.todoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("タイトル");
        bar.setSubtitle("サブタイトル");
        bar.setDisplayShowHomeEnabled(true);
        //bar.setDisplayUseLogoEnabled(true);
        //bar.setLogo(android.R.drawable.ic_delete);
        bar.setIcon(android.R.drawable.ic_media_next);

        bar.setCustomView(R.layout.sample_my_view);
        bar.setDisplayShowCustomEnabled(true);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickButton());
    }

    private class OnClickButton implements View .OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        }
    }
}