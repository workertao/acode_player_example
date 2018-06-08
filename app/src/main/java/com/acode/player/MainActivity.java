package com.acode.player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acode.player.lib.utils.PermissionUtils;
import com.acode.player.listplayer.ListPlayerActivity;
import com.acode.player.oneplayer.OnePlayerActivity;

/**
 * user:yangtao
 * date:2018/6/81533
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class MainActivity extends Activity {
    private Button btn1, btn2;

    private PermissionUtils permissionUtils;

    private final int PERMISSION_REQ_CODE = 100;

    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        tv = findViewById(R.id.tv);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OnePlayerActivity.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListPlayerActivity.class));
            }
        });
    }

    private void initData() {
        permissionUtils = new PermissionUtils(this);
        permissionUtils.requestPermission(PERMISSION_REQ_CODE, permissionUtils.request_permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //遍历循环查看用户是否授权，如果有一个没有授权就return
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        tv.setText("快，可以查看事列了！");
    }
}
