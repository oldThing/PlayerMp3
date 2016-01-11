package com.huangyinghao.playermp3.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.broadcast.MusicBroadCast;
import com.huangyinghao.playermp3.broadcast.RefreshBroadCast;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.service.MusicService;
import com.huangyinghao.playermp3.utils.CurrentMusic;
import com.huangyinghao.playermp3.utils.MusicList;
import com.huangyinghao.playermp3.utils.ToolUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by deny on 2015/12/25.
 */
public class PlayActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener, View.OnTouchListener {

    private ImageButton previous;
    private ImageButton next;
    private ImageView imageView;
    private ImageLoader loader;
    private DisplayImageOptions option;
    private MusicList instance;

    private static SeekBar bar;
    private static TextView time;
    private static TextView totalTime;
    private long albumId;
    private ImageButton playAndPause;
    public  static int flag = 1;   //默认状态是播放
    private static int progress;

    //创建一个用于识别收拾的GestureDetector对象waiyuwu.blogcn.com
    private GestureDetector detector = new GestureDetector(this);

//    public Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            Bundle bundle = msg.getData();
//            int duration = bundle.getInt("duration");
//            int currentPosition = bundle.getInt("currentPosition");
//            bar.setMax(duration);
//            bar.setProgress(currentPosition);
//
//            String durationTime = ToolUtils.toTime(duration);
//            String currentPositionTime = ToolUtils.toTime(currentPosition);
//            totalTime.setText(durationTime);
//            time.setText(currentPositionTime);
//
//            //进度条的点击事件
//            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                    progress = seekBar.getProgress();
//                    Intent intent = new Intent(MusicBroadCast.MY_ACTION);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("progress", progress);
//                    intent.putExtras(bundle);
//                    PlayActivity.this.sendBroadcast(intent);
//                }
//            });
//
//
//        }
//    };




    public Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            int state = msg.getData().getInt("state");
            Bundle bundle = msg.getData();
            int state = bundle.getInt("state", 0);
            Log.i("tag" , "state:" + state);
            MusicInfo musicInfo = CurrentMusic.instance().getMusicInfo();
            albumId = musicInfo.getAlbun_id();
            int duration = musicInfo.getDuration();
            loadPicture(albumId);
            playSong.setText(musicInfo.getName());
            playSinger.setText(musicInfo.getSinger());
            if(state == MusicService.NOTIFICATION_NEXT){
                playAndPause.setImageResource(R.mipmap.icon_pause_normal);
                flag = 1;
            } else if(state == MusicService.NOTIFICATION_PRIVIOUS){
                playAndPause.setImageResource(R.mipmap.icon_pause_normal);
                flag = 1;
            } else if(state == MusicService.NOTIFICATION_PAUSE){
                Log.i("tag" , "state" + state);
                playAndPause.setImageResource(R.mipmap.icon_play_normal);
                flag = 0;
            } else if(state == MusicService.NOTIFICATION_PLAY){
                playAndPause.setImageResource(R.mipmap.icon_pause_normal);
                flag = 1;
            }

//            int currentPosition = msg.getData().getInt("currentPosition");
//            Log.i("tag" , "currentPosition" + currentPosition );
            int currentPosition = bundle.getInt("currentPosition", 0);
            bar.setProgress(currentPosition);
            bar.setMax(duration);

