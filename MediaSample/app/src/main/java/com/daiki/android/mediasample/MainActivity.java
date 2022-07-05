package com.daiki.android.mediasample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer m_Player;

    private final Handler handler = new Handler();
    private Runnable m_SbUpdate;

    //  UI
    Button m_BtPlay;
    Button m_BtBack;
    Button m_BtForward;
    SeekBar m_SbProgress;
    SwitchMaterial m_SwLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  UIの取得
        m_BtPlay = findViewById(R.id.btPlay);
        m_BtBack = findViewById(R.id.btBack);
        m_BtForward = findViewById(R.id.btForward);
        m_SbProgress = findViewById(R.id.sbProgress);
        m_SwLoop = findViewById(R.id.swLoop);

        m_Player = new MediaPlayer();

        m_SwLoop.setOnCheckedChangeListener(new LoopSwitchChangedListener());

        //  UIイベントの紐づけ
        m_BtPlay.setOnClickListener(new OnClickButtonListener());
        m_BtBack.setOnClickListener(new OnClickButtonListener());
        m_BtForward.setOnClickListener(new OnClickButtonListener());

        //  メディア再生の準備
        String mediaFilePath = "android.resource://" + getPackageName() + "/" + R.raw.music_sample2;
        Uri mediaFileUri = Uri.parse(mediaFilePath);
        try{
            m_Player.setDataSource(MainActivity.this,mediaFileUri);
            //  メディア再生の準備が完了した際のリスナを設定(非同期)
            m_Player.setOnPreparedListener(new PlayerPreparedListener());
            //  メディア再生が終了した際のリスナを設定
            m_Player.setOnCompletionListener(new PlayerCompletionListener());
            //  メディア再生を準備(非同期)
            m_Player.prepareAsync();
        }catch (IOException ex){
            Toast.makeText(
                    MainActivity.this,
                    "メディアプレイヤー準備時の例外発生",
                    Toast.LENGTH_SHORT).show();

            Log.e("MainActivity","メディアプレイヤー準備時の例外発生",ex);
        }
    }

    //  メディア再生の準備が完了した際のリスナ
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener{
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            //  準備が完了したので、各操作ボタンを有効にする
            m_BtPlay.setEnabled(true);
            m_BtBack.setEnabled(true);
            m_BtForward.setEnabled(true);

            //  シークバーの上限を設定
            m_SbProgress.setMax(m_Player.getDuration());
            //  シークバー更新ループ生成
            m_SbUpdate = new SeekBarUpdate();
            handler.post(m_SbUpdate);
        }
    }

    //  メディア再生が終了した際のリスナ
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if(m_Player.isLooping()) {
                //  ループする場合
            }else{  //  ループしない場合
                //  再生ボタンのラベルを再生に設定
                m_BtPlay.setText(R.string.bt_play_play);
            }
        }
    }

    //  各種ボタン処理
    private  class OnClickButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

            //  各ボタンによって処理を分岐
            if(id == R.id.btPlay){
                //  再生中の場合
                if(m_Player.isPlaying()){
                    m_Player.pause();
                    m_BtPlay.setText(R.string.bt_play_play);
                }else{  //  停止中の場合
                    m_Player.start();
                    m_BtPlay.setText(R.string.bt_play_pause);
                }
            }else if(id == R.id.btBack){
                m_Player.seekTo(0);
            }else if(id == R.id.btForward){
                m_Player.seekTo(m_Player.getDuration());
                //  停止中の場合は、最後までシークさせても、
                //  最後の一歩手前で止まってOnCompletionListenerが発火されないので、
                //  発火させるために、再生させる
                if(!m_Player.isPlaying()){
                    m_Player.start();
                }
            }
        }
    }

    private class SeekBarUpdate implements Runnable{
        @Override
        public void run() {
            //  シークバーの位置を更新
            m_SbProgress.setProgress(m_Player.getCurrentPosition());
            //  再帰処理によるループ
            handler.postDelayed(this,100);
        }
    }

    //  ループトグルボタンのOn/Off変化時のリスナー
    private class LoopSwitchChangedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            //  ループするかの切り替え
            m_Player.setLooping(isChecked);
        }
    }

    @Override
    protected void onDestroy() {

        //  再生中の場合、停止する
        if(m_Player.isPlaying()){
            m_Player.stop();
        }

        //  メディアプレイヤーの破棄
        m_Player.release();
        m_Player = null;

        //  プログレスバー更新処理の停止
        handler.removeCallbacks(m_SbUpdate);

        super.onDestroy();
    }
}