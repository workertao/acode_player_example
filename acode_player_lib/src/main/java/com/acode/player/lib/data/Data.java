package com.acode.player.lib.data;

import android.net.Uri;


import com.acode.player.lib.bean.PlayerBean;

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
        for (int i = 0; i < 4; i++) {
            PlayerBean playerBean = new PlayerBean();
            playerBean.setUrl("");
            playerBean.setCurrentTime("00:00");
            playerBean.setEndTime("00:00");
            playerBean.setUploadProgress(0);
            if (i == 0) {
                playerBean.setTitle("mp4");
                playerBean.setInfo("mp4");
                ArrayList<String> uris = new ArrayList<>();
                uris.add("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
                uris.add("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
                uris.add("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
                playerBean.setPlayerUrls(uris);
                ArrayList<String> linsNames = new ArrayList<>();
                linsNames.add("超清");
                linsNames.add("高清");
                linsNames.add("标清");
                playerBean.setLineNames(linsNames);
                playerBean.setMs(100000+System.currentTimeMillis());
            }
            if (i == 1) {
                playerBean.setTitle("m3u8暂不支持");
                playerBean.setInfo("m3u8暂不支持");
                ArrayList<String> uris = new ArrayList<>();
                uris.add("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
                uris.add("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
                uris.add("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
                playerBean.setPlayerUrls(uris);
                ArrayList<String> linsNames = new ArrayList<>();
                linsNames.add("超清");
                linsNames.add("高清");
                linsNames.add("标清");
                playerBean.setLineNames(linsNames);
            }
            if (i == 2) {
                playerBean.setTitle("rtmp暂不支持");
                playerBean.setInfo("rtmp暂不支持");
                ArrayList<String> uris = new ArrayList<>();
                uris.add("rtmp://media3.sinovision.net:1935/live/livestream");
                uris.add("rtmp://media3.sinovision.net:1935/live/livestream");
                uris.add("rtmp://media3.sinovision.net:1935/live/livestream");
                playerBean.setPlayerUrls(uris);
                ArrayList<String> linsNames = new ArrayList<>();
                linsNames.add("超清");
                linsNames.add("高清");
                linsNames.add("标清");
                playerBean.setLineNames(linsNames);
            }
            if (i == 3) {
                playerBean.setTitle("六一儿童节快乐");
                playerBean.setInfo("六一儿童节快乐");
                ArrayList<String> uris = new ArrayList<>();
                uris.add("http://mp4.vjshi.com/2015-05-11/1431320221859_786.mp4");
                uris.add("http://mp4.vjshi.com/2016-10-21/84bafe60ef0af95a5292f66b9f692504.mp4");
                uris.add("http://mp4.vjshi.com/2017-04-14/933db6540f53dd38974b4446388fb928.mp4");
                playerBean.setPlayerUrls(uris);
                ArrayList<String> linsNames = new ArrayList<>();
                linsNames.add("超清");
                linsNames.add("高清");
                linsNames.add("标清");
                playerBean.setLineNames(linsNames);
            }
            playerBeans.add(playerBean);
        }
        return playerBeans;
    }
}
