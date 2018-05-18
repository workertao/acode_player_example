package com.acode.player.utils;

import android.util.Log;

import com.acode.player.listener.AcodePlayerStateListener;
import com.acode.player.Config;
import com.acode.player.bean.PlayerBean;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * user:yangtao
 * date:2018/4/261227
 * email:yangtao@bjxmail.com
 * introduce:监听视频播放进度
 */
public class TimerUtils {
    private Timer timer;

    private TimerTask timerTask;
    //播放状态监听
    private AcodePlayerStateListener acodePlayerStateListener;

    private SimpleExoPlayer player;

    private PlayerBean playerBean;

    public TimerUtils(AcodePlayerStateListener acodePlayerStateListener, SimpleExoPlayer player, PlayerBean playerBean) {
        this.acodePlayerStateListener = acodePlayerStateListener;
        this.player = player;
        this.playerBean = playerBean;
    }


    public void start() {
        timer = new Timer(true);
        timerTask = new MyTimerTask();
        timer.schedule(timerTask, Config.firstStart, Config.secondStart);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }


    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.d("post","timerTask:"+timerTask+"     timer:"+timer);
            if (timerTask == null || timer==null){
                return;
            }
            if (acodePlayerStateListener == null) {
                return;
            }
            if (!player.getPlayWhenReady()) {
                stop();
                return;
            }
            playerBean.setCurrentTime(StringUtils.onFormatTime(player.getCurrentPosition()));
            playerBean.setCurrentPosition(player.getCurrentPosition());
            playerBean.setBufferedPercentage(player.getBufferedPercentage());
            playerBean.setDuration(player.getDuration());
            playerBean.setEndTime(StringUtils.onFormatTime(player.getDuration()));
            acodePlayerStateListener.playerRuning(playerBean);
        }
    }
}
