package com.wwb.paotui.bean;

public class RunnerModel {


    /**
     * id : 5bee9fc86f764c6fb732e06cb234a64e
     * trackingNumber : 15391622060435808
     * buyerName : 哈哈哈
     * buyerTelnum : 13258866544
     * nameFrom : 送到
     * telnumFrom : 13202288808
     * distance : 0
     * commissionCalculate : 0.01
     * courierName : null
     * courierTelNum : null
     * state : 1
     * createTime : 2018-10-10 17:03:26
     * arriveTime : null
     * ordertakingTime : null
     * minute : null
     * deliverMap : {"id":null,"longitude":"113.391073","latitude":"23.049913","province":null,"city":null,"district":null,"street":null,"adress":"国医西路与大学城内环西路交叉口东北200米中心湖公园西门","spare1":"不好好回家","spare2":null}
     * pickMap : null
     */
    private String goodsName;
    private String note;

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

    private String id;
    private String trackingNumber;
    private String buyerName;
    private String buyerTelnum;
    private String nameFrom;
    private String telnumFrom;
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
    private DeliverMapBean pickMap;

    public String getId() {
        return id;
    }

    public DeliverMapBean getPickMap() {
        return pickMap;
    }

    public void setPickMap(DeliverMapBean pickMap) {
        this.pickMap = pickMap;
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

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public String getTelnumFrom() {
        return telnumFrom;
    }

    public void setTelnumFrom(String telnumFrom) {
        this.telnumFrom = telnumFrom;
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

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getOrdertakingTime() {
        return ordertakingTime;
    }

    public void setOrdertakingTime(String ordertakingTime) {
        this.ordertakingTime = ordertakingTime;
    }

    public String getMinute() {
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
         * id : null
         * longitude : 113.391073
         * latitude : 23.049913
         * province : null
         * city : null
         * district : null
         * street : null
         * adress : 国医西路与大学城内环西路交叉口东北200米中心湖公园西门
         * spare1 : 不好好回家
         * spare2 : null
         */

        private String id;
        private String longitude;
        private String latitude;
        private String province;
        private String city;
        private String district;
        private String street;
        private String adress;
        private String spare1;
        private String spare2;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getSpare1() {
            return spare1;
        }

        public void setSpare1(String spare1) {
            this.spare1 = spare1;
        }

        public String getSpare2() {
            return spare2;
        }

        public void setSpare2(String spare2) {
            this.spare2 = spare2;
        }
    }
}
