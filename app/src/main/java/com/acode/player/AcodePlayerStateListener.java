package com.acode.player;

import com.acode.player.bean.PlayerBean;

/**
 * user:yangtao
 * date:2018/4/261451
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public interface AcodePlayerStateListener {

    /**
     * 播放中
     *
     * @param playerBean 当前播放视频实体
     */
    void playerRuning(PlayerBean playerBean);
}
