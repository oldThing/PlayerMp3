package com.huangyinghao.playermp3.service;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.huangyinghao.playermp3.broadcast.RefreshBroadCast;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.utils.CurrentMusic;
import com.huangyinghao.playermp3.utils.MusicList;
import com.huangyinghao.playermp3.utils.NotificationUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by deny on 2015/12/26.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    /**
     * 播放
     */
    public static final int NOTIFICATION_PLAY = 0x01;

    /**
     * 暂停
     */
    public static final int NOTIFICATION_PAUSE = 0x02;


    /**
     * 音乐通知的ID
     */
    public static final int MY_NOTIFICATION_ID = 0x03;


    /**
     * 上一首
     */
    public static final int NOTIFICATION_PRIVIOUS = 0x04;

    /**
     * 下一首
     */
    public static final int NOTIFICATION_NEXT = 0x05;

    /**
     * 刷新进度条
     */
    public static final int NOTIFICATION_REFRESH = 0x06;

    /**
     *
     */
    public static final int NOTIFICATION_INTO_PLAY = 0x07;

    private MediaPlayer mediaPlayer;
    private MusicInfo musicInfo;
    private MusicList instance;
    private Timer timer;
    public  int pos;
    private int currentPosition;
    private int state;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        instance = MusicList.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //把当前的音乐放进去
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            state = bundle.getInt("state");

            switch (state) {
                case 0x10:
                    play();

                    break;
                case NOTIFICATION_PLAY:
                    continuePlay();
                    break;

                case NOTIFICATION_PAUSE:
                    pause();
                    break;

                case NOTIFICATION_NEXT:
                    //把歌曲暂停
                    pause();
                    nextPlay();
                    break;

                case NOTIFICATION_PRIVIOUS:
                    //把歌曲暂停
                    pause();
                    previous();
                    break;

                case NOTIFICATION_INTO_PLAY:
                    if(mediaPlayer.isPlaying()) {
                        continuePlay();
                    } else {
                        pause();
                    }
                    break;

                case NOTIFICATION_REFRESH:
                    int progress = bundle.getInt("progress");
                    mediaPlayer.seekTo(progress);
                    break;
            }
        }

        CurrentMusic.instance().setMusicInfo(musicInfo);

        //发送广播
        Intent intent2 = new Intent();
        intent2.setAction(RefreshBroadCast.REFRESH_BRROAD);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("state" , state);
//        bundle2.putInt("currentPosition" , currentPosition);
        Log.i("tag", "state:+" + state);
        intent2.putExtras(bundle2);
        sendBroadcast(intent2);        //刷新页面的广播


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();   //先停止 ，再释放资源
            mediaPlayer.release();
        }
        if(timer != null){
            timer.cancel();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextPlay();
        NotificationUtil.sendNotification(MusicService.this, NOTIFICATION_PLAY,
                MY_NOTIFICATION_ID, musicInfo);
    }


    /**
     * 播放当前的一首歌
     */
    public void play(){
        pos = instance.getPos();
        musicInfo = instance.getMusicInfos().get(pos);
        Log.i("tag" , "musicInfo" + musicInfo);
        long song_id = musicInfo.getSong_id();
        play(song_id);
    }

    /**
     * 播放的方法
     * @param song_id
     */
    public void play(long song_id){
        //首先要清空数据源
        mediaPlayer.reset();
        ContentUris uris = new ContentUris();
        Uri uri = uris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song_id);
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    addTimer();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(this);
        NotificationUtil.sendNotification(MusicService.this, NOTIFICATION_PLAY,
                MY_NOTIFICATION_ID, musicInfo);
    }
    /**
     * 暂停
     */
    public void pause(){
        mediaPlayer.pause();
        NotificationUtil.sendNotification(MusicService.this, NOTIFICATION_PAUSE,
                MY_NOTIFICATION_ID, musicInfo);
        addTimer();
    }

    /**
     * 继续播放
     */

    public void continuePlay(){
        mediaPlayer.start();
        NotificationUtil.sendNotification(MusicService.this, NOTIFICATION_PLAY,
                MY_NOTIFICATION_ID, musicInfo);
        addTimer();
    }

    /**
     * 上一首
     */
    public void previous(){

        //判断该音乐是否处于第一首
        if(pos > 0 ){
            pos --;
            Log.i("tag" , pos +"---+");
//            MusicInfo musicInfo = instance.getMusicInfos().get(pos);
            musicInfo = instance.getMusicInfos().get(pos);
            long song_id = musicInfo.getSong_id();
            play(song_id);
        }
        if(pos == 0) {
                Toast.makeText(this, "前面已经没有歌曲了！", Toast.LENGTH_SHORT).show();
            pos = 0;
        }
    }
    /**
     * 下一首
     */
    public void nextPlay(){
        if(pos < instance.getMusicInfos().size() - 1){
            pos ++ ;
            musicInfo = instance.getMusicInfos().get(pos);

        long song_id = musicInfo.getSong_id();
            Log.i("tag" , "size" + instance.getMusicInfos().size() +"");
        play(song_id);
        } else {
            Toast.makeText(this , "后面已经没有歌曲了！" , Toast.LENGTH_SHORT).show();
            pos = instance.getMusicInfos().size() - 1;
        }
    }
    public void seekTo(int process) {
        mediaPlayer.seekTo(process);
    }

    /**
     * 添加计时器
     */
    public void addTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
//                    duration = mediaPlayer.getDuration();
                    currentPosition = mediaPlayer.getCurrentPosition();
                    //发送广播
                    Intent intent = new Intent(RefreshBroadCast.REFRESH_BRROAD);
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentPosition" , currentPosition);
                    intent.putExtras(bundle);
//                    Log.i("currentPosition" , currentPosition +"");
                    sendBroadcast(intent);

//                    Message message = PlayActivity.handler.obtainMessage();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("duration", duration);
//                    bundle.putInt("currentPosition", currentPosition);
//                    message.setData(bundle);

//                    PlayActivity.handler.sendMessage(message);
                }
            }, 500, 1000);
        }
    }




}
