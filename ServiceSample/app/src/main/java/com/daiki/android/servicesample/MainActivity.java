package com.daiki.android.servicesample;

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

        Button btPlay= findViewById(R.id.btPlay);
        btPlay.setOnClickListener(new OnButtonClickListener());

        Button btStop= findViewById(R.id.btStop);
        btStop.setOnClickListener(new OnButtonClickListener());
    }

    public class OnButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

            Intent intent = new Intent(MainActivity.this,SoundManageService.class);

            Button btPlay = findViewById(R.id.btPlay);
            Button btStop = findViewById(R.id.btStop);

            if(id == R.id.btPlay){
                //  サービスを開始
                startService(intent);
                btPlay.setEnabled(false);
                btStop.setEnabled(true);
            }else if(id == R.id.btStop){
                //  サービスを停止
                stopService(intent);
                btStop.setEnabled(false);
                btPlay.setEnabled(true);
            }
        }
    }
}