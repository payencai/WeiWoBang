package com.weiwobang.paotui.bean;

import java.io.Serializable;

public class OrderAddr implements Serializable {
    private String address;
    private String detail;
    private double lat;
    private double lon;
    private String firceng;
    private String secceng;

    public String getFirceng() {
        return firceng;
    }

    public void setFirceng(String firceng) {
        this.firceng = firceng;
    }

    public String getSecceng() {
        return secceng;
    }

    public void setSecceng(String secceng) {
        this.secceng = secceng;
    }

    public String getAddress() {
        return address;
    }

    public OrderAddr(String address, String detail, double lat, double lon) {
        this.address = address;
        this.detail = detail;
        this.lat = lat;
        this.lon = lon;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

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
}
