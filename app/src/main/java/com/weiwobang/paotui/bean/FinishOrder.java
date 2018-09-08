package com.weiwobang.paotui.bean;

public class FinishOrder {

    /**
     * id : d38c5aab-f169-4e05-9443-575c2f3c690a
     * trackingNumber : 15355934222257350
     * buyerName : 李白
     * buyerTelnum : 13202908144
     * distance : 2.82
     * commissionCalculate : 10.46
     * courierName : null
     * courierTelNum : null
     * state : 5
     * createTime : 2018-08-30 09:43:42
     * arriveTime : null
     * ordertakingTime : null
     * minute : null
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
    private String createTime;
    private String arriveTime;
    private String ordertakingTime;
    private String minute;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Object getOrdertakingTime() {
        return ordertakingTime;
    }

    public void setOrdertakingTime(String ordertakingTime) {
        this.ordertakingTime = ordertakingTime;
    }

    public Object getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
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
