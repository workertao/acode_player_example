package com.acode.player.lib;

/**
 * user:yangtao
 * date:2018/4/201050
 * email:yangtao@bjxmail.com
 * introduce:
 */
public interface IBJXPlayerPresenter {
    //开始播放
    void onStartPlayer();

    //暂停播放
    void onPausePlayer();

    //快进or快退
    void seekTo();
}
