package com.acode.player.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * user:yangtao
 * date:2018/5/161405
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class CircleFragment extends DTBaseFragment {

    @Override
    public void initData() {
        //初始化TAB数据
        initMagiIndicator(getData());
    }

    @Override
    public PagerAdapter setTabAdapter() {
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.setDatas(getData());
        return tabAdapter;
    }

    @Override
    public void getChannel(int position) {
        //当前频道
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
}
