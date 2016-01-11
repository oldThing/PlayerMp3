package com.huangyinghao.playermp3;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyinghao.playermp3.activity.PlayActivity;
import com.huangyinghao.playermp3.adapter.ViewPagerAdapter;
import com.huangyinghao.playermp3.broadcast.MusicBroadCast;
import com.huangyinghao.playermp3.broadcast.RefreshBroadCast;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.fragment.AlbumFragement;
import com.huangyinghao.playermp3.fragment.SingerFragment;
import com.huangyinghao.playermp3.fragment.SongFragment;
import com.huangyinghao.playermp3.service.MusicService;
import com.huangyinghao.playermp3.ui.cursorTab;
import com.huangyinghao.playermp3.utils.CubeTransformer;
import com.huangyinghao.playermp3.utils.CurrentMusic;
import com.huangyinghao.playermp3.utils.MobliePhoneStateListener;
import com.huangyinghao.playermp3.utils.MusicLoadUtils;
import com.huangyinghao.playermp3.utils.SingerLoadUtils;
import com.huangyinghao.playermp3.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private List<Fragment> mFragments;
    private ViewPager mViewPager;
    private cursorTab songTag;
    private cursorTab singerTag;
    private cursorTab albumTag;
    private List<cursorTab> mCursorTabs;
    private ImageButton buttonNext;
    private RefreshBroadCast refreshBroadCast;

    private static int flag = 1;   // 1 为播放  0 为暂停


    //刷新页面
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mainPlayBar.setVisibility(View.VISIBLE);
                int state = msg.getData().getInt("state");

                MusicInfo musicInfo = CurrentMusic.instance().getMusicInfo();
                albunId = musicInfo.getAlbun_id();
                ToolUtils.loadPic(MainActivity.this, albunId, imageView);

                if (state == MusicService.NOTIFICATION_NEXT) {
                    flag = 1;
                    playAndPauseButton.setImageResource(R.mipmap.icon_pause_normal);

                } else if (state == MusicService.NOTIFICATION_NEXT) {
                    flag = 1;
                    playAndPauseButton.setImageResource(R.mipmap.icon_pause_normal);
                } else if (state == MusicService.NOTIFICATION_PAUSE){
                    playAndPauseButton.setImageResource(R.mipmap.icon_play_normal);
                    flag = 0;
                } else if (state == MusicService.NOTIFICATION_PLAY){
                    playAndPauseButton.setImageResource(R.mipmap.icon_pause_normal);
                    flag = 1;
                }
                singerText.setText(musicInfo.getSinger());
                songText.setText(musicInfo.getName());
        }
    };
    private ImageView imageView;
    private long albunId;
    private ImageButton playAndPauseButton;
    private TextView songText;
    private TextView singerText;
    private Toolbar toolbar;
    private RelativeLayout mainPlayBar;
    private TelephonyManager telManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏渲染颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setTranslucentStatus(true);
        }

        //来电监听
        telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(new MobliePhoneStateListener(this) , PhoneStateListener.LISTEN_CALL_STATE);


        initView();
        initData();
        initEvent();
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


    /**
     * 初始化布局
     */
    private void initView() {

        mFragments = new ArrayList<>();
        mCursorTabs = new ArrayList<>();

        mViewPager = (ViewPager) findViewById(R.id.main_viewPager);

        songTag = (cursorTab) findViewById(R.id.tab_song);
        singerTag = (cursorTab) findViewById(R.id.tab_singer);
        albumTag = (cursorTab) findViewById(R.id.tab_album);

        mCursorTabs.add(songTag);
        mCursorTabs.add(singerTag);
        mCursorTabs.add(albumTag);


        buttonNext = (ImageButton) findViewById(R.id.main_next);
//        buttonPause = (ImageButton) findViewById(R.id.main_pause);
//        buttonPlay = (ImageButton) findViewById(R.id.main_play);

        imageView = (ImageView) findViewById(R.id.main_icon);


        playAndPauseButton = (ImageButton) findViewById(R.id.main_play_and_pause);
//        MusicList instance = MusicList.getInstance();
//        albunId = instance.getMusicInfos().get(instance.getPos()).getAlbun_id();
//        ToolUtils.loadPic(this , albunId , imageView);
        singerText = (TextView) findViewById(R.id.main_singer);
        songText = (TextView) findViewById(R.id.main_song);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("我的音乐");
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        musicBroadCast = new MusicBroadCast(this);
        refreshBroadCast = new RefreshBroadCast(MainActivity.this , handler);

    }
