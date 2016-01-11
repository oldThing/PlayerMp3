package com.huangyinghao.playermp3.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.broadcast.MusicBroadCast;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.service.MusicService;

/**
 * Created by deny on 2015/12/30.
 */
public class NotificationUtil {

    public static void sendNotification(Context context , int notificationState , int notificationId , MusicInfo one){
        NotificationManager manager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        RemoteViews views = new RemoteViews(context.getPackageName() , R.layout.notification );


        //发送广播
        Intent intent = new Intent();
        intent.setAction(MusicBroadCast.MY_ACTION);

        if(notificationState == MusicService.NOTIFICATION_PLAY){
            //播放
            intent.putExtra("state", MusicService.NOTIFICATION_PAUSE);
//            views.setTextViewText(R.id.noti_play_and_pause, "暂停");
            views.setImageViewResource(R.id.noti_play_and_pause , R.mipmap.icon_pause_normal);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0x11, intent, 0);
            views.setOnClickPendingIntent(R.id.noti_play_and_pause , pendingIntent);
        } else if(notificationState == MusicService.NOTIFICATION_PAUSE){
            intent.putExtra("state", MusicService.NOTIFICATION_PLAY);
//            views.setTextViewText(R.id.noti_play_and_pause, "播放");
            views.setImageViewResource(R.id.noti_play_and_pause, R.mipmap.icon_play_normal);


            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0x12, intent, 0);
            views.setOnClickPendingIntent(R.id.noti_play_and_pause , pendingIntent1);
        }

        //上一首

        intent.putExtra("state" , MusicService.NOTIFICATION_PRIVIOUS);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0x13, intent, 0);
        views.setOnClickPendingIntent(R.id.noti_previous , pendingIntent2);

        //下一首
        intent.putExtra("state" , MusicService.NOTIFICATION_NEXT);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context , 0x14 , intent , 0);
        views.setOnClickPendingIntent(R.id.noti_next , pendingIntent3);

        //点进去播放


        intent.putExtra("state" , MusicService.NOTIFICATION_INTO_PLAY);

        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context , 0x15 , intent , 0);

        views.setOnClickPendingIntent(R.id.noti_image , pendingIntent4);


        //加载图片,歌手名，歌名
//        MusicInfo one = musicService.getCurrentMusic();

        Uri artistUri = Uri.parse("content://media/external/audio/albumart");
        ContentUris uris = new ContentUris();

        Uri uri = uris.withAppendedId(artistUri, one.getAlbun_id());

        views.setImageViewUri(R.id.noti_image, uri);

        views.setTextViewText(R.id.noti_text_song, one.getName());
        views.setTextViewText(R.id.noti_text_singer, one.getSinger());

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("音乐播放").setContent(views);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        manager.notify(notificationId, notification);

    }
}
