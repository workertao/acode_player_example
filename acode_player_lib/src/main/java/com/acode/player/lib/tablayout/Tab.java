package com.acode.player.lib.tablayout;

import java.io.Serializable;

/**
 * user:yangtao
 * date:2018/5/161404
 * email:yangtao@bjxmail.com
 * introduce:TAB功能
 */
public class Tab implements Serializable{
    private int id;
    private String title;
    private String info;

    public int getId() {
        return id;
    }

    public Tab setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Tab setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public Tab setInfo(String info) {
        this.info = info;
        return this;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
