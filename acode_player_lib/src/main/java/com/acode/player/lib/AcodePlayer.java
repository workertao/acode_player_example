package com.acode.player.lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.acode.player.lib.bean.PlayerBean;
import com.acode.player.lib.data.Config;
import com.acode.player.lib.listener.AcodePlayerListener;
import com.acode.player.lib.listener.AcodePlayerStateListener;
import com.acode.player.lib.utils.GestureEnum;
import com.acode.player.lib.utils.StringUtils;
import com.acode.player.lib.utils.TimerUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * user:yangtao
 * date:2018/5/181051
 * email:yangtao@bjxmail.com
 * introduce:播放器
 */
public class AcodePlayer implements AcodePlayerStateListener, View.OnTouchListener {
    //播放器
    private SimpleExoPlayer player;
    //上下文
    private Context context;
    //播放器渲染view
    private SurfaceView surfaceView;
    //播放器监听
    private AcodePlayerListener acodePlayerListener;
    //定时监听播放状态
    private TimerUtils timerUtils;
    //播放器的视频实体
    private PlayerBean playerBean;
    //记录是不是页面切换
    private boolean isSucfare;
    //手势监听
    private GestureDetector gestureDetector;
    //屏幕宽度
    private int screeWidth;
    //系统音量控制器
    private AudioManager audiomanager;
    //音量的最大值
    private int maxVolume;
    //当前音量
    private int currentVolume = -1;
    //亮度值
    private float currentBrightness = -1;

    //新的播放进度
    private long newPosition = -1;

    public AcodePlayer(Context context, SurfaceView surfaceView, AcodePlayerListener acodePlayerListener) {
        this.context = context;
        this.surfaceView = surfaceView;
        this.acodePlayerListener = acodePlayerListener;
        initData();
    }

    //初始化数据
    private void initData() {
        gestureDetector = new GestureDetector(context, new PlayerGestureListener());
        surfaceView.setOnTouchListener(this);
        screeWidth = context.getResources().getDisplayMetrics().heightPixels;
        audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    //播放器实例
    public SimpleExoPlayer getPlayer() {
        return player;
    }

    //播放总长度
    public long getDuration() {
        if (player == null) {
            return 0;
        }
        return player.getDuration();
    }

    //当前的播放位置
    public long getCurrentPosition() {
        if (player == null) {
            return 0;
        }
        return player.getCurrentPosition();
    }

    //当前的缓冲位置
    public int getBufferedPercentage() {
        if (player == null) {
            return 0;
        }
        return player.getBufferedPercentage();
    }

    public void seekTo(long position) {
        player.seekTo(position);
    }

    /**
     * 当前是否播放
     *
     * @return
     */
    public boolean getPlayWhenReady() {
        if (player == null) {
            return false;
        }
        return player.getPlayWhenReady();
    }

    //创建播放器
    public void createPlayer() {
        // step1. 创建一个默认的TrackSelector
        Handler mainHandler = new Handler();

        // 创建带宽
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        // 创建轨道选择工厂
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        // 创建轨道选择器实例
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        //step2. 创建播放器
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        //设置播放的view
        player.setVideoSurfaceView(surfaceView);

    }

    /**
     * 准备播放
     *
     * @param playerBean 播放数据源
     *                   默认播放标清
     */
    public void readyPlayer(PlayerBean playerBean) {
        readyPlayer(playerBean, Config.STARTMD_CLEAR);

    }

    /**
     * 准备播放
     *
     * @param playerBean 播放数据源
     *                   设置播放线路
     */
    public void readyPlayer(PlayerBean playerBean, String lins) {
        this.playerBean = playerBean;
        if (timerUtils != null) {
            timerUtils.stop();
            timerUtils = null;
        }
        //创建定时间监听播放状态
        timerUtils = new TimerUtils(this, player, playerBean);
        Uri uri = null;
        for (int i = 0; i < playerBean.getPlayerUrls().size(); i++) {
            if (lins.equals(playerBean.getLineNames().get(i))) {
                uri = Uri.parse(playerBean.getPlayerUrls().get(i));
            }
        }
        //初始化数据源
        MediaSource mediaSource = initMediaSource(uri);
        // 添加数据源
        player.prepare(mediaSource);

        // 设置播放进度
        player.seekTo(playerBean.getCurrentPosition());

        // 播放监听
        player.addListener(eventListener);
    }


    //初始化数据源
    private MediaSource initMediaSource(Uri uri) {
        // 测量播放带宽，如果不需要可以传null
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        // 创建加载数据的工厂
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getPackageName()), bandwidthMeter);

