package com.huangyinghao.playermp3.utils;

import android.content.Intent;

import com.huangyinghao.playermp3.domain.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deny on 2015/12/26.
 */
public class MusicList {

    private List<MusicInfo> musicInfos = new ArrayList<>();
    private static Intent mIntent;
    private static MusicList instance = new MusicList();
    private int pos;
    private long ariId;


    public static MusicList getInstance(){
        return instance;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public long getAriId() {
        return ariId;
    }

    public void setAriId(long ariId) {
        this.ariId = ariId;
    }

    private MusicList(){

    }

    public List<MusicInfo> getMusicInfos() {
        return musicInfos;
    }

    public void setMusicInfos(List<MusicInfo> musicInfos) {
        this.musicInfos = musicInfos;
    }

}
