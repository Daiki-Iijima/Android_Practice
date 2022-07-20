package com.daiki.android.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        bar.setIcon(android.R.drawable.ic_media_next);

        bar.setCustomView(R.layout.sample_my_view);
        bar.setDisplayShowCustomEnabled(true);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickButton());

        //  カスタムビューの部品を取得
        View v = bar.getCustomView();
        Button btn1 = v.findViewById(R.id.btn1);
        btn1.setOnClickListener(new OnClickButton());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  インフレーターを取得する
        MenuInflater inflater = getMenuInflater();

        //  引数でうけとったMenuにレイアウトファイルをインフレート
        inflater.inflate(R.menu.option,menu);

        //  オーバーライドしたらtrueを返す決まり
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //  レイアウトファイルで設定してあるIDを取得できるので、ここで分岐する
        int id = item.getItemId();

        if(id == R.id.test1){
            Log.i("MainActivity","テストが押されました");
        }

        return true;
    }



    private class OnClickButton implements View .OnClickListener{
        @Override
        public void onClick(View view) {
//                カスタムビューのボタンだった場合
            if(R.id.btn1 == view.getId()){
                Log.i("MainActivity","カスタムビューのボタンが押された");
            }else {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        }
    }
}