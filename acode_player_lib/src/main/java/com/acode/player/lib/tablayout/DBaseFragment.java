package com.acode.player.lib.tablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.acode.player.R;
import com.acode.player.lib.utils.Dlog;

/**
 * user:yangtao
 * date:2018/3/231547
 * email:yangtao@bjxmail.com
 * introduce:DBaseFragment
 */
public abstract class DBaseFragment extends Fragment {
    //加載佈局
    public abstract int res();

    //初始化view
    public abstract void initView();

    //初始化data
    public abstract void initData();


    public View centerView;

    private FrameLayout base_fl_fram_title;

    private FrameLayout base_fl_fram_center;

    //是否刷新当前页  默认不刷新
    public static boolean isOnResumeRefresh;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dlog.i(DBaseFragment.class,"onCreateView---------");
        centerView = LayoutInflater.from(getContext()).inflate(R.layout.base_fragment_res, null);
        base_fl_fram_title = (FrameLayout) centerView.findViewById(R.id.base_fl_fram_title);
        base_fl_fram_center = (FrameLayout) centerView.findViewById(R.id.base_fl_fram_center);
        //设置内容
        if (res() != 0) {
            base_fl_fram_center.addView(LayoutInflater.from(getActivity()).inflate(res(), null));
        }
        initView();
        return centerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
    }

}
