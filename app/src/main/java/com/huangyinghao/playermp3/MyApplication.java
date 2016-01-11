package com.huangyinghao.playermp3; /**
 * Created by deny on 2015/12/25.
 */

import android.content.Intent;
import android.os.Handler;

import com.huangyinghao.playermp3.broadcast.MusicBroadCast;

import org.litepal.LitePalApplication;
public class MyApplication extends LitePalApplication {

    Handler handler = new Handler();
    private MusicBroadCast musicBroadCast;


    @Override
    public void onCreate() {
        super.onCreate();
        musicBroadCast = new MusicBroadCast(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(musicBroadCast != null) {
            musicBroadCast.unregister();
        }

        Intent intent = new Intent("com.huangyinghao.playermp3.service.MusicService");
        stopService(intent);
    }
}
