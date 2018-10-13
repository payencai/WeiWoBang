package com.wwb.paotui.bean;

public class Car {
    private String name;
    private String size;
    private String weight;
    private String volume;
    private int resId;

    public Car(String name, String size, String weight, String volume, int resId) {
        this.name = name;
        this.size = size;
        this.weight = weight;
        this.volume = volume;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
