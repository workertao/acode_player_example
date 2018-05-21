package com.acode.player.lib.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * user:yangtao
 * date:2018/4/271150
 * email:yangtao@bjxmail.com
 * introduce:视频实体
 */
public class PlayerBean implements Serializable {
    //视频的uri
    private Uri uri;

    //视频名称
    private String title;

    //视频介绍
    private String info;

    //视频的封面图片地址
    private String imgUrl;

    //视频的当前播放时间
    private String currentTime;

    //视频的总时长
    private String endTime;

    //视频的上传地址
    private String url;

    //视频的上传进度
    private int uploadProgress;

    //当前的播放位置
    private long currentPosition;
    //当前视频的总长度
    private long duration;
    //当前视频的缓冲进度
    private int bufferedPercentage;

    @Override
    public String toString() {
        return "PlayerBean{" +
                "uri=" + uri +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", currentTime=" + currentTime +
                ", endTime=" + endTime +
                ", url='" + url + '\'' +
                ", uploadProgress=" + uploadProgress +
                ", currentPosition=" + currentPosition +
                ", duration=" + duration +
                ", bufferedPercentage=" + bufferedPercentage +
                '}';
    }

    public int getBufferedPercentage() {
        return bufferedPercentage;
    }

    public PlayerBean setBufferedPercentage(int bufferedPercentage) {
        this.bufferedPercentage = bufferedPercentage;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public PlayerBean setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public PlayerBean setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
        return this;
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

    public String getCurrentTime() {
        return currentTime;
    }

    public PlayerBean setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public PlayerBean setEndTime(String endTime) {
        this.endTime = endTime;
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
