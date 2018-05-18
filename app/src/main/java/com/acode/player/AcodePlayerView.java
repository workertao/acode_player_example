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
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.acode.player.listener.AcodePlayerListener;
import com.acode.player.utils.DimenUtils;
import com.acode.player.utils.GestureEnum;
import com.acode.player.utils.StringUtils;

/**
 * user:yangtao
 * date:2018/4/261348
 * email:yangtao@bjxmail.com
 * introduce:播放器
 */
public class AcodePlayerView extends FrameLayout implements View.OnClickListener {
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
    private AcodePlayer player;
    //定时监听播放状态
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
    //系统设置音量/亮度
    private LinearLayout ll_player_sys_set_state;
    //音量还是亮度
    private TextView tv_sys_type;
    //进度
    private TextView tv_sys_num;
    //进度条
    private SeekBar sb_sys_progress;
    //进度view
    private LinearLayout ll_player_player_progress;
    //进度name
    private TextView tv_player_progress_name;
    //进度比
    private TextView tv_player_progress;
    //进度的进度条
    private SeekBar sb_player_progress;


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
        view = LayoutInflater.from(context).inflate(R.layout.acode_player_view, null);
        initPlayerView();
        initSystemSetView();
        initPlayerProgressView();
        this.addView(view);
        initListener();
        initData();
    }


    //初始化播放器的布局
    private void initPlayerView() {
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
    }

    //初始化系统设置布局
    public void initSystemSetView() {
        ll_player_sys_set_state = view.findViewById(R.id.ll_player_sys_set_state);
        tv_sys_type = view.findViewById(R.id.tv_sys_type);
        tv_sys_num = view.findViewById(R.id.tv_sys_num);
        sb_sys_progress = view.findViewById(R.id.sb_sys_progress);
    }

    private void initPlayerProgressView() {
        ll_player_player_progress = view.findViewById(R.id.ll_player_player_progress);
        tv_player_progress_name = view.findViewById(R.id.tv_player_progress_name);
        tv_player_progress = view.findViewById(R.id.tv_player_progress);
        sb_player_progress = view.findViewById(R.id.sb_player_progress);
    }

    //设置监听
    public void initListener() {
        btn_start_play.setOnClickListener(this);
        btn_bottom_full_screen.setOnClickListener(this);
        rl_controller_view.setOnClickListener(this);
        sv_player.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    private void initData() {
        player = new AcodePlayer(context, sv_player, new AcodePlayerListener() {
            @Override
            public void onLoading() {
                Log.d("post", "缓冲中。。。");
                //缓冲中展示loading
                isLoad = true;
                rl_controller_view.setVisibility(GONE);
                showLoading();
            }

            @Override
            public void onReady() {
                Log.d("post", "准备播放");
                isLoad = false;
                dismissLoading();
                //初始化播放点击事件并设置总时长
                initPlayer();
            }

            @Override
            public void onPlayering(PlayerBean pb) {
                Log.d("post", "播放中");
                playerBean = pb;
                handler.sendEmptyMessage(UPDATE_CURRNET_UI);
            }

            @Override
            public void onEnd() {
                Log.d("post", "播放结束");
                //回到开头
                player.seekTo(0);
                tv_bottom_curr_time.setText("00:00");
                seekBar.setProgress(0);
                btn_start_play.setImageResource(R.mipmap.exo_controls_play);
            }

            @Override
            public void onCatch() {
                Log.d("post", "播放异常");
                Toast.makeText(context, "网络状态差，请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Log.d("post", "播放错误");
            }

            @Override
            public void onVolumes(int maxVolume, int currentVolume) {
                ll_player_sys_set_state.setVisibility(VISIBLE);
                tv_sys_type.setText("音量");
                tv_sys_num.setText(StringUtils.onPercentage(currentVolume, maxVolume) + "%");
                sb_sys_progress.setMax(maxVolume);
                sb_sys_progress.setProgress(currentVolume);
            }

            @Override
            public void onBrightness(int maxBrightness, int currentBrightness) {
                ll_player_sys_set_state.setVisibility(VISIBLE);
                tv_sys_type.setText("亮度");
                tv_sys_num.setText(StringUtils.onPercentage(currentBrightness, maxBrightness) + "%");
                sb_sys_progress.setMax(maxBrightness);
                sb_sys_progress.setProgress(currentBrightness);
            }

            @Override
            public void onProgress(long seekTimePosition, long duration, String seekTime, String totalTime) {
                ll_player_player_progress.setVisibility(VISIBLE);
                tv_player_progress_name.setText("进度");
                tv_player_progress.setText(seekTime + "/" + totalTime);
                sb_player_progress.setMax((int) duration);
                sb_player_progress.setProgress((int) seekTimePosition);
                updateData();
            }

            @Override
            public void oEndGesture(GestureEnum state) {
                ll_player_player_progress.setVisibility(GONE);
                ll_player_sys_set_state.setVisibility(GONE);
                switch (state) {
                    case PROGRESS:
                        startPlayer();
                        break;
                    case VOLUM:
                        break;
                    case BRIGHTNESS:
                        break;
                }
            }
        });
        createPlayer();
    }

    /**
     * 创建播放器
     */
    public void createPlayer() {
        if (player.getPlayer() == null) {
            player.createPlayer();
        }
    }

    /**
     * 准备播放
     *
     * @param playerBean 播放的实体
     */
    public void readyPlayer(PlayerBean playerBean) {
        player.readyPlayer(playerBean);
        this.playerBean = playerBean;
        //设置标题
        tv_title.setText(playerBean.getInfo());
    }

    //开始播放
    public void startPlayer() {
        player.startPlayer();
        btn_start_play.setImageResource(R.mipmap.exo_controls_pause);
    }

    //暂停播放
    public void pausePlayer() {
        player.pausePlayer();
        btn_start_play.setImageResource(R.mipmap.exo_controls_play);
    }

    //释放资源
    public void cancel() {
        player.cancel();
    }

    /**
     * 再次回到此页面调用此方法
     */
    public void onResume() {
        player.onResume();
        btn_start_play.setImageResource(R.mipmap.exo_controls_play);
    }

    /**
     * 页面切换调用此方法
     */
    public void onPause() {
        player.onPause();
        //更新界面
        btn_start_play.setImageResource(R.mipmap.exo_controls_play);
    }

    /**
     * 展示loading
     */
    public void showLoading() {
        if (rl_loading == null) {
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
        if (rl_loading == null) {
            return;
        }
        rl_loading.setVisibility(GONE);
    }

    //初始化播放器
    private void initPlayer() {
        //初始化界面UI
        //当前的播放时间
        tv_bottom_curr_time.setText(StringUtils.onFormatTime(playerBean.getCurrentPosition()));
        //当前视频的总时长
        tv_bottom_end_time.setText(StringUtils.onFormatTime(playerBean.getDuration()));
        seekBar.setMax((int) playerBean.getDuration());
        //当前进度条
        seekBar.setProgress((int) playerBean.getCurrentPosition());
        //当前缓冲进度条
        seekBar.setSecondaryProgress(playerBean.getBufferedPercentage());
        //监听进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_bottom_curr_time.setText(StringUtils.onFormatTime(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pausePlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
                updateData();
                startPlayer();
            }
        });
    }

    /**
     * 更新当前实体的数据
     */
    private void updateData() {
        playerBean.setCurrentTime(StringUtils.onFormatTime(player.getCurrentPosition()));
        playerBean.setEndTime(StringUtils.onFormatTime(player.getDuration()));
        playerBean.setCurrentPosition(player.getCurrentPosition());
        playerBean.setBufferedPercentage(player.getBufferedPercentage());
        playerBean.setDuration(player.getDuration());
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
                    seekBar.setMax((int) playerBean.getDuration());
                    //更新当前进度条
                    seekBar.setProgress((int) playerBean.getCurrentPosition());
                    //更新当前缓冲进度条
                    seekBar.setSecondaryProgress(playerBean.getBufferedPercentage());
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_play:
                //播放按钮
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
                //横竖屏
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
            case R.id.img_back:
                //左上角返回键
                int screenNum1 = getResources().getConfiguration().orientation;
                if (screenNum1 == 2) {
                    //切换成竖屏
                    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return;
                }
                break;
            case R.id.rl_controller_view:
                //整个播放器的控制层view
                if (isLoad) {
                    return;
                }
                rl_controller_view.setVisibility(GONE);
                break;
            case R.id.sv_player:
                //播放器view
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
