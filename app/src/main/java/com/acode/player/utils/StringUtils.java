package com.acode.player.utils;

import com.google.android.exoplayer2.util.Util;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

/**
 * user:yangtao
 * date:2018/5/181520
 * email:yangtao@bjxmail.com
 * introduce:String
 */
public class StringUtils {
    /**
     * 获取两个int类型的百分比
     *
     * @param a
     * @param b
     */
    public static String onPercentage(int a, int b) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format((float) a / (float) b * 100);
    }

    /**
     * 将播放器的当前时间格式化
     *
     * @param position
     * @return
     */
    public static String onFormatTime(long position) {
        //格式字符
        StringBuilder formatBuilder = new StringBuilder();
        //格式化类
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());
        //格式化
        return Util.getStringForTime(formatBuilder, formatter, position);
    }
}
