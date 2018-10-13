package com.wwb.paotui.bean;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable{

    /**
     * id : 5f9094dd-5b34-4360-bb44-d68c276aa918
     * orderNum : 15337940072140912
     * bbsUserId : df8ab3de-4bbd-4330-920a-0654561ab746
     * bbsNickname : 李白
     * createTime : 2018-08-09 13:53:27
     * updateTime : 2018-08-09 13:53:27
     * longitudeFrom : 113.39126527041779
     * latitudeFrom : 23.055145184822212
     * addressFrom : 广州大学城体育中心综合办公楼大学城内环东路218号大学城体育中心
     * addressFromDetail : 哈哈哈
     * floorFrom : 无电梯二层
     * longitudeTo : 113.37302490832406
     * latitudeTo : 23.052688291752347
     * addressTo : 番禺YH城外环西路368号
     * addressToDetail : 呵呵红红火火
     * floorTo : 无电梯三层
     * distance : 4.35
     * telephoneNum : 13202908144
     * originalRemoveTime : 2018-11-01 00:00:00
     * removeTime : 2018-11-01 00:00:00
     * note : 滚滚滚哈哈哈
     * status : 1
     * bookSuccessfullyTime : null
     * completeTime : null
     * cancelTime : null
     * isDeleted : 1
     */
    private String contactName;
    private double cost;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setBookSuccessfullyTime(String bookSuccessfullyTime) {
        this.bookSuccessfullyTime = bookSuccessfullyTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    private String id;
    private String orderNum;
    private String bbsUserId;
    private String bbsNickname;
    private String createTime;
    private String updateTime;
    private double longitudeFrom;
    private double latitudeFrom;
    private String addressFrom;
    private String addressFromDetail;
    private String floorFrom;
    private double longitudeTo;
    private double latitudeTo;
    private String addressTo;
    private String addressToDetail;
    private String floorTo;
    private double distance;
    private String telephoneNum;
    private String originalRemoveTime;
    private String removeTime;
    private String note;
    private String status;
    private String bookSuccessfullyTime;
    private String completeTime;
    private String cancelTime;
    private String isDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getBbsUserId() {
        return bbsUserId;
    }

    public void setBbsUserId(String bbsUserId) {
        this.bbsUserId = bbsUserId;
    }

    public String getBbsNickname() {
        return bbsNickname;
    }

    public void setBbsNickname(String bbsNickname) {
        this.bbsNickname = bbsNickname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public double getLongitudeFrom() {
        return longitudeFrom;
    }

    public void setLongitudeFrom(double longitudeFrom) {
        this.longitudeFrom = longitudeFrom;
    }

    public double getLatitudeFrom() {
        return latitudeFrom;
    }

    public void setLatitudeFrom(double latitudeFrom) {
        this.latitudeFrom = latitudeFrom;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressFromDetail() {
        return addressFromDetail;
    }

    public void setAddressFromDetail(String addressFromDetail) {
        this.addressFromDetail = addressFromDetail;
    }

    public String getFloorFrom() {
        return floorFrom;
    }

    public void setFloorFrom(String floorFrom) {
        this.floorFrom = floorFrom;
    }

    public double getLongitudeTo() {
        return longitudeTo;
    }

    public void setLongitudeTo(double longitudeTo) {
        this.longitudeTo = longitudeTo;
    }

    public double getLatitudeTo() {
        return latitudeTo;
    }

    public void setLatitudeTo(double latitudeTo) {
        this.latitudeTo = latitudeTo;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getAddressToDetail() {
        return addressToDetail;
    }

    public void setAddressToDetail(String addressToDetail) {
        this.addressToDetail = addressToDetail;
    }

    public String getFloorTo() {
        return floorTo;
    }

    public void setFloorTo(String floorTo) {
        this.floorTo = floorTo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getOriginalRemoveTime() {
        return originalRemoveTime;
    }

    public void setOriginalRemoveTime(String originalRemoveTime) {
        this.originalRemoveTime = originalRemoveTime;
    }

    public String getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(String removeTime) {
        this.removeTime = removeTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBookSuccessfullyTime() {
        return bookSuccessfullyTime;
    }



    public Object getCompleteTime() {
        return completeTime;
    }



    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
