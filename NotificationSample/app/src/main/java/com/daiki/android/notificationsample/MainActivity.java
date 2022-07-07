package com.daiki.android.notificationsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "test_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        boolean fromNotification = getIntent.getBooleanExtra("fromNotification",false);
        if(fromNotification){
            Log.i("MainActivity","通知から来ました");
        }else {

            //  チェネルを作成
            NotificationChannel channel = NotificationUtil.CreateChannel(
                    CHANNEL_ID,
                    getString(R.string.notification_channel_name)
            );

            //  システムから通知マネージャーを取得
            NotificationManager manager = getSystemService(NotificationManager.class);

            //  通知マネージャーに作成したチャネルを登録
            manager.createNotificationChannel(channel);

            //  通知ビルダーを生成
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID);

            //  通知エリアに表示されるアイコンを設定
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);

            //  通知ドロワーで表示されるタイトルを設定
            builder.setContentTitle(getString(R.string.notification_title));

            //  通知ドロワーで表示されるメッセージを設定
            builder.setContentText(getString(R.string.notification_text));

            //  通知タップ時の処理
            //  PendingIntent : 通知がタップされたら起動するActivity
            //Intent intent = new Intent(MainActivity.this, MainActivity.class);
            //intent.putExtra("fromNotification", true);
            //PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            //builder.setContentIntent(pendingIntent);

            //  通知オブジェクトを生成
            Notification notification = builder.build();

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);

            //  通知を発火
            //  100 : NotificationのIDでアプリ内で一意になるようにする
            managerCompat.notify(1000, notification);

            //  サービスを開始
            Intent serviceIntent = new Intent(MainActivity.this,TestService.class);
            startService(serviceIntent);

        }
    }
}