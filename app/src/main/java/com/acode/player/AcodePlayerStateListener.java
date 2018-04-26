package com.acode.player;

/**
 * user:yangtao
 * date:2018/4/261451
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public interface AcodePlayerStateListener {

    //播放中
    void playerRuning(int currentTime, int currentProgress);

    //暂停
    void playerStop();
}
