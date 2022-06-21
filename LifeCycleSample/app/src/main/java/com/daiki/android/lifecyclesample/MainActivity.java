package com.daiki.android.lifecyclesample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("LifeCycleSample", "Main onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        Log.i("LifeCycleSample","Main onStart() called");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("LifeCycleSample","Main onRestart() called");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("LifeCycleSample","Main onResume() called");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("LifeCycleSample","Main onPause() called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("LifeCycleSample","Main onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("LifeCycleSample","Main onDestroy called");
        super.onDestroy();
    }

    //  次へボタンクリックイベント
    //  紐づけはxml内のonClick属性
    public void onButtonClick(View view){
        Intent intent = new Intent(MainActivity.this,SubActivity.class);

        startActivity(intent);
    }
}