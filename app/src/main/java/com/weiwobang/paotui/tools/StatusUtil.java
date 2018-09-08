package com.weiwobang.paotui.tools;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class StatusUtil {

    /***订单处理日志behavior***/
    public static String getBehaviorName(Integer id){
        String result = "";
        if(id != null)
            switch (id){
                case 0:
                    result = "未知";
                    break;
                case 1:
                    result = "[商家派单]";
                    break;
                case 2:
                    result = "[成员接单]";
                    break;
                case 3:
                    result = "[取货成功]";
                    break;
                case 4:
                    result = "[送货成功]";
                    break;
                case 5:
                    result = "[订单移交]";
                    break;
                case 6:
                    result = "[商家投诉]";
                    break;
                default:
                    result = "";
                    break;
            }
        return result;
    }

    //获取操作日志behavior前缀
    public static String getBehaviorPrefix(Integer id){
        String result = "";
        if(id != null)
            switch (id){
                case 0:
                    result = "未知 ";
                    break;
                case 1:
                    result = "派单人 ";
                    break;
                case 2:
                    result = "接单人 ";
                    break;
                case 3:
                    result = "取货人 ";
                    break;
                case 4:
                    result = "送货人 ";
                    break;
                case 5:
                    result = "移交给 ";
                    break;
                case 6:
                    result = "投诉登记：";
                    break;
                default:
                    result = "";
                    break;
            }
        return result;
    }

    // [跑腿]任务（订单）状态 1待接单 2待取货 3派送中 4已完成 5已取消
    public static String getTaskStatus(String status){
        String result = "";
        int id = 0;
        if(status != null) {
            try {
                id = Integer.parseInt(status);
            }
            catch (Exception ex) {}
        }
        switch (id){
            case 0:
                result = "未知";
                break;
            case 1:
                result = "待接单";
                break;
            case 2:
                result = "待取货";
                break;
            case 3:
                result = "派送中";
                break;
            case 4:
                result = "已完成";
                break;
            case 5:
                result = "已取消";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    // [商家]派单处理状态： 0未作处理 1已接受 2已拒绝 3已过期
    public static String getRunnerNotificationHandleStatus(String status){
        String result = "";
        int id = 1;
        if(status != null) {
            try {
                id = Integer.parseInt(status);
            }
            catch (Exception ex) {}
        }
        switch (id){
            case 0:
                result = "未作处理";
                break;
            case 1:
                result = "已接受";
                break;
            case 2:
                result = "已拒绝";
                break;
            case 3:
                result = "已过期";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    // [商家]派单处理状态： 0未作处理 1已接受 2已拒绝 3已过期
    public static String getDispatchHandleStatus(String status){
        String result = "";
        int id = 1;
        if(status != null) {
            try {
                id = Integer.parseInt(status);
            }
            catch (Exception ex) {}
        }
        switch (id){
            case 1:
                result = "未作处理";
                break;
            case 2:
                result = "已接受";
                break;
            case 3:
                result = "已拒绝";
                break;
            case 4:
                result = "已过期";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    // 通知类型：1 自动分配 2后台管理分配 3移交
    public static String getNotificationType(String type){
        String result = "";
        int id = 0;
        if(type != null) {
            try {
                id = Integer.parseInt(type);
            }
            catch (Exception ex) {}
        }
        switch (id){
            case 0:
                result = "未知";
                break;
            case 1:
                result = "自动分配";
                break;
            case 2:
                result = "后台指派";
                break;
            case 3:
                result = "移交";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
}
