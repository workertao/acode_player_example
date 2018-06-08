package com.acode.player.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.acode.player.R;
import com.acode.player.lib.AcodePlayerView;
import com.acode.player.lib.bean.PlayerBean;
import com.acode.player.lib.data.Data;
import com.acode.player.lib.utils.PermissionUtils;
import com.acode.player.listplayer.ListPlayerActivity;

public class OnePlayerActivity extends Activity {
    private LinearLayout ll_video_files_root;

    private AcodePlayerView acodePlayerView;

    //是否是首次加载
    private boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        initView();
        initData();
    }

    private void initView() {
        acodePlayerView = findViewById(R.id.acodePlayerView);
        ll_video_files_root = findViewById(R.id.ll_video_files_root);
    }

    private void initData() {
        initPlaybeanData();
    }

    //初始化数据
    public void initPlaybeanData() {
        ll_video_files_root.removeAllViews();
        for (final PlayerBean playerBean : Data.getPlayerBeans()) {
            Button button = new Button(OnePlayerActivity.this);
            button.setText(playerBean.getTitle());
            ll_video_files_root.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFirst) {
                        acodePlayerView.showLoading();
                        isFirst = false;
                    }
                    acodePlayerView.pausePlayer();
                    acodePlayerView.readyPlayer(playerBean);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (acodePlayerView == null) {
            return;
        }
        acodePlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (acodePlayerView == null) {
            return;
        }
        acodePlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (acodePlayerView == null) {
            return;
        }
        acodePlayerView.onConfigurationChanged(newConfig.orientation);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (acodePlayerView == null) {
            return;
        }
        acodePlayerView.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (acodePlayerView == null) {
            return super.onKeyDown(keyCode, event);
        }
        return acodePlayerView.onKeyDown(keyCode, event);
    }
}
