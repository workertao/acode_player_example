package com.acode.player;

import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * user:yangtao
 * date:2018/4/261227
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class TimerUtils {
    private Timer timer;

    private TimerTask timerTask;
    //播放状态监听
    private AcodePlayerStateListener acodePlayerStateListener;

    private SimpleExoPlayer player;

    public TimerUtils(AcodePlayerStateListener acodePlayerStateListener, SimpleExoPlayer player) {
        this.acodePlayerStateListener = acodePlayerStateListener;
        this.player = player;
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
            if (acodePlayerStateListener == null) {
                return;
            }
            if (!player.getPlayWhenReady()) {
                stop();
                acodePlayerStateListener.playerStop();
                return;
            }
            int currentTime = (int) (player.getCurrentPosition() / 1000);
            int currentProgress = (int) (new BigDecimal((float) player.getCurrentPosition() / player.getDuration()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
            Log.d("post", "当前timer内存地址：" + timer.toString() + "    当前进度：" + currentProgress);
            if (currentProgress < 100) {
                acodePlayerStateListener.playerRuning(currentTime, currentProgress);
                return;
            }
            acodePlayerStateListener.playerStop();
            stop();
        }
    }
}
