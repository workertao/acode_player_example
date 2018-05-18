package com.acode.player;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.acode.player.base.TestActivity;
import com.acode.player.bean.PlayerBean;
import com.acode.player.data.Data;
import com.acode.player.lib.utils.PermissionUtils;
import com.acode.player.utils.NetUtils;

public class MainActivity extends AcodeBaseActivity {
    public static String TAG = "MainActivity";
    private final int PERMISSION_REQ_CODE = 100;
    //获取本地视频列表
    private Button btn;

    private Button btn_intent;

    private LinearLayout ll_video_files_root;

    private PermissionUtils permissionUtils;

    private AcodePlayerView acodePlayerView;

    private NetUtils netUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNet();
        initView();
        initData();
    }

    private void initNet() {
        netUtils = new NetUtils(this);
        netUtils.setOnNetBackListener(new NetUtils.OnNetBackListener() {
            @Override
            public void onNetWork(int type) {
                switch (type) {
                    case NetUtils.TYPE_NONE:
                        Toast.makeText(MainActivity.this, "没网络", Toast.LENGTH_SHORT).show();
                        break;
                    case NetUtils.TYPE_WIFI:
                        Toast.makeText(MainActivity.this, "wifi网络", Toast.LENGTH_SHORT).show();
                        break;
                    case NetUtils.TYPE_MOBILE:
                        Toast.makeText(MainActivity.this, "移动网络", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        netUtils.registerReceiverNet();
    }

    private void initData() {
        permissionUtils = new PermissionUtils(this);
        permissionUtils.requestPermission(PERMISSION_REQ_CODE, permissionUtils.request_permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQ_CODE:

                break;
        }
    }

    private void initView() {
        acodePlayerView = findViewById(R.id.acodePlayerView);
        btn = findViewById(R.id.btn);
        btn_intent = findViewById(R.id.btn_intent);
        ll_video_files_root = findViewById(R.id.ll_video_files_root);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_video_files_root.removeAllViews();
                for (final PlayerBean playerBean : Data.getPlayerBeans()) {
                    Button button = new Button(MainActivity.this);
                    button.setText(playerBean.getTitle());
                    ll_video_files_root.addView(button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            acodePlayerView.pausePlayer();
                            acodePlayerView.readyPlayer(playerBean);
                        }
                    });
                }
            }
        });
        btn_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        acodePlayerView.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        acodePlayerView.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("post", "横竖屏切换");
        acodePlayerView.onConfigurationChanged(newConfig.orientation);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        if (acodePlayerView != null) {
            acodePlayerView.cancel();
        }
        if (netUtils != null) {
            netUtils.unNetworkBroadcastReceiver();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (acodePlayerView ==  null){
            return super.onKeyDown(keyCode, event);
        }
        return acodePlayerView.onKeyDown(keyCode,event);
    }

    /**
     * 解决找不到某些jar的静态方法
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
