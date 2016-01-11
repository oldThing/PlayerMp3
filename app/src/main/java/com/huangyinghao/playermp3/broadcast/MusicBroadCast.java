package com.huangyinghao.playermp3.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.huangyinghao.playermp3.service.MusicService;

/**
 * Created by deny on 2015/12/30.
 */
public class MusicBroadCast extends BroadcastReceiver {

    public static final String MY_ACTION = "com.huangyinghao.playermp3.mybroadcast";

    private Context mContext;
//    private Handler mHandler;




    public MusicBroadCast(Context context  ){
        mContext = context;
//        mHandler = handler;
        IntentFilter filter = new IntentFilter();
        filter.addAction(MY_ACTION);
        context.registerReceiver(this, filter);

    }

    /**
     * 注销广播
     */
    public void unregister(){
        mContext.unregisterReceiver(this);
    }


    /**
     * 接收到的数据，通过handler传递消息给播放界面来控制音乐的播放动态
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //播放音乐的操作
        Intent serviceIntent = new Intent(context , MusicService.class);
        Bundle bundle = intent.getExtras();
        serviceIntent.putExtras(bundle);

        context.startService(serviceIntent);

//        CurrentMusic instance = CurrentMusic.instance();
//        long albunId = instance.getMusicInfo().getAlbun_id();

//        Bundle bundle2 = new Bundle();
//        bundle2.putInt("state" , 0);



        //刷新页面的操作
////        Message message = mHandler.obtainMessage();
//        message.setData(bundle2);
//        Log.i("tag", "-------------------------");
////        message.obj = intent.getIntExtra("state", 0);
//        mHandler.sendMessage(message);

    }
}
