package com.acode.player.base;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.acode.player.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;


/**
 * user:yangtao
 * date:2018/3/231617
 * email:yangtao@bjxmail.com
 * introduce:DBaseFragment  管理滑动fragment
 */
public abstract class DTBaseFragment extends DBaseFragment implements ViewPager.OnPageChangeListener {
    //tab
    private MagicIndicator magiIndicator;
    //vp
    private ViewPager mViewPager;
    //tab跳转器
    private CommonNavigator commonNavigator;

    //tab适配器
    public abstract PagerAdapter setTabAdapter();

    //当前选中的channel
    public abstract void getChannel(int position);

    //当前tab点击事件
    public abstract void onTabClick(int index);


    @Override
    public int res() {
        return R.layout.base_fragment_tab_res;
    }

    @Override
    public void initView() {
        magiIndicator = (MagicIndicator) centerView.findViewById(R.id.mid_gf_tab);
        mViewPager = (ViewPager) centerView.findViewById(R.id.vp_gf_list);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化指示器
     */
    public void initMagiIndicator(final ArrayList<Tab> gfTabs) {
        mViewPager.setAdapter(setTabAdapter());
        magiIndicator.setBackgroundColor(Color.WHITE);
        commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return gfTabs == null ? 0 : gfTabs.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(gfTabs.get(index).getTitle());
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(Color.parseColor("#000000"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#FFA500"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewPager.getCurrentItem() != index) {
                            mViewPager.setCurrentItem(index);
                            return;
                        }
                        onTabClick(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 2.0f;
                } else if (index == 1) {
                    return 1.2f;
                } else {
                    return 1.0f;
                }
            }
        });
        magiIndicator.setNavigator(commonNavigator);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        magiIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        getChannel(position);
    }

    @Override
    public void onPageSelected(int position) {
        magiIndicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        magiIndicator.onPageScrollStateChanged(state);
    }

    //设置展示的position
    public void setVpPosition(int position) {
        mViewPager.setCurrentItem(position);
        isOnResumeRefresh = true;
    }
}