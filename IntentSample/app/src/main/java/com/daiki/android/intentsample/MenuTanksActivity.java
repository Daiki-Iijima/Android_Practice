package com.daiki.android.intentsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuTanksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tanks);

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
    }

    public void onBackButtonClick(View view){
        finish();
    }
}