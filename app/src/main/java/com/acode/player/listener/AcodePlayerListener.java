package com.acode.player.listener;

import com.acode.player.bean.PlayerBean;
import com.acode.player.utils.GestureEnum;

/**
 * user:yangtao
 * date:2018/5/181126
 * email:yangtao@bjxmail.com
 * introduce:播放器监听
 */
public interface AcodePlayerListener {
    /**
     * 加载中
     */
    void onLoading();

    /**
     * 准备播放
     */
    void onReady();

    /**
     * 播放中
     */
    void onPlayering(PlayerBean playerBean);

    /**
     * 播放结束
     */
    void onEnd();

    /**
     * 播放异常
     * 网络
     */
    void onCatch();

    /**
     * 播放错误
     */
    void onError();

    /**
     * 调节音量回调
     */
    void onVolumes(int maxVolume, int currentVolume);

    /**
     * 调节亮度回调
     */
    void onBrightness(int maxBrightness, int currentBrightness);

    /**
     * 滑动进度回调
     */
    void onProgress(long seekTimePosition, long duration, String seekTime, String totalTime);

    /**
     * 滑动结束回调
     */
    void oEndGesture(GestureEnum state);


}
