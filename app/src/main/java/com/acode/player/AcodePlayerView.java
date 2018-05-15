package com.acode.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acode.player.anim.AnimUtils;
import com.acode.player.bean.PlayerBean;
import com.acode.player.utils.DimenUtils;
import com.acode.player.utils.TimerUtils;
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

import java.math.BigDecimal;

/**
 * user:yangtao
 * date:2018/4/261348
 * email:yangtao@bjxmail.com
 * introduce:播放器
 */
public class AcodePlayerView extends FrameLayout implements View.OnClickListener, AcodePlayerStateListener,View.OnTouchListener {
    //更新当前时间和进度条
    private final int UPDATE_CURRNET_UI = 1000;
    private Context context;
    //视频中间的播放按钮
    private ImageView btn_start_play;
    //视频右侧的放大/缩小按钮
    private ImageView btn_bottom_full_screen;
    //播放器
    private FrameLayout rl_player_view;
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
    //loading
    private ImageView iv_loading;
    //播放器
    private View view;
    //控制播放的view
    private RelativeLayout rl_controller_view;
    //是否在缓冲加载  默认未加载
    private boolean isLoad;
    //返回键
    private ImageView img_back;
    //title
    private TextView tv_title;
    //loading
    private RelativeLayout rl_loading;
    //手势监听
    private GestureDetector gestureDetector;


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

    @SuppressLint("WrongViewCast")
    private void initView() {
        gestureDetector = new GestureDetector(context,new PlayerGestureListener());
        view = LayoutInflater.from(context).inflate(R.layout.acode_player_view, null);
        rl_player_view = view.findViewById(R.id.rl_player_view);
        img_back = view.findViewById(R.id.img_back);
        tv_title = view.findViewById(R.id.tv_title);
        rl_controller_view = view.findViewById(R.id.rl_controller_view);
        sv_player = view.findViewById(R.id.sv_player);
        seekBar = view.findViewById(R.id.seekBar);
        btn_start_play = view.findViewById(R.id.btn_start_play);
        btn_bottom_full_screen = view.findViewById(R.id.btn_bottom_full_screen);
        tv_bottom_curr_time = view.findViewById(R.id.tv_bottom_curr_time);
        tv_bottom_end_time = view.findViewById(R.id.tv_bottom_end_time);
        btn_start_play.setOnClickListener(this);
        btn_bottom_full_screen.setOnClickListener(this);
        rl_controller_view.setOnClickListener(this);
        sv_player.setOnClickListener(this);
        img_back.setOnClickListener(this);
        if (player == null) {
            createPlayer();
        }
        this.addView(view);
        sv_player.setOnTouchListener(this);
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

        // 添加数据源
        player.prepare(videoSource);

        // 设置播放进度
        player.seekTo(playerBean.getCurrentPosition());

        // 播放监听
        player.addListener(eventListener);

        //设置标题
        tv_title.setText(playerBean.getInfo());
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
        btn_start_play.setImageResource(R.mipmap.exo_controls_pause);
    }

    /**
     * 播放中
     *
     * @param playerBean 当前播放视频实体
     */
    @Override
    public void playerRuning(PlayerBean playerBean) {
        this.playerBean = playerBean;
        handler.sendEmptyMessage(UPDATE_CURRNET_UI);
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
        btn_start_play.setImageResource(R.mipmap.exo_controls_play);
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
        isSucfare = false;
        createPlayer();
        readyPlayer(playerBean);
        if (playerBean.getCurrentPosition() >= playerBean.getDuration()) {
            player.seekTo(playerBean.getDuration());
            player.setPlayWhenReady(false);
            timerUtils.stop();
            btn_start_play.setImageResource(R.mipmap.exo_controls_play);
            return;
        }
        player.seekTo(playerBean.getCurrentPosition());
        player.setPlayWhenReady(false);
        timerUtils.start();
        btn_start_play.setImageResource(R.mipmap.exo_controls_play);
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
        //更新界面
        btn_start_play.setImageResource(R.mipmap.exo_controls_play);
        cancel();
    }

    /**
     * 展示loading
     */
    public void showLoading() {
        if (rl_loading == null){
            rl_loading = view.findViewById(R.id.rl_loading);
        }
        if (iv_loading == null) {
            iv_loading = view.findViewById(R.id.iv_loading);
            AnimUtils.playRotation360(iv_loading);
        }
        rl_loading.setVisibility(VISIBLE);
    }

    //隐藏loading
    public void dismissLoading() {
        if (rl_loading == null){
            return;
        }
        rl_loading.setVisibility(GONE);
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pausePlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress() * (player.getDuration() / 100));
                int currentTime = (int) (player.getCurrentPosition() / 1000);
                int currentProgress = (int) (new BigDecimal((float) player.getCurrentPosition() / player.getDuration()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
                int secondProgressPer = player.getBufferedPercentage();
                playerBean.setCurrentTime(currentTime);
                playerBean.setCurrentProgress(currentProgress);
                playerBean.setSecondProgress(secondProgressPer);
                playerBean.setCurrentPosition(player.getCurrentPosition());
                playerBean.setDuration(player.getDuration());
                startPlayer();
            }
        });
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
            case R.id.btn_bottom_full_screen:
            case R.id.img_back:
                //int ORIENTATION_PORTRAIT = 1;  竖屏
                //int ORIENTATION_LANDSCAPE = 2; 横屏
                int screenNum = getResources().getConfiguration().orientation;
                if (screenNum == 2) {
                    //切换成竖屏
                    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return;
                }
                //切换成横屏
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.rl_controller_view:
                if (isLoad) {
                    return;
                }
                rl_controller_view.setVisibility(GONE);
                break;
            case R.id.sv_player:
                if (isLoad) {
                    return;
                }
                rl_controller_view.setVisibility(VISIBLE);
                break;
        }
    }

    //横竖屏切换
    public void onConfigurationChanged(int newConfig) {
        if (newConfig == Configuration.ORIENTATION_LANDSCAPE) {
            screenFull();
        } else {
            screenVertical();
        }
    }

    //横屏播放
    private void screenFull() {
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rl_player_view.setLayoutParams(params);
    }

    //竖屏播放
    private void screenVertical() {
        ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = DimenUtils.dip2px(context, 300);
        rl_player_view.setLayoutParams(params);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //将触摸事件交给GestureDetector来处理
        return gestureDetector.onTouchEvent(event);
    }

    //手势监听
    private class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("post","onDoubleTap");
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("post","onDown");
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("post","e1:"+e1.getAction()+"");
            Log.d("post","e2:"+e2.getAction()+"");
            Log.d("post","distanceX:"+distanceX);
            Log.d("post","distanceY:"+distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
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
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    //缓冲中展示loading
                    isLoad = true;
                    rl_controller_view.setVisibility(GONE);
                    showLoading();
                    Log.d("post", "STATE_BUFFERING");
                    break;
                case Player.STATE_ENDED:
                    Log.d("post", "STATE_ENDED");
                    timerUtils.stop();
                    btn_start_play.setImageResource(R.mipmap.exo_controls_pause);
                    break;
                case Player.STATE_IDLE:
                    Log.d("post", "STATE_IDLE:网络状态差，请检查网络。。。");
                    Toast.makeText(context, "网络状态差，请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case Player.STATE_READY:
                    Log.d("post", "STATE_READY：" + player.getNextWindowIndex());
                    isLoad = false;
                    dismissLoading();
                    //初始化播放点击事件并设置总时长
                    initPlayer();
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            int screenNum = getResources().getConfiguration().orientation;
            if (screenNum == 2) {
                //切换成竖屏
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            }
        }
        return false;
    }
}
