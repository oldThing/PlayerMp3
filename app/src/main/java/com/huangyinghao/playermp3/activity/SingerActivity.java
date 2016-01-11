package com.huangyinghao.playermp3.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.adapter.SongAdapter;
import com.huangyinghao.playermp3.broadcast.MusicBroadCast;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.utils.MusicList;
import com.huangyinghao.playermp3.utils.MusicListBySinger;
import com.huangyinghao.playermp3.utils.ToolUtils;

import java.util.List;


/**
 * Created by deny on 2016/1/3.
 */
public class SingerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView singerImg;
    private long albun_id;
    private RecyclerView singerRecycler;
    private List<MusicInfo> musicInfos;
    private SongAdapter songAdapter;
    private FloatingActionButton fab;

    private Handler handler = new Handler();
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singer_activity);
        //设置状态栏渲染颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setTranslucentStatus(true);
        }


        initData();
        initView();
        initEvent();
        setRecyclerView();
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        musicBroadCast = new MusicBroadCast(this );
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        musicBroadCast.unregister();
//    }

    private void setRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        singerRecycler.setLayoutManager(manager);
        singerRecycler.setAdapter(songAdapter);

    }

    private void initData() {
        long artId = MusicListBySinger.getInstance().getAriId();
        musicInfos = MusicInfo.where("artid = ?",
                String.valueOf(artId)).find(MusicInfo.class);

        albun_id = musicInfos.get(0).getAlbun_id();

    }

    private void initView() {
        singerImg = (ImageView) findViewById(R.id.singer_activity_img);
        ToolUtils.loadPic(this, albun_id, singerImg);
        singerRecycler = (RecyclerView) findViewById(R.id.singer_recycler);
        songAdapter = new SongAdapter(this , musicInfos);
//        fab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.singer_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.singer_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(musicInfos.get(0).getSinger());
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);     //收放后的数据
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);     //为收放后的数据

    }

    private void initEvent() {
//        fab.setOnClickListener(this);

        songAdapter.setListener(new SongAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int pos) {
                MusicList instance = MusicList.getInstance();
                List<MusicInfo> musicInfos = instance.getMusicInfos();
                MusicListBySinger instance2 = MusicListBySinger.getInstance();
                Log.i("tag" , musicInfos.get(0).getName());
                int temp = 0;
                for(int i = 0 ; i < musicInfos.size() ; i++){
                    if(musicInfos.get(i).getSong_id() == instance2.getMusicInfos().get(pos).getSong_id()){
                            temp = i;
                    }
                }
///                Log.i("tag" , "pos:" + temp );
                instance.setPos(temp);
                Intent intent = new Intent(SingerActivity.this , PlayActivity.class);
                startActivity(intent);

                //发一个广播
                Intent intent1 = new Intent();
                intent1.setAction(MusicBroadCast.MY_ACTION);
                Bundle bundle = new Bundle();
                bundle.putInt("state" , 0x10);
                intent1.putExtras(bundle);
                sendBroadcast(intent1);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
