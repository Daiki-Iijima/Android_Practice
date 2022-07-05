package com.daiki.android.servicesample;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class SoundManageService extends Service {
    private MediaPlayer mPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.music_sample;

        Uri mediaUri = Uri.parse(mediaFileUriStr);

        mPlayer = new MediaPlayer();

        try{
            mPlayer.setDataSource(SoundManageService.this,mediaUri);
            //  読み込み完了イベント
            mPlayer.setOnPreparedListener(new OnMediaPreparedListener());
            //  再生終了イベント
            mPlayer.setOnCompletionListener(new OnMediaCompletionListener());
            //  メディアの準備開始(非同期)
            mPlayer.prepareAsync();

        }catch (IOException ex){
            Log.e("SoundManager","メディアプレイヤー準備時の例外発生",ex);
        }

        //  定数を返す
        //  ここで返す定数によってサービスの強制終了時の動作が変わる
        //  START_NOT_STICKY : 再起動しない(一番安全)
        return START_NOT_STICKY;
    }

    //  読み込み完了イベント
    private class OnMediaPreparedListener implements MediaPlayer.OnPreparedListener{
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            //  準備ができ次第再生開始
            mediaPlayer.start();
        }
    }

    //  メディアが最後まで再生されたときのイベント
    private class OnMediaCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //  自分自身のサービスを終了
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        if(mPlayer.isPlaying()){
            mPlayer.stop();
        }

        //  プレイヤーを解放
        mPlayer.release();
        mPlayer = null;
    }
}