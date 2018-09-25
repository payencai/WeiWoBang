package com.weiwobang.paotui.api;

public class Api {

    public static final String webNewUrl="http://119.23.216.131:8085/";//新版唯我帮接口
    public static final String localNewUrl="http://192.168.3.29:8085/";//新版唯我帮接口
    public static final String localSellerUrl="http://192.168.3.29:8087/";//商家本地接口
    public static final String rootUrl = "http://119.23.216.131:8087/";//商家远程接口
    public static class Message{
        public static final String sPostEdit="message/edit/" ;
        public static final String sPostDel="message/delete/";
        public static final String sGetDetail= "message/getDetail/";
        public static final String sGetToday = "message/getToday/";
        public static final String sGetMine = "message/getMine/";
        public static final String sPostMsgInform = "message/inform/";
        public static final String sPostPublish = "message/publish/";
        public static final String sGetTypeMsg="message/getByType/";
        public static final String sGetBySearch="message/getBySearch/";
    }

    public static class User{
        public static final String sGetCash= "remove_valuation_rule/getCostByDistance";
        public static final String sGetVerifyCode = "bbs_user/getVerificationCode/";
        public static final String sGetUserInfo = "bbs_user/getInformation/";
        public static final String sPostLogin ="bbs_user/login/";
        public static final String sPostRegister="bbs_user/register/";
        public static final String sPostUpdateName="bbs_user/updateName/";
        public static final String sWechatLogin ="bbs_user/loginByWechat/";
        public static final String sBindWechat ="bbs_user/bindTelephone/";
        public static final String sIsBindWechat ="bbs_user/isTelephoneBindWechat/";

        public static final String sPostResetPwd="bbs_user/resetPassword/";
        public static final String sPostUpdateHead="bbs_user/updateHeading/";
        public static final String sPostSugg="problem_report/add/";
        public static final String sUpLoadImg="image/uploadImage/";
        public static final String sUpLoadVideo="image/uploadVideo/";
        public static final String sPostAddSeller = "business_application/add/";
    }
    public static class Comment{
        public static final String sGetComment="comment/getDetail/" ;
        public static final String sPostInform="comment/inform/";
        public static final String sPostAdd= "comment/add/";
        public static final String sPostReply ="comment/reply/";


    }
    public static class Remove{
        public static final String sGetDistance="distance/get/";
        public static final String sPostAdd ="remove/add/";
        public static final String sGetMine ="remove/getMine/";
        public static final String sAddCityOrder = "/city_wide_delivery/add";

    }
    //seller
    public static class Seller {
        public static final String getPesonalCenter = "business/info/getPesonalCenter/";   //获取商家中心
        public static final String getInProcess = "order/business/getInProcess";//获取正在执行的订单
        public static final String getFinished = "order/business/getFinished";//获取已经完成的订单
        public static final String getOrderDetail = "order/business/getOrderDetail";//获取订单详情
        public static final String getHomepage = "order/business/getHomepage/";//主页
        public static final String getNotice = "order/business/getNotice";//获取通知

                           /**------------------分界线------------------*/

        public static final String addName = "business/info/addName";//添加联系人
        public static final String addTel = "business/info/addTel";//添加电话
        public static final String addMap = "business/info/addMap";
        public static final String refuseCancel = "order/business/refuseCancel";//拒绝取消订单
        public static final String agreeCancel = "order/business/agreeCancel";//同意取消订单
        public static final String cancel = "order/business/cancel";//取消订单
        public static final String complain = "order/business/complain";//投诉
        public static final String apply = "business/refund/apply";   //商家提现
        public static final String add = "order/business/add";//下单
        //public static final String addHeadPortraint = "business/info/addHeadPortraint";//post 更改用户头像
    }

}
