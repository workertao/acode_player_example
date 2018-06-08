package com.acode.player.listplayer;

import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.acode.player.lib.utils.NoScrollLinearManager;
import com.acode.player.R;
import com.acode.player.lib.tablayout.DBaseFragment;
import com.acode.player.lib.utils.Dlog;

/**
 * user:yangtao
 * date:2018/5/161409
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class ListPlayerFragment extends DBaseFragment {
    private RecyclerView rv;
    private ListPlayerAdapter testAdapter;
    private int currentPosition = -1;
    private NoScrollLinearManager manager;

    @Override
    public int res() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView() {
        rv = centerView.findViewById(R.id.rv);
    }

    @Override
    public void initData() {
        testAdapter = new ListPlayerAdapter(getActivity());
        manager = new NoScrollLinearManager(getActivity());
        rv.setLayoutManager(manager);
        rv.setAdapter(testAdapter);
        //监听滑动，当前滑动的item不在可见视图内，则更新adapter
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstC = manager.findFirstVisibleItemPosition();
                int lastC = manager.findLastVisibleItemPosition();
                if (testAdapter == null || testAdapter.getApv() == null) {
                    return;
                }
                if (currentPosition == -1) {
                    return;
                }
                if (currentPosition >= firstC && currentPosition <= lastC) {
                    return;
                }
                Dlog.i(ListPlayerFragment.class, "currentPosition:" + currentPosition + "    firstC:" + firstC + "  lastC:" + lastC);
                testAdapter.getApv().pausePlayer();
                testAdapter.notifyItemChanged(currentPosition, currentPosition);
            }
        });
        //列表点击事件，记录当前点击的position
        testAdapter.setOnItemClickListener(new ListPlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (currentPosition != -1 && currentPosition != position) {
                    testAdapter.notifyItemChanged(currentPosition, currentPosition);
                }
                currentPosition = position;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (testAdapter == null || testAdapter.getApv() == null) {
            return;
        }
        testAdapter.getApv().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (testAdapter == null || testAdapter.getApv() == null) {
            return;
        }
        testAdapter.getApv().onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    /*8
     *  横竖屏切换的时候要处理一下
     *  禁止recylerview的滑动事件
     *  禁止viewpager的滑动事件
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (testAdapter == null || testAdapter.getApv() == null) {
            return;
        }
        testAdapter.getApv().onConfigurationChanged(newConfig.orientation);
        int screenNum = getActivity().getResources().getConfiguration().orientation;
        if (screenNum == 2) {
            //横屏
            manager.scrollToPositionWithOffset(currentPosition, 0); // 位置从0开始.
            manager.setScrollEnabled(false);
            ((CircleFragment) getParentFragment()).setVpScroll(false);
            ((CircleFragment) getParentFragment()).setMagiIndicatorVis(false);
            return;
        }
        //竖屏
        manager.setScrollEnabled(true);
        ((CircleFragment) getParentFragment()).setVpScroll(true);
        ((CircleFragment) getParentFragment()).setMagiIndicatorVis(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (testAdapter == null || testAdapter.getApv() == null) {
            return;
        }
        testAdapter.getApv().cancel();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (testAdapter == null || testAdapter.getApv() == null) {
            return false;
        }
        return testAdapter.getApv().onKeyDown(keyCode, event);
    }
}
