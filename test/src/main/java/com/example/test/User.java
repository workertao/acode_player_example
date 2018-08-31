package com.example.test;

/**
 * user:yangtao
 * date:2018/8/91441
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class User {
    private int id;
    private String name;

    public User() {
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
