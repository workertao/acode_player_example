package com.acode.player.listplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.acode.player.R;

/**
 * user:yangtao
 * date:2018/5/111414
 * email:yangtao@bjxmail.com
 * introduce:test
 */
public class ListPlayerActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
        initData();
    }

    private void initView() {
    }

    private void initData() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fram, new CircleFragment(), CircleFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        CircleFragment circleFragment = (CircleFragment) getSupportFragmentManager().findFragmentByTag(CircleFragment.class.getSimpleName());
        return circleFragment.onKeyDown(keyCode, event);
    }
}
