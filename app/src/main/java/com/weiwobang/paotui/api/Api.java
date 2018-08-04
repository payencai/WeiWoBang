package com.weiwobang.paotui.api;

public class Api {
    public static final String testUrl="http://119.23.216.131:8083/image/uploadImage";
    public static final String baseUrl="http://119.23.216.131:8083/";
    public static final String rootUrl="http://www.wewobang.com/delivery/";
    public static class Login {
        public static final String sLogin ="login/";
        public static final String sFullLogin =rootUrl+"login";

    }
    public static class Message{
        public static final String sPostEdit="message/edit/" ;
        public static final String sPostDel="message/delete/";
        public static final String sGetDetail= "message/getDetail/";
        public static final String sGetToday = "message/getToday/";
        public static final String sGetMine = "message/getMine/";
        public static final String sPostMsgInform = "message/inform/";
        public static final String sPostPublish = "message/publish/";
        public static final String sGetTypeMsg="message/getByType/";
    }
//    public static class Logout{
//        public static final String sLogout ="logout/";
//    }
//    public static class Runner {
//        public static final String sHead ="manage/courier/";
//
//    }
//    public static class Seller {
//        public static final String sHead ="manage/business/";
//
//    }
    public static class User{
        public static final String sGetVerifyCode = "bbs_user/getVerificationCode/";
        public static final String sGetUserInfo = "bbs_user/getInformation/";
        public static final String sPostLogin ="bbs_user/login/";
        public static final String sPostRegister="bbs_user/register/";
        public static final String sPostUpdateName="bbs_user/updateName/";
        public static final String sPostResetPwd="bbs_user/resetPassword/";
        public static final String sPostUpdateHead="bbs_user/updateHeading/";
        public static final String sPostSugg="problem_report/add/";
        public static final String sUpLoadImg="image/uploadImage";
    }
    public static class Comment{
        public static final String sGetComment="comment/getDetail/" ;
        public static final String sPostInform="comment/inform/";
        public static final String sPostAdd= "comment/add/";
        public static final String sPostReply ="comment/reply/";


    }
}
