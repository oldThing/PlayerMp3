package com.huangyinghao.playermp3.service;

import com.huangyinghao.playermp3.domain.MusicInfo;

/**
 * Created by deny on 2015/12/26.
 */
public interface MusicImp {
    void play();
    void pause();
    void continuePlay();
    void nextPlay();
    void previous();
    void seekTo(int process);
    MusicInfo getCurrentMusic();
    boolean isPlay();
}