        // 创建解析数据的工厂
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // 传入Uri、加载数据的工厂、解析数据的工厂，就能创建出MediaSource
        MediaSource videoSource = new ExtractorMediaSource(uri,
                dataSourceFactory, extractorsFactory, null, null);
        return videoSource;
    }

    //播放监听
    ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            Log.d("post", "播放: onTimelineChanged 周期总数 " + timeline);
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.d("post", "播放: TrackGroupArray ");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.d("post", "播放: onLoadingChanged " + isLoading);
        }

        /**
         * 视频的播放状态
         * STATE_IDLE 播放器空闲，既不在准备也不在播放
         * STATE_PREPARING 播放器正在准备
         * STATE_BUFFERING 播放器已经准备完毕，但无法立即播放。此状态的原因有很多，但常见的是播放器需要缓冲更多数据才能开始播放
         * STATE_ENDED 播放已完毕
         */
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (acodePlayerListener == null) {
                return;
            }
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    acodePlayerListener.onLoading();
                    break;
                case Player.STATE_ENDED:
                    timerUtils.stop();
                    player.setPlayWhenReady(false);
                    player.seekTo(0);
                    playerBean.setCurrentPosition(0);
                    playerBean.setCurrentTime("00:00");
                    acodePlayerListener.onEnd(playerBean);
                    break;
                case Player.STATE_IDLE:
                    acodePlayerListener.onCatch();
                    player.setPlayWhenReady(false);
                    break;
                case Player.STATE_READY:
                    acodePlayerListener.onReady();
                    break;

            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.d("post", "播放状态: onRepeatModeChanged");
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            Log.d("post", "播放状态: onShuffleModeEnabledChanged");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.d("post", "播放: onPlayerError  ");
            if (timerUtils != null) {
                timerUtils.stop();
            }
            if (player != null) {
                player.setPlayWhenReady(false);
            }
            if (acodePlayerListener != null) {
                acodePlayerListener.onError();
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.d("post", "播放: onPositionDiscontinuity  " + reason);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.d("post", "播放状态: onPlaybackParametersChanged");
        }

        @Override
        public void onSeekProcessed() {
            Log.d("post", "播放状态: onSeekProcessed");
        }
    };

    /**
     * 播放中
     *
     * @param playerBean 当前播放视频实体
     */
    @Override
    public void playerRuning(PlayerBean playerBean) {
        if (acodePlayerListener == null) {
            return;
        }
        acodePlayerListener.onPlayering(playerBean);
    }

    //开始播放
    public void startPlayer() {
        if (player == null) {
            return;
        }
        if (playerBean == null) {
            return;
        }
        //开始监听
        timerUtils.start();
        player.setPlayWhenReady(true);
    }

    //暂停播放
    public void pausePlayer() {
        if (player == null) {
            return;
        }
        if (timerUtils == null) {
            return;
        }
        //终止监听
        timerUtils.stop();
        player.setPlayWhenReady(false);
    }

    //释放资源
    public void cancel() {
        if (player == null) {
            return;
        }
        if (timerUtils == null) {
            return;
        }
        timerUtils.stop();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }

    /**
     * 再次回到此页面调用此方法
     */
    public void onResume() {
        if (!isSucfare) {
            return;
        }
        if (playerBean == null) {
            return;
        }
        if (player != null) {
            cancel();
        }
        isSucfare = false;
        createPlayer();
        readyPlayer(playerBean);
        player.seekTo(playerBean.getCurrentPosition());
        player.setPlayWhenReady(false);
    }


    /**
     * 页面切换调用此方法
     */
    public void onPause() {
        isSucfare = true;
        cancel();
    }

    /**
     * 网络找不到
     */
    public void onNetworkNofound() {
        cancel();
    }

    /**
     * 网络重新连接
     */
    public void onNetworkClient() {
        if (playerBean == null) {
            return;
        }
        if (player != null) {
            cancel();
        }
        createPlayer();
        readyPlayer(playerBean);
        player.seekTo(playerBean.getCurrentPosition());
        player.setPlayWhenReady(false);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //将触摸事件交给GestureDetector来处理
        //竖屏下不执行手势操作
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (isPortrait) {
            return false;
        }
        //如果设置手势监听，则交给gestureDetector去处理
        if (gestureDetector != null && gestureDetector.onTouchEvent(event)) {
            return true;
        }
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
            default:
        }
        return false;
    }

    //手势监听
    private class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean toSeek;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("post", "onDoubleTap");
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("post", "onDown");
            firstTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //e1,之前DOWN事件
            //e2当前的MOVE事件
            //distanceX当前MOVE事件
            //distanceY上一个MOVE事件的位移量
            if (firstTouch) {
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                firstTouch = false;
            }
            //获取本次X轴的偏移量
            float differX = e1.getX() - e2.getX();
            //区分是不是滑动调节进度
            if (toSeek) {
                differX = -differX;
                long position = player.getCurrentPosition();
                long duration = player.getDuration();
                long newPosition = (int) (position + differX * duration / surfaceView.getWidth());
                if (newPosition > duration) {
                    newPosition = duration;
                } else if (newPosition <= 0) {
                    newPosition = 0;
                }
                showProgressDialog(newPosition, duration, StringUtils.onFormatTime(newPosition), StringUtils.onFormatTime(duration));
            } else {
                //区分是调节音量还是调节亮度  左侧亮度 右侧声音
                int branch = (int) (screeWidth * 0.5f);
                //滑动的Y坐标 距离
                float differY = e1.getY() - e2.getY();
                //计算本次的比例
                float percent = differY / surfaceView.getHeight();
                if (e1.getX() > branch) {
                    Log.d("post", "右---y轴动向：" + e1.getY() + "   " + e2.getY() + "    " + distanceY);
                    showVolumeDialog(percent);
                } else {
                    Log.d("post", "左---y轴动向：" + e1.getY() + "   " + e2.getY() + "     " + distanceY);
                    showBrightnessDialog(percent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /****
     * 滑动进度
     *
     * @param  seekTimePosition  滑动的时间
     * @param  duration         视频总长
     * @param  seekTime    滑动的时间 格式化00:00
     * @param  totalTime    视频总长 格式化00:00
     **/
    private synchronized void showProgressDialog(long seekTimePosition, long duration, String seekTime, String totalTime) {
        newPosition = seekTimePosition;
        if (acodePlayerListener == null) {
            return;
        }
        Log.d("post", "seekTimePosition:" + seekTimePosition + "  duration:" + duration + "    seekTime:" + seekTime + "    totalTime:" + totalTime);
        acodePlayerListener.onProgress(seekTimePosition, duration, seekTime, totalTime);
    }

    /**
     * 滑动改变声音大小
     * synchronized 避免用户在滑动进度的时候触发音量的回调
     *
     * @param percent percent 滑动
     */
    private synchronized void showVolumeDialog(float percent) {
        if (currentVolume == -1) {
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume < 0) {
                currentVolume = 0;
            }
        }
        int index = (int) (percent * maxVolume) + currentVolume;
        if (index > maxVolume) {
            index = maxVolume;
        } else if (index < 0) {
            index = 0;
        }
        Log.d("post", "音量：" + index);
        audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        if (acodePlayerListener == null) {
            return;
        }
        acodePlayerListener.onVolumes(maxVolume, index);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent 值大小
     */
    private synchronized void showBrightnessDialog(float percent) {
        if (currentBrightness < 0) {
            currentBrightness = ((Activity) context).getWindow().getAttributes().screenBrightness;
            if (currentBrightness <= 0.00f) {
                currentBrightness = 0.50f;
            } else if (currentBrightness < 0.01f) {
                currentBrightness = 0.01f;
            }
        }
        WindowManager.LayoutParams lpa = ((Activity) context).getWindow().getAttributes();
        lpa.screenBrightness = currentBrightness + percent;
        if (lpa.screenBrightness > 1.0) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        ((Activity) context).getWindow().setAttributes(lpa);
        if (acodePlayerListener == null) {
            return;
        }
        acodePlayerListener.onBrightness(100, (int) (lpa.screenBrightness * 100));
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        currentVolume = -1;
        currentBrightness = -1f;
        if (newPosition >= 0) {
            acodePlayerListener.oEndGesture(GestureEnum.PROGRESS);
            player.seekTo(newPosition);
            newPosition = -1;
            return;
        }
        acodePlayerListener.oEndGesture(GestureEnum.VOLUM);
    }
}
