package com.weiwobang.paotui.bean;

public class PoiBean {
    private String address;
    private double lat;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    private double lon;
    private String name;

    private boolean isupdateColor=false;

    public boolean isIsupdateColor() {
        return isupdateColor;
    }

    public void setIsupdateColor(boolean isupdateColor) {
        this.isupdateColor = isupdateColor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
