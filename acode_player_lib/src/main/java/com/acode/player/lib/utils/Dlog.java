package com.acode.player.lib.utils;

import android.util.Log;

/**
 * user:yangtao
 * date:2018/6/70958
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class Dlog {
    public static void i(Class clazz, String msg) {
        Log.d("|acodePlayer|" + clazz.getSimpleName(), msg);
    }
}
