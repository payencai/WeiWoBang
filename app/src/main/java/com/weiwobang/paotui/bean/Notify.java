package com.weiwobang.paotui.bean;

public class Notify {

    /**
     * id : 067c5beb-fd2b-480d-a9e6-a27ddd72c90e
     * businessName : 麻辣烫
     * businessTelnum : 12321312345
     * courierName : 蒋琬
     * courierTelnum : 18256398984
     * buyerName : 141001
     * buyerTelnum : 13800000000
     * distance : 4.01
     * commissionCalculate : 10.32
     * createTime : 2018-01-04 11:07:16
     * state : 1
     * trackingNumber : 15150313289581521
     * deliverMap : {"longitude":"113.35777","latitude":"23.060234","adress":"华工","heading":null}
     */

    private String id;
    private String businessName;
    private String businessTelnum;
    private String courierName;
    private String courierTelnum;
    private String buyerName;
    private String buyerTelnum;
    private double distance;
    private double commissionCalculate;
    private String createTime;
    private String state;
    private String trackingNumber;
    private DeliverMapBean deliverMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessTelnum() {
        return businessTelnum;
    }

    public void setBusinessTelnum(String businessTelnum) {
        this.businessTelnum = businessTelnum;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierTelnum() {
        return courierTelnum;
    }

    public void setCourierTelnum(String courierTelnum) {
        this.courierTelnum = courierTelnum;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTelnum() {
        return buyerTelnum;
    }

    public void setBuyerTelnum(String buyerTelnum) {
        this.buyerTelnum = buyerTelnum;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCommissionCalculate() {
        return commissionCalculate;
    }

    public void setCommissionCalculate(double commissionCalculate) {
        this.commissionCalculate = commissionCalculate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public DeliverMapBean getDeliverMap() {
        return deliverMap;
    }

    public void setDeliverMap(DeliverMapBean deliverMap) {
        this.deliverMap = deliverMap;
    }

    public static class DeliverMapBean {
        /**
         * longitude : 113.35777
         * latitude : 23.060234
         * adress : 华工
         * heading : null
         */

        private String longitude;
        private String latitude;
        private String adress;
        private String heading;

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }
    }
}
