package com.daiki.android.lifecyclesample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LifeCycleSample","Sub onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
    }

    @Override
    public void onStart() {
        Log.i("LifeCycleSample","Sub onStart() called");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("LifeCycleSample","Sub onRestart() called");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("LifeCycleSample","Sub onResume() called");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("LifeCycleSample","Sub onPause() called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("LifeCycleSample","Sub onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("LifeCycleSample","Sub onDestroy called");
        super.onDestroy();
    }

    //  次へボタンクリックイベント
    //  紐づけはxml内のonClick属性
    public void onButtonClick(View view){
        //  前の画面に戻る
        finish();
    }
}