package com.acode.player;

/**
 * user:yangtao
 * date:2018/4/261451
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public interface AcodePlayerStateListener {

    /**
     * 播放中
     * @param currentTime        当前播放时间
     * @param currentProgress    当前的进度条
     * @param secondProgressPer  当前的缓冲进度条
     */
    void playerRuning(int currentTime, int currentProgress,int secondProgressPer);

    //暂停
    void playerStop();
}
