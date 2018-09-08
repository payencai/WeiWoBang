package com.weiwobang.paotui.bean;

import java.io.Serializable;

public class Userinfo implements Serializable{

    /**
     * account : string
     * activeTime : 2018-07-31T09:56:52.384Z
     * createTime : 2018-07-31T09:56:52.384Z
     * heading : string
     * headingUri : string
     * id : string
     * isBanned : string
     * isDeleted : string
     * lastPasswordResetDate : 2018-07-31T09:56:52.384Z
     * nickname : string
     * openId : string
     * pushAlias : string
     * systemId : string
     * token : string
     */

    private String account;
    private String activeTime;
    private String createTime;
    private String heading;
    private String headingUri;
    private String id;
    private String isBanned;
    private String isDeleted;
    private String lastPasswordResetDate;
    private String nickname;
    private String openId;
    private String pushAlias;
    private String systemId;
    private String token;
    private String isBusiness;
    private String businessToken;
    public String getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(String isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getBusinessToken() {
        return businessToken;
    }

    public void setBusinessToken(String businessToken) {
        this.businessToken = businessToken;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
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

    public String getHeadingUri() {
        return headingUri;
    }

    public void setHeadingUri(String headingUri) {
        this.headingUri = headingUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(String isBanned) {
        this.isBanned = isBanned;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(String lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPushAlias() {
        return pushAlias;
    }

    public void setPushAlias(String pushAlias) {
        this.pushAlias = pushAlias;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