//



//    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != refreshBroadCast) {
//            musicBroadCast.unregister();
            refreshBroadCast.unregister();
        }
    }

    /**
     * 初始化fragment,设置viewpager的adapter
     */
    private void initData() {
        AlbumFragement alubmFragement = new AlbumFragement();
        SingerFragment singerFragment = new SingerFragment();
        SongFragment songFragment = new SongFragment();
        mFragments.add(songFragment);
        mFragments.add(singerFragment);
        mFragments.add(alubmFragement);
        ViewPagerAdapter viewpagerAdapter = new ViewPagerAdapter(getSupportFragmentManager() ,
                this , mFragments);
        mViewPager.setAdapter(viewpagerAdapter);

//        //扫描歌曲信息
        ContentResolver contentResolver = getContentResolver();
        SingerLoadUtils.INSTANCE(contentResolver);
        MusicLoadUtils.Instance(contentResolver);



    }

    private void initEvent() {
        songTag.setOnClickListener(this);
        singerTag.setOnClickListener(this);
        albumTag.setOnClickListener(this);
        mCursorTabs.get(0).setIconAlpha(1.0f);
        mViewPager.addOnPageChangeListener(this);

        mViewPager.setPageTransformer(true, new CubeTransformer());


        buttonNext.setOnClickListener(this);
//        buttonPause.setOnClickListener(this);
//        buttonPlay.setOnClickListener(this);
        playAndPauseButton.setOnClickListener(this);
        imageView.setOnClickListener(this);


        mainPlayBar = (RelativeLayout) findViewById(R.id.main_play_bar);

    }



    /**
     * tag的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        setOtherColor();
        int state = 0;
        int tabState = 0;
        switch(v.getId()){
            case R.id.tab_song:
                mCursorTabs.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                tabState = 1;
                break;
            case R.id.tab_singer:
                mCursorTabs.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                tabState = 1;
                break;
            case R.id.tab_album:
                mCursorTabs.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                tabState = 1;
                break;
            case R.id.main_next:
                state = MusicService.NOTIFICATION_NEXT;
                playAndPauseButton.setImageResource(R.mipmap.icon_pause_normal);
                flag = 1;
                break;
            case R.id.main_play_and_pause:
                if(1 == flag){
                    state = MusicService.NOTIFICATION_PAUSE;
                    playAndPauseButton.setImageResource(R.mipmap.icon_pause_normal);
                    flag = 0;
                }  else if ( 0 == flag){
                    state = MusicService.NOTIFICATION_PLAY;
                    playAndPauseButton.setImageResource(R.mipmap.icon_play_normal);
                    flag = 1;
                }
                break;
            case R.id.main_icon:
                state = MusicService.NOTIFICATION_INTO_PLAY;
                Intent intent1 = new Intent(MainActivity.this , PlayActivity.class);
                startActivity(intent1);
                break;

        }

        //如果是tab的按钮就不需要发送广播。
        if(tabState == 1){
            return;
        }

        //发送 广播
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        Intent intent = new Intent();
        intent.setAction(MusicBroadCast.MY_ACTION);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    /**
     * 把其他的设为原始状态
     */
    private void setOtherColor() {
        int num = mCursorTabs.size();
        for(int i = 0 ; i < num ; i++){
            mCursorTabs.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(positionOffset > 0){
            cursorTab left = mCursorTabs.get(position);
            cursorTab right = mCursorTabs.get(position + 1);
            left.setIconAlpha(1-positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



}
