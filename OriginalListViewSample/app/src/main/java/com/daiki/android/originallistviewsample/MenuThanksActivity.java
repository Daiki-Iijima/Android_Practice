package com.daiki.android.originallistviewsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class MenuThanksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_thanks);

        //  インテントオブジェクトを取得
        Intent intent = getIntent();

        //  前の画面から渡されたデータを取得
        String menuName = intent.getStringExtra("menuName");
        String menuPrice = intent.getStringExtra("menuPrice");

        //  定食名と金額を表示させるTextViewを取得
        TextView tvMenuName = findViewById(R.id.tvMenuName);
        TextView tvMenuPrice = findViewById(R.id.tvMenuPrice);

        //  前の画面から受け取ったデータを表示
        tvMenuName.setText(menuName);
        tvMenuPrice.setText(menuPrice);

        //  アクションバーを取得
        //  TODO : getActionBarメソッドもあるが、それは昔使われていたやつか(名前空間がandroid.だった)?
        ActionBar actionBar = getSupportActionBar();

        //  アクションバーの戻るメニューを有効に設定(xmlの記述は不要)
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean returnVal = true;

        int itemId = item.getItemId();

        //  Android SDKで戻るボタンのIDは[home]
        if(itemId == android.R.id.home) {
            finish();
        }else{
            returnVal = super.onOptionsItemSelected(item);
        }

        return returnVal;
    }
}