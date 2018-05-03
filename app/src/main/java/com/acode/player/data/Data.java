package com.acode.player.data;

import android.net.Uri;

import com.acode.player.bean.PlayerBean;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;

/**
 * user:yangtao
 * date:2018/4/271355
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class Data {
    public static ArrayList<PlayerBean> getPlayerBeans() {
        ArrayList<PlayerBean> playerBeans = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PlayerBean playerBean = new PlayerBean();
            playerBean.setUrl("");
            playerBean.setCurrentTime(0);
            playerBean.setEndTime(0);
            playerBean.setCurrentProgress(0);
            playerBean.setSecondProgress(0);
            playerBean.setUploadProgress(0);
            if (i == 0) {
                playerBean.setTitle("小神龙");
                playerBean.setInfo("小神龙谈父亲");
                playerBean.setUri(Uri.parse("http://oif1jvh5f.bkt.clouddn.com/tmp.mp4"));
            }
            if (i == 1) {
                playerBean.setTitle("烟火里的尘埃");
                playerBean.setInfo("不是华晨宇唱的烟火里的尘埃");
                playerBean.setUri(Uri.parse("http://oif1jvh5f.bkt.clouddn.com/CFNetworkDownload_t3QJVZ.mp4"));
            }
            playerBeans.add(playerBean);
        }
        return playerBeans;
    }
}
