package com.wwb.paotui.bean;

public class SellerOrder {

    /**
     * id : d38c5aab-f169-4e05-9443-575c2f3c690a
     * trackingNumber : 15355934222257350
     * buyerName : 李白
     * buyerTelnum : 13202908144
     * distance : 2.82
     * commissionCalculate : 10.46
     * courierName : null
     * courierTelNum : null
     * state : 1
     * identifyingCode : 9453
     * deliverMap : {"longitude":"113.37783","latitude":"22.95871","adress":"广东省广州市番禺区甘棠大道18号","heading":"甘棠幼儿园"}
     */

    private String id;
    private String trackingNumber;
    private String buyerName;
    private String buyerTelnum;
    private double distance;
    private double commissionCalculate;
    private String courierName;
    private String courierTelNum;
    private String state;
    private String goodsName;
    private String note;
    private String identifyingCode;
    private DeliverMapBean deliverMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
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

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierTelNum() {
        return courierTelNum;
    }

    public void setCourierTelNum(String courierTelNum) {
        this.courierTelNum = courierTelNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdentifyingCode() {
        return identifyingCode;
    }

    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public DeliverMapBean getDeliverMap() {
        return deliverMap;
    }

    public void setDeliverMap(DeliverMapBean deliverMap) {
        this.deliverMap = deliverMap;
    }

    public static class DeliverMapBean {
        /**
         * longitude : 113.37783
         * latitude : 22.95871
         * adress : 广东省广州市番禺区甘棠大道18号
         * heading : 甘棠幼儿园
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
