package com.daiki.android.notificationsample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestService extends Service {

    private static final String CHANNEL_ID = "service_notification_channel";

    private Loop mLoop;

    private Handler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //  チャネルを生成
        NotificationChannel channel = NotificationUtil.CreateChannel(
                CHANNEL_ID,
                getString(R.string.notification_service_channel_name)
        );

        //  システムから通知マネージャーを取得
        NotificationManager manager = getSystemService(NotificationManager.class);

        //  チャネルをシステム登録
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                TestService.this,
                CHANNEL_ID
        );

        builder.setContentTitle("サービスからの通知");
        builder.setContentText("この通知はサービスから発行しています");
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);

        //  メインスレッドを取得しておく
        Looper mainLopper = Looper.getMainLooper();
        mHandler = HandlerCompat.createAsync(mainLopper);

        //  非同期処理開始
        mLoop = new Loop(builder.build(),5000);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(mLoop);

        //  定数を返す
        //  ここで返す定数によってサービスの強制終了時の動作が変わる
        //  START_NOT_STICKY : 再起動しない(一番安全)
        return START_NOT_STICKY;
    }

    //  ループして通知を発行する
    private class Loop implements Runnable{

        private Notification mNotification;
        private long mMillisLoopTime;

        private boolean IsLoop = true;

        public Loop(Notification notification,long millisLoopTime){
            mNotification = notification;
            mMillisLoopTime = millisLoopTime;
        }

        @WorkerThread
        @Override
        public void run() {
            int id = 0;
            while(IsLoop) {
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(TestService.this);
                managerCompat.notify(id, mNotification);

                try {
                    Thread.sleep(mMillisLoopTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                id++;
            }
        }

        public void stop(){
            IsLoop = false;
        }
    }

    @Override
    public void onDestroy() {
        mLoop.stop();
    }
}