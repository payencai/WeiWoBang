package com.wwb.paotui.bean;

public class LocateBean {

    @Override
    public String toString() {
        return "LocateBean{" +
                "adress='" + adress + '\'' +
                ", bbsUserId='" + bbsUserId + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", createTime='" + createTime + '\'' +
                ", heading='" + heading + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", num=" + num +
                '}';
    }

    /**
     * adress : string
     * bbsUserId : string
     * contactName : string
     * contactNumber : string
     * createTime : 2018-10-11T07:39:12.279Z
     * heading : string
     * id : string
     * latitude : string
     * longitude : string
     * num : 0
     */

    private String adress;
    private String bbsUserId;
    private String contactName;
    private String contactNumber;
    private String createTime;
    private String heading;
    private String id;
    private String latitude;
    private String longitude;
    private int num;

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBbsUserId() {
        return bbsUserId;
    }

    public void setBbsUserId(String bbsUserId) {
        this.bbsUserId = bbsUserId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
