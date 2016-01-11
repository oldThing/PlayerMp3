package com.huangyinghao.playermp3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.activity.SingerActivity;
import com.huangyinghao.playermp3.adapter.SingerAdapter;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.domain.SingerInfo;
import com.huangyinghao.playermp3.utils.MusicList;
import com.huangyinghao.playermp3.utils.MusicListBySinger;
import com.huangyinghao.playermp3.utils.MyItemDecoration;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by deny on 2015/12/23.
 */
public class SingerFragment extends Fragment{

    private List<SingerInfo> singerInfos;
    private RecyclerView recycleView;
    private SingerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_recycleview, container, false);
        recycleView = (RecyclerView) view.findViewById(R.id.recycle);

        initData();
        setRecycleView();
        initeAdapter();

        return view;
    }

    private void setRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL ,false);
        recycleView.setLayoutManager(manager);
        recycleView.addItemDecoration(new MyItemDecoration(getContext()));

        adapter = new SingerAdapter(getContext() , singerInfos);

        adapter.setListener(new SingerAdapter.OnClickListener() {
            @Override
            public void onClick(View view, long pos) {
                Intent intent = new Intent(getActivity() , SingerActivity.class);
                MusicList.getInstance().setAriId(pos);
                List<MusicInfo> musicInfos = DataSupport.where("artid = ?", String.valueOf(pos)).find(MusicInfo.class);
                MusicListBySinger.getInstance().setMusicInfos(musicInfos);
                MusicListBySinger.getInstance().setAriId(pos);
                startActivity(intent);
            }
        });

    }

    private void initeAdapter() {
        recycleView.setAdapter(adapter);

    }

    private void initData() {
        singerInfos = DataSupport.findAll(SingerInfo.class);
    }
}
