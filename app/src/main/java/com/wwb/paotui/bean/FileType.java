package com.wwb.paotui.bean;

public class FileType {
    private String data="";
    private String type="";

    public String getData() {
        return data;
    }

    public FileType(String data, String type) {
        this.data = data;
        this.type = type;
    }

    public FileType() {
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
