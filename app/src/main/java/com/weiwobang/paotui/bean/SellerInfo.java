package com.weiwobang.paotui.bean;

public class SellerInfo {


    /**
     * resultCode : 0
     * message : null
     * data : {"data":{"businessName":"飘风的小店","businessId":"3f017408af1c418eb6451294ea1c9cd0","headPortraintUrl":null,"balance":1299997.54,"no":"S00003"},"tel":{"telNumber":"13202908144","name":"赵云"},"map":{"longitude":"113.37869","latitude":"22.94078","adress":"广东省广州市番禺区平康路与石山大街一巷交汇处","heading":"沙墟市场"}}
     */

    private int resultCode;
    private String message;
    private DataBeanX data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * data : {"businessName":"飘风的小店","businessId":"3f017408af1c418eb6451294ea1c9cd0","headPortraintUrl":null,"balance":1299997.54,"no":"S00003"}
         * tel : {"telNumber":"13202908144","name":"赵云"}
         * map : {"longitude":"113.37869","latitude":"22.94078","adress":"广东省广州市番禺区平康路与石山大街一巷交汇处","heading":"沙墟市场"}
         */

        private DataBean data;
        private TelBean tel;
        private MapBean map;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public TelBean getTel() {
            return tel;
        }

        public void setTel(TelBean tel) {
            this.tel = tel;
        }

        public MapBean getMap() {
            return map;
        }

        public void setMap(MapBean map) {
            this.map = map;
        }

        public static class DataBean {
            /**
             * businessName : 飘风的小店
             * businessId : 3f017408af1c418eb6451294ea1c9cd0
             * headPortraintUrl : null
             * balance : 1299997.54
             * no : S00003
             */

            private String businessName;
            private String businessId;
            private Object headPortraintUrl;
            private double balance;
            private String no;

            public String getBusinessName() {
                return businessName;
            }

            public void setBusinessName(String businessName) {
                this.businessName = businessName;
            }

            public String getBusinessId() {
                return businessId;
            }

            public void setBusinessId(String businessId) {
                this.businessId = businessId;
            }

            public Object getHeadPortraintUrl() {
                return headPortraintUrl;
            }

            public void setHeadPortraintUrl(Object headPortraintUrl) {
                this.headPortraintUrl = headPortraintUrl;
            }

            public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }
        }

        public static class TelBean {
            /**
             * telNumber : 13202908144
             * name : 赵云
             */

            private String telNumber;
            private String name;

            public String getTelNumber() {
                return telNumber;
            }

            public void setTelNumber(String telNumber) {
                this.telNumber = telNumber;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class MapBean {
            /**
             * longitude : 113.37869
             * latitude : 22.94078
             * adress : 广东省广州市番禺区平康路与石山大街一巷交汇处
             * heading : 沙墟市场
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
}
