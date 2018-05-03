package com.acode.player.bean;

import android.net.Uri;

/**
 * user:yangtao
 * date:2018/4/271150
 * email:yangtao@bjxmail.com
 * introduce:视频实体
 */
public class PlayerBean {
    //视频的uri
    private Uri uri;

    //视频名称
    private String title;

    //视频介绍
    private String info;

    //视频的封面图片地址
    private String imgUrl;

    //视频的播放时间
    private int currentTime;

    //视频的总时长
    private int endTime;

    //视频的播放进度
    private int currentProgress;

    //视频的缓冲进度
    private int secondProgress;

    //视频的上传地址
    private String url;

    //视频的上传进度
    private int uploadProgress;

    @Override
    public String toString() {
        return "PlayerBean{" +
                "uri=" + uri +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", currentTime=" + currentTime +
                ", endTime=" + endTime +
                ", currentProgress=" + currentProgress +
                ", secondProgress=" + secondProgress +
                ", url='" + url + '\'' +
                ", uploadProgress=" + uploadProgress +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public PlayerBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public PlayerBean setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public PlayerBean setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public Uri getUri() {
        return uri;
    }

    public PlayerBean setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public PlayerBean setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public int getEndTime() {
        return endTime;
    }

    public PlayerBean setEndTime(int endTime) {
        this.endTime = endTime;
        return this;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public PlayerBean setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        return this;
    }

    public int getSecondProgress() {
        return secondProgress;
    }

    public PlayerBean setSecondProgress(int secondProgress) {
        this.secondProgress = secondProgress;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PlayerBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getUploadProgress() {
        return uploadProgress;
    }

    public PlayerBean setUploadProgress(int uploadProgress) {
        this.uploadProgress = uploadProgress;
        return this;
    }
}
