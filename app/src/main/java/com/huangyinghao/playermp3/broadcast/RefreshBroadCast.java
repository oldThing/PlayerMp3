package com.huangyinghao.playermp3.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * Created by deny on 2016/1/5.
 */
public class RefreshBroadCast extends BroadcastReceiver {
    public static final String REFRESH_BRROAD =  "com.huangyinghao.playermp3.refreshbroadcast";

    private Context context;
    private Handler handler;
    //注册广播
    public RefreshBroadCast(Context context , Handler handler){
        this.handler = handler;
        this.context = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(REFRESH_BRROAD);

        context.registerReceiver(this , filter);

    }


    //注销广播
    public void unregister(){
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

//        Bundle bundle = intent.getExtras();
        Bundle bundle = intent.getExtras();
//        bundle.getInt("state" , 0);
//        int currentPosition = bundle.getInt("currentPosition", 0);
//        Log.i("tag" , currentPosition +"currentPosition");

        Message message = handler.obtainMessage();
        message.setData(bundle);

        handler.sendMessage(message);
    }
}
