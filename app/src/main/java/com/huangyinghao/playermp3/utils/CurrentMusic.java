package com.huangyinghao.playermp3.utils;

import com.huangyinghao.playermp3.domain.MusicInfo;

/**
 * Created by deny on 2016/1/5.
 */
public class CurrentMusic {
    private static CurrentMusic mCurrentMusic = new CurrentMusic();
    private MusicInfo musicInfo;

    private CurrentMusic(){
    }

    public static CurrentMusic instance(){
        return mCurrentMusic;
    }


    public MusicInfo getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(MusicInfo musicInfo) {
        this.musicInfo = musicInfo;
    }

}
