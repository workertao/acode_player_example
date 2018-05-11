package com.acode.player;

import android.content.Context;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.acode.player.bean.PlayerBean;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
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

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * user:yangtao
 * date:2018/4/261348
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class AcodePlayerView extends FrameLayout implements View.OnClickListener, AcodePlayerStateListener {
    //更新当前时间和进度条
    private final int UPDATE_CURRNET_UI = 1000;
    //更新当前时间
    private final String CURRENT_TIME = "CURRENT_TIME";
    //更新当前进度条
    private final String CURRENT_PROGRESS = "CURRENT_PROGRESS";
    //更新当前缓冲进度条
    private final String SECOND_PROGRESS = "SECOND_PROGRESS";
    private Context context;
    //视频中间的播放按钮
    private Button btn_start_play;
    //视频左下角的播放按钮
    private Button btn_bottom_start_play;
    //视频右侧的放大/缩小按钮
    private Button btn_bottom_full_screen;
    //当前播放时间
    private TextView tv_bottom_curr_time;
    //总时长
    private TextView tv_bottom_end_time;
    //进度条
    private SeekBar seekBar;
    //播放内容的view
    private SurfaceView sv_player;
    //播放器
    private SimpleExoPlayer player;
    //定时监听播放状态
    private TimerUtils timerUtils;
    //播放实体类
    private PlayerBean playerBean;
    //记录是不是页面切换
    private boolean isSucfare;

    public AcodePlayerView(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public AcodePlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public AcodePlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.acode_player_view, null);
        sv_player = view.findViewById(R.id.sv_player);
        seekBar = view.findViewById(R.id.seekBar);
        btn_start_play = view.findViewById(R.id.btn_start_play);
        btn_bottom_start_play = view.findViewById(R.id.btn_bottom_start_play);
        btn_bottom_full_screen = view.findViewById(R.id.btn_bottom_full_screen);
        tv_bottom_curr_time = view.findViewById(R.id.tv_bottom_curr_time);
        tv_bottom_end_time = view.findViewById(R.id.tv_bottom_end_time);
        btn_start_play.setOnClickListener(this);
        if (player == null) {
            createPlayer();
        }
        this.addView(view);
    }

    //创建播放器
    private void createPlayer() {
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
        player.setVideoSurfaceView(sv_player);

    }

    /**
     * 准备播放
     *
     * @param playerBean 播放数据源
     */
    public void readyPlayer(PlayerBean playerBean) {
        this.playerBean = playerBean;
        //创建定时间监听播放状态
        timerUtils = new TimerUtils(this, player, playerBean);
        //先将定时器关闭
//        timerUtils.stop();
        // 测量播放带宽，如果不需要可以传null
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        // 创建加载数据的工厂
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getPackageName()), bandwidthMeter);

        // 创建解析数据的工厂
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // 传入Uri、加载数据的工厂、解析数据的工厂，就能创建出MediaSource
        MediaSource videoSource = new ExtractorMediaSource(playerBean.getUri(),
                dataSourceFactory, extractorsFactory, null, null);

        // Prepare
        player.prepare(videoSource);

        //播放监听
        player.addListener(eventListener);
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
        btn_start_play.setText("暂停");
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
        btn_start_play.setText("播放");
    }

    //释放资源
    public void cancel() {
        if (player == null) {
            return;
        }
        timerUtils.stop();
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
        isSucfare = false;
        createPlayer();
        readyPlayer(playerBean);
        Log.d("post","A:"+playerBean.getCurrentPosition());
        Log.d("post","B:"+playerBean.getDuration());
        if (playerBean.getCurrentPosition()>=playerBean.getDuration()){
            player.seekTo(playerBean.getDuration());
            player.setPlayWhenReady(false);
            timerUtils.stop();
            btn_start_play.setText("播放");
            return;
        }
        player.seekTo(playerBean.getCurrentPosition());
        player.setPlayWhenReady(true);
        timerUtils.start();
        btn_start_play.setText("暂停");
    }

    /**
     * 页面切换调用此方法
     */
    public void onPause() {
        isSucfare = true;
        if (player == null) {
            return;
        }
        if (timerUtils == null) {
            return;
        }
        //保存当前视频的播放位置
        playerBean.setCurrentPosition(player.getCurrentPosition());
        //保存当前视频的总长度
        playerBean.setDuration(player.getDuration());
        //更新界面
        btn_start_play.setText("播放");
        cancel();
    }

    //初始化播放器
    private void initPlayer() {
        //初始化界面UI
        //当前的播放时间
        tv_bottom_curr_time.setText(playerBean.getCurrentTime() + "");
        //当前视频的总时长
        tv_bottom_end_time.setText(String.valueOf(player.getDuration() / 1000));
        //当前进度条
        seekBar.setProgress(playerBean.getCurrentProgress());
        //当前缓冲进度条
        seekBar.setSecondaryProgress(playerBean.getSecondProgress());
        //监听进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("post", "不想以前，不谈以后，活在当下：" + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("post", "我要紧紧的把你抱住：" + seekBar.getProgress());
                pausePlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("post", "对你的疼爱是手放开：" + seekBar.getProgress());
                player.seekTo(seekBar.getProgress() * (player.getDuration() / 100));
                int currentTime = (int) (player.getCurrentPosition() / 1000);
                int currentProgress = (int) (new BigDecimal((float) player.getCurrentPosition() / player.getDuration()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
                int secondProgressPer = player.getBufferedPercentage();
                playerBean.setCurrentTime(currentTime);
                playerBean.setCurrentProgress(currentProgress);
                playerBean.setSecondProgress(secondProgressPer);
                startPlayer();
            }
        });
    }

    @Override
    public void playerRuning(PlayerBean playerBean) {
        //播放中
        this.playerBean = playerBean;
        handler.sendEmptyMessage(UPDATE_CURRNET_UI);
    }

    @Override
    public void playPause() {
        Log.d("post", "播放暂停");
    }

    @Override
    public void playerComplete() {
        Log.d("post", "播放完成");
    }


    //开启线程读取进度
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //如果是播放状态  去更新进度条and当前的时间
            switch (msg.what) {
                case UPDATE_CURRNET_UI:
                    //更新当前时间
                    tv_bottom_curr_time.setText(String.valueOf(playerBean.getCurrentTime()));
                    //更新当前进度条
                    seekBar.setProgress(playerBean.getCurrentProgress());
                    //更新当前缓冲进度条
                    seekBar.setSecondaryProgress(playerBean.getSecondProgress());
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bottom_start_play:
            case R.id.btn_start_play:
                if (player == null) {
                    return;
                }
                if (!player.getPlayWhenReady()) {
                    startPlayer();
                    return;
                }
                pausePlayer();
                break;
        }
    }

    //保存当前的播放状态
    private void savePlayerState() {

    }

    //读取当前的播放状态
    private void readPlayerState() {

    }


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
            Log.d("post", "播放: onLoadingChanged ");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case PlaybackState.STATE_PLAYING:
                    //初始化播放点击事件并设置总时长
                    Log.d("post：", "总时长：" + player.getDuration() / 1000);
                    Log.d("post", "播放状态: 准备 playing");
                    initPlayer();
                    break;

                case PlaybackState.STATE_BUFFERING:
                    Log.d("post", "播放状态: 缓存完成 playing");
                    break;

                case PlaybackState.STATE_CONNECTING:
                    Log.d("post", "播放状态: 连接 CONNECTING");
                    break;

                case PlaybackState.STATE_ERROR://错误
                    Log.d("post", "播放状态: 错误 STATE_ERROR");
                    break;

                case PlaybackState.STATE_FAST_FORWARDING:
                    Log.d("post", "播放状态: 快速传递");
                    break;

                case PlaybackState.STATE_NONE:
                    Log.d("post", "播放状态: 无 STATE_NONE");
                    break;

                case PlaybackState.STATE_PAUSED:
                    Log.d("post", "播放状态: 暂停 PAUSED");
                    break;

                case PlaybackState.STATE_REWINDING:
                    Log.d("post", "播放状态: 倒回 REWINDING");
                    break;

                case PlaybackState.STATE_SKIPPING_TO_NEXT:
                    Log.d("post", "播放状态: 跳到下一个");
                    break;

                case PlaybackState.STATE_SKIPPING_TO_PREVIOUS:
                    Log.d("post", "播放状态: 跳到上一个");
                    break;

                case PlaybackState.STATE_SKIPPING_TO_QUEUE_ITEM:
                    Log.d("post", "播放状态: 跳到指定的Item");
                    break;

                case PlaybackState.STATE_STOPPED:
                    //停止播放
                    timerUtils.stop();
                    Log.d("post", "播放状态: 停止的 STATE_STOPPED");
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
            timerUtils.stop();
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
}
