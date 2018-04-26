package com.acode.player.lib.entity;

/**
 * user:yangtao
 * date:2018/4/240949
 * email:yangtao@bjxmail.com
 * introduce:功能
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:bean
 * Created by arthinking on 2017/6/26.
 */

public class Material implements Parcelable {
    private String mLogo;
    private String title;
    private String time;
    private String filePath;
    private boolean isChecked;
    private long fileSize;
    private long fileId;
    private long uploadedSize;
    private int fileType;
    private boolean uploaded;
    private int progress; //上传进度
    private String timeStamps; //时间戳
    private int flag;//上传标志 0-正常 1--网络错误 2--超时(除了0以为均为上传失败标识)


    public static Parcelable.Creator<Material> getCREATOR() {
        return CREATOR;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public long getUploadedSize() {
        return uploadedSize;
    }

    public void setUploadedSize(long uploadedSize) {
        this.uploadedSize = uploadedSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getLogo() {
        return mLogo;
    }

    public void setLogo(String logo) {
        mLogo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(String timeStamps) {
        this.timeStamps = timeStamps;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "MaterialBean{" +
                "mLogo='" + mLogo + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", filePath='" + filePath + '\'' +
                ", isChecked=" + isChecked +
                ", fileSize=" + fileSize +
                ", fileId=" + fileId +
                ", uploadedSize=" + uploadedSize +
                ", fileType=" + fileType +
                ", uploaded=" + uploaded +
                ", progress=" + progress +
                ", timeStamps='" + timeStamps + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public Material() {
    }

    protected Material(Parcel in) {
        mLogo = in.readString();
        title = in.readString();
        time = in.readString();
        filePath = in.readString();
        isChecked = in.readByte() != 0;
        fileSize = in.readLong();
        fileId = in.readLong();
        uploadedSize = in.readLong();
        fileType = in.readInt();
        uploaded = in.readByte() != 0;
        progress = in.readInt();
        timeStamps = in.readString();
        flag = in.readInt();
    }

    public static final Creator<Material> CREATOR = new Creator<Material>() {
        @Override
        public Material createFromParcel(Parcel in) {
            return new Material(in);
        }

        @Override
        public Material[] newArray(int size) {
            return new Material[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLogo);
        dest.writeString(title);
        dest.writeString(time);
        dest.writeString(filePath);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeLong(fileSize);
        dest.writeLong(fileId);
        dest.writeLong(uploadedSize);
        dest.writeInt(fileType);
        dest.writeByte((byte) (uploaded ? 1 : 0));
        dest.writeInt(progress);
        dest.writeString(timeStamps);
        dest.writeInt(flag);
    }
}
