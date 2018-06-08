package com.acode.player.listplayer;

import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;

import com.acode.player.lib.tablayout.DTBaseFragment;
import com.acode.player.lib.tablayout.Tab;

import java.util.ArrayList;

/**
 * user:yangtao
 * date:2018/5/161405
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class CircleFragment extends DTBaseFragment {
    private TabAdapter tabAdapter;
    private int currentPosition;

    @Override
    public void initData() {
        //初始化TAB数据
        initMagiIndicator(getData());
    }

    @Override
    public PagerAdapter setTabAdapter() {
        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.setDatas(getData());
        return tabAdapter;
    }

    @Override
    public void getChannel(int position) {
        //当前频道
        this.currentPosition = position;
    }

    @Override
    public void onTabClick(int position) {
        //tab的点击事件
    }

    private ArrayList<Tab> getData() {
        ArrayList<Tab> tabs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Tab tab = new Tab();
            tab.setId(i);
            tab.setTitle("标题" + i);
            tab.setInfo("这是第" + i + "个recylerview");
            tabs.add(tab);
        }
        return tabs;
    }

    public void setVpScroll(boolean isScroll) {
        super.setVpScroll(isScroll);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return ((ListPlayerFragment) tabAdapter.getItem(currentPosition)).onKeyDown(keyCode, event);
    }
}
