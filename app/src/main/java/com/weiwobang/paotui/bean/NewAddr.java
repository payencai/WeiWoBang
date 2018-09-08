package com.weiwobang.paotui.bean;

import java.io.Serializable;

public class NewAddr implements Serializable{
    private String name;
    private String address;
    private double lat;
    private double lon;
    private String contact;
    private String phone;
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public NewAddr(String name, String address, double lat, double lon, String contact, String phone) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.contact = contact;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
