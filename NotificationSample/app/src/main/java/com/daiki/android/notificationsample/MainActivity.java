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
            NotificationChannel channel = CreateChannel();

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
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("fromNotification", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);

            //  通知オブジェクトを生成
            Notification notification = builder.build();

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);

            //  通知を発火
            //  100 : NotificationのIDでアプリ内で一意になるようにする
            managerCompat.notify(100, notification);
        }
    }

    private NotificationChannel CreateChannel(){
        //  通知チャネル名
        String titleName = getString(R.string.notification_channel_name);

        //  通知チャネルの優先度
        //  DEFAULT : 標準
        int importance = NotificationManager.IMPORTANCE_HIGH;

        //  通知チャネルを生成
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,titleName,importance);

        //  バイブレーションするか
        channel.enableVibration(true);

        //  ロック画面で表示するか
        //  画面ロック中に通知が来たときだけ表示される?いまの処理では表示されない
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        return channel;
    }
}