            String durationTime = ToolUtils.toTime(duration);
            String currentPositionTime = ToolUtils.toTime(currentPosition);
            totalTime.setText(durationTime);
            time.setText(currentPositionTime);
        }
    };
    private MusicInfo musicInfo;
    private RefreshBroadCast refreshBroadCast;
    private SensorManager sensorManager;
    private TextView playSinger;
    private TextView playSong;
    private SwitchCompat switchCompat = null;
    private Toolbar playTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //隐藏系统状态栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.play_activity);

        instance = MusicList.getInstance();
        Intent serviceIntent = new Intent(this , MusicService.class);
        startService(serviceIntent);

        //设置状态栏渲染颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setTranslucentStatus(true);
        }


        initView();

    }

    public void setTranslucentStatus(boolean translucentStatus) {
        Window win = getWindow();
        WindowManager.LayoutParams wm = win.getAttributes();
        int bit = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;     //透明标志
        if(translucentStatus){
            wm.flags |= bit;
        } else {
            wm.flags &= ~bit;
        }
        win.setAttributes(wm);
    }

    private SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int state = 0;
            float xValue = event.values[0];

            //向左 上一首
            if(xValue > 12){
                state = MusicService.NOTIFICATION_PRIVIOUS;
                playAndPause.setImageResource(R.mipmap.icon_pause_normal);
            }
            //向右 下一首
            if(xValue < -12){
                state = MusicService.NOTIFICATION_NEXT;
                playAndPause.setImageResource(R.mipmap.icon_pause_normal);
            }

            Intent intent = new Intent();
            intent.setAction(MusicBroadCast.MY_ACTION);
            Bundle bundle = new Bundle();
            bundle.putInt("state" , state);
            intent.putExtras(bundle);
            sendBroadcast(intent);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播
        refreshBroadCast = new RefreshBroadCast(this , handler2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(refreshBroadCast != null){
            refreshBroadCast.unregister();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(MusicBroadCast.MY_ACTION);

        musicInfo = CurrentMusic.instance().getMusicInfo();
        int state = 0;

        switch (v.getId()){
            /**
             * 判断音乐是否播放，如果播放显示暂停图标，按下去暂停
             * 按钮 发送 广播
             */
            //发送广播
            case R.id.play_play_and_pause:
                //1 代表音乐播放 0 代表音乐暂停
                if(flag == 0){
                    state = MusicService.NOTIFICATION_PLAY;
                    playAndPause.setImageResource(R.mipmap.icon_pause_normal);
                    flag = 1;
                } else if(flag == 1){
                    state = MusicService.NOTIFICATION_PAUSE;
                    playAndPause.setImageResource(R.mipmap.icon_play_normal);
                    flag = 0;
                }
                break;
            case R.id.play_previous:
                state = MusicService.NOTIFICATION_PRIVIOUS;
                playAndPause.setImageResource(R.mipmap.icon_pause_normal);
                //把默认状态设为1
                flag = 1;
                break;
            case R.id.play_next:
                state = MusicService.NOTIFICATION_NEXT;
                playAndPause.setImageResource(R.mipmap.icon_pause_normal);
                //把默认状态设为1
                flag = 1;
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("state" , state);
//        bundle.putInt("process" , progress);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }


    /**
     * 初始化布局
     */
    private void initView() {

        loader = ImageLoader.getInstance();
        if(!loader.isInited()){
            loader.init(ImageLoaderConfiguration.createDefault(this));
        }
        option = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        bar = (SeekBar) findViewById(R.id.seek_bar);
        time = (TextView) findViewById(R.id.current_time);
        totalTime = (TextView) findViewById(R.id.total_time);


        //歌曲信息 歌手 歌曲
        playSinger = (TextView) findViewById(R.id.play_singer);
        playSong = (TextView) findViewById(R.id.play_song);

        previous = (ImageButton) findViewById(R.id.play_previous);
        next = (ImageButton) findViewById(R.id.play_next);
        playAndPause = (ImageButton) findViewById(R.id.play_play_and_pause);
        imageView = (ImageView) findViewById(R.id.image);

        imageView.setOnTouchListener(this);
        //下面两个要记得设哦，不然就没法处理轻触以外的事件了，例如抛掷动作。
        imageView.setLongClickable(true);
        detector.setIsLongpressEnabled(true);

        playAndPause.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);

        playTool = (Toolbar) findViewById(R.id.play_toolbar);
        playTool.setTitle("");

        setSupportActionBar(playTool);

        playTool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId()){
                    case android.R.id.home:
                        finish();
                        break;
                }
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        instance = MusicList.getInstance();
        albumId = instance.getMusicInfos().get(instance.getPos()).getAlbun_id();
//        albumId = MusicService.musicInfo.getAlbun_id();

        loadPicture(albumId);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
                Intent intent = new Intent(MusicBroadCast.MY_ACTION);
                Bundle bundle = new Bundle();
                bundle.putInt("state" , MusicService.NOTIFICATION_REFRESH);
                bundle.putInt("progress", progress);
                intent.putExtras(bundle);
                PlayActivity.this.sendBroadcast(intent);
            }
        });
    }

    /**
     * 加载图片
     */
    public void loadPicture(long albumId) {
        ToolUtils.loadPic(this , albumId , imageView);
    }



    //在按下动作时被调用
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int state = 0;
        Intent intent = new Intent();
        intent.setAction(MusicBroadCast.MY_ACTION);
        if(velocityX > 0){
            state = MusicService.NOTIFICATION_PRIVIOUS;
            playAndPause.setImageResource(R.mipmap.icon_pause_normal);
            flag = 1;
        } else if(velocityX < 0){
            state = MusicService.NOTIFICATION_NEXT;
            playAndPause.setImageResource(R.mipmap.icon_pause_normal);
            flag = 1;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("state" , state);
        bundle.putInt("process" , progress);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //增加switchCompat
        MenuItem switchItem = menu.findItem(R.id.menu_switch);
        MenuItemCompat.setActionView(switchItem, R.layout.switch_compat);
        switchCompat = (SwitchCompat) switchItem.getActionView().findViewById(R.id.siwthc_compat);
        //设置默认的勾选
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //加速度传感器 Sensor
                    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    Toast.makeText(PlayActivity.this, "结束摇动切歌", Toast.LENGTH_SHORT).show();
                    if(sensorManager != null){
                        sensorManager.unregisterListener(sensorListener);
                    }
                }
            }
        });
        return true;
    }
}
