package com.huangyinghao.playermp3.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.huangyinghao.playermp3.broadcast.MusicBroadCast;
import com.huangyinghao.playermp3.service.MusicService;

/**
 * Created by deny on 2016/1/7.
 */
public class MobliePhoneStateListener extends PhoneStateListener {
    private Context context;

    public MobliePhoneStateListener(Context context){
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch(state){
//            case TelephonyManager.CALL_STATE_IDLE:  //挂机状态
//                Intent intent = new Intent(MusicBroadCast.MY_ACTION);
//                Bundle bundle = new Bundle();
//                bundle.putInt("state", MusicService.NOTIFICATION_PLAY);
//                intent.putExtras(bundle);
//                context.sendBroadcast(intent);
//                break;
            case  TelephonyManager.CALL_STATE_OFFHOOK:   //通话状态
            case TelephonyManager.CALL_STATE_RINGING:   //响铃状态

                Intent intent2 = new Intent(MusicBroadCast.MY_ACTION);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("state", MusicService.NOTIFICATION_PAUSE);
                intent2.putExtras(bundle2);
                context.sendBroadcast(intent2);
                break;
        }
    }
}
