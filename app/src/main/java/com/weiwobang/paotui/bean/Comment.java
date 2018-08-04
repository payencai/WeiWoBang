package com.weiwobang.paotui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Comment implements Serializable{

    /**
     * id : 72794983-6a18-4f48-9d2c-66636e3101ae
     * messageId : 25359817-9353-4494-8502-ee2f8e50f337
     * commentUserId : 54f82e54-9e5c-4240-a5f1-436e09782945
     * commentUserHeading : bbs/2018080120003850
     * commentUserHeadingUri : http://paotui.oss-cn-shenzhen.aliyuncs.com/bbs/2018080120003850?Expires=1533200593&OSSAccessKeyId=LTAIf8Qhga9o9zWP&Signature=PSKU7iFyBmUxnwq2oKypSqMaIDE%3D
     * commentUserNickname : 你好
     * commentContent : 嘻嘻嘻嘻
     * commentTime : 2018-08-01 21:13:15
     * commentIsDeleted : 1
     */

    private String id;
    private String messageId;
    private String commentUserId;
    private String commentUserHeading;
    private String commentUserHeadingUri;
    private String commentUserNickname;
    private String commentContent;
    private String commentTime;
    private int commentIsDeleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String name;
    private String url;
    @SerializedName("replyList")
    private List<Reply> replyList;

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserHeading() {
        return commentUserHeading;
    }

    public void setCommentUserHeading(String commentUserHeading) {
        this.commentUserHeading = commentUserHeading;
    }

    public String getCommentUserHeadingUri() {
        return commentUserHeadingUri;
    }

    public void setCommentUserHeadingUri(String commentUserHeadingUri) {
        this.commentUserHeadingUri = commentUserHeadingUri;
    }

    public String getCommentUserNickname() {
        return commentUserNickname;
    }

    public void setCommentUserNickname(String commentUserNickname) {
        this.commentUserNickname = commentUserNickname;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getCommentIsDeleted() {
        return commentIsDeleted;
    }

    public void setCommentIsDeleted(int commentIsDeleted) {
        this.commentIsDeleted = commentIsDeleted;
    }
}
