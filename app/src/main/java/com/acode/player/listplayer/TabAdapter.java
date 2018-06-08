package com.acode.player.listplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.acode.player.lib.tablayout.Tab;

import java.util.ArrayList;
import java.util.List;


/**
 * user:yangtao
 * date:2018/3/200936
 * email:yangtao@bjxmail.com
 * introduce:首页适配器
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    private List<Tab> datas;

    private List<ListPlayerFragment> testFragments;

    public void setDatas(List<Tab> datas) {
        this.datas = datas;
        if (datas == null) {
            return;
        }
        testFragments = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ListPlayerFragment fragment = new ListPlayerFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("tab", datas.get(i));
            fragment.setArguments(bundle);
            testFragments.add(fragment);
        }
    }

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (testFragments==null){
            return null;
        }
        return testFragments.get(position);
    }
//    如果不需要重复刷新数据则不需要实现父类destroyItem的方法
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//    }

    @Override
    public int getCount() {
        return datas == null ? null : datas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return datas.get(position).getTitle();
    }
}
