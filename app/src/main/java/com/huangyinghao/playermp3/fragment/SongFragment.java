package com.huangyinghao.playermp3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.activity.PlayActivity;
import com.huangyinghao.playermp3.adapter.SongAdapter;
import com.huangyinghao.playermp3.broadcast.MusicBroadCast;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.utils.MusicList;
import com.huangyinghao.playermp3.utils.MyItemDecoration;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by deny on 2015/12/23.
 */
public class SongFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<MusicInfo> musics;
    private SongAdapter songAdapter;
    public android.os.Handler handler2 = new android.os.Handler(){

    };
    private MusicBroadCast musicBroadCast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_recycleview, container , false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);

        initData();
        setRecycleView();
        setAdapter();
        return view;
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        musicBroadCast = new MusicBroadCast(getContext());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if(musicBroadCast != null){
//            musicBroadCast.unregister();
//        }
//    }

    private void initData() {
        //从数据库中读取信息
        musics = DataSupport.findAll(MusicInfo.class);
        MusicList.getInstance().setMusicInfos(musics);

    }

    private void setRecycleView() {
        songAdapter = new SongAdapter(getContext() , musics);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL ,false);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new MyItemDecoration(getContext()));

        //点击事件
        songAdapter.setListener(new SongAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);

                MusicList.getInstance().setPos(pos);
                startActivity(intent);
                //发一个广播
                Intent intent1 = new Intent();
                intent1.setAction(MusicBroadCast.MY_ACTION);
                Bundle bundle = new Bundle();
                bundle.putInt("state", 0x10);
                intent1.putExtras(bundle);
                getContext().sendBroadcast(intent1);
            }
        });
    }

    private void setAdapter() {
        recyclerView.setAdapter(songAdapter);
    }


}
