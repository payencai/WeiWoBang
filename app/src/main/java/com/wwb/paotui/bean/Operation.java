package com.wwb.paotui.bean;

public class Operation {


    /**
     * operatorId : e8f44e45-fd51-45d9-9008-926dc1977222
     * operatorRoleName : courier
     * operationTime : 2018-09-04 18:03:14
     * operationType : 成员接单
     * orderId : 0f14d09f-03be-4106-a937-233f32c7ccad
     * describe : 接单人 小杜 18219116738
     * extend : {"name":"小杜","telNum":"18219116738"}
     */

    private String operatorId;
    private String operatorRoleName;
    private String operationTime;
    private String operationType;
    private String orderId;
    private String describe;
    private ExtendBean extend;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorRoleName() {
        return operatorRoleName;
    }

    public void setOperatorRoleName(String operatorRoleName) {
        this.operatorRoleName = operatorRoleName;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public ExtendBean getExtend() {
        return extend;
    }

    public void setExtend(ExtendBean extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operatorId='" + operatorId + '\'' +
                ", operatorRoleName='" + operatorRoleName + '\'' +
                ", operationTime='" + operationTime + '\'' +
                ", operationType='" + operationType + '\'' +
                ", orderId='" + orderId + '\'' +
                ", describe='" + describe + '\'' +
                ", extend=" + extend +
                '}';
    }

    public static class ExtendBean {
        /**
         * name : 小杜
         * telNum : 18219116738
         */

        private String name;
        private String telNum;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelNum() {
            return telNum;
        }

        public void setTelNum(String telNum) {
            this.telNum = telNum;
        }
    }

}
