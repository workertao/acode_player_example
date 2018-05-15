package com.acode.player.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

/**
 * user:yangtao
 * date:2018/4/130911
 * email:yangtao@bjxmail.com
 * introduce:动画
 */
public class AnimUtils {
    /**
     * @param o1 放大/缩小的对象
     * @param o2 渐变的对象
     */
    public static void playScaleWithAlpha(Object o1, Object o2) {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator scaleXo1 = ObjectAnimator.ofFloat(o1, "scaleX", 1f, 0.5f, 1f);
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator scaleYo1 = ObjectAnimator.ofFloat(o1, "scaleY", 1f, 0.5f, 1f);
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator alphaO2 = ObjectAnimator.ofFloat(o2, "alpha", 0f, 0.7f, 0.8f, 0.9f, 1f, 0.9f, 0.8f, 0.7f, 0f);
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator scaleXo2 = ObjectAnimator.ofFloat(o2, "scaleX", 0f, 1.2f);
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator scaleYo2 = ObjectAnimator.ofFloat(o2, "scaleY", 0f, 1.2f);

        AnimatorSet set = new AnimatorSet();
        set.play(scaleXo1).with(scaleYo1).with(scaleXo2).with(scaleYo2).with(alphaO2);
        set.setDuration(1000);
        set.start();
    }

    public static void playRotation360(Object o) {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator rotation = ObjectAnimator.ofFloat(o, "rotation", 0f, 360f);
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.setDuration(1000);
        rotation.start();
    }
}
