package com.daiki.android.notificationsample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationUtil {

    //  一番優先度の高いチャネルを生成
    public static NotificationChannel CreateChannel(String channelID,String channelName){
        //  通知チャネルの優先度
        //  DEFAULT : 標準
        int importance = NotificationManager.IMPORTANCE_HIGH;

        //  通知チャネルを生成
        //  チャネルID,チャネル名,チャネル優先度
        NotificationChannel channel = new NotificationChannel(channelID,channelName,importance);

        //  バイブレーションするか
        channel.enableVibration(true);

        //  ロック画面で表示するか
        //  画面ロック中に通知が来たときだけ表示される?いまの処理では表示されない
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        return channel;
    }
}
