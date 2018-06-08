package com.acode.player.lib.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * user:yangtao
 * date:2018/6/80951
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class NoScrollLinearManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollLinearManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
