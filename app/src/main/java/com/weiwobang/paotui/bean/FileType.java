package com.weiwobang.paotui.bean;

public class FileType {
    private String data;
    private int type;

    public String getData() {
        return data;
    }

    public FileType(String data, int type) {
        this.data = data;
        this.type = type;
    }

    public FileType() {
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
