package com.acode.player.lib.files;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.acode.player.lib.entity.Material;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * user:yangtao
 * date:2018/4/240949
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class VideosFilsUtils {
    /**
     * 获取本地所有的视频
     *
     * @return list
     */
    public static List<Material> getAllLocalVideos(Context context) {
        long totalUploadCount = 1000;
        String[] projection = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };
        //全部图片
        String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=?";
        String[] whereArgs = {"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv",
                "video/mkv", "video/mov", "video/mpg"};
        List<Material> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection, where, whereArgs, MediaStore.Video.Media.DATE_ADDED + " DESC ");
        if (cursor == null) {
            return list;
        }
        try {
            while (cursor.moveToNext()) {
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
                if (size < 600 * 1024 * 1024) {//<600M
                    Material materialBean = new Material();
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
                    materialBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                    materialBean.setLogo(path);
                    materialBean.setFilePath(path);
                    materialBean.setChecked(false);
                    materialBean.setFileType(2);
                    materialBean.setFileId(totalUploadCount++);
                    materialBean.setUploadedSize(0);
                    materialBean.setTimeStamps(System.currentTimeMillis() + "");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                    String t = format.format(duration);
                    materialBean.setTime(t);
                    materialBean.setFileSize(size);
                    list.add(materialBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
}
