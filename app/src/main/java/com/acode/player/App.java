package com.acode.player;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * user:yangtao
 * date:2018/4/240942
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class App extends Application{
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
