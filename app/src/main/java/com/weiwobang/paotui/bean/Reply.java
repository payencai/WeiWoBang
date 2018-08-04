package com.weiwobang.paotui.bean;

import java.io.Serializable;

public class Reply implements Serializable{

    /**
     * id : 30e96650-f202-4634-ae2d-eb2ac4337b4b
     * commentId : 72794983-6a18-4f48-9d2c-66636e3101ae
     * messageId : 25359817-9353-4494-8502-ee2f8e50f337
     * replyContent : 都加
     * replyUserId : 54f82e54-9e5c-4240-a5f1-436e09782945
     * replyTime : 2018-08-02 10:32:43
     * replyIsDeleted : 1
     */

    private String id;
    private String commentId;
    private String messageId;
    private String replyContent;
    private String replyUserId;
    private String replyTime;
    private String name;

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

    private String url;
    private int replyIsDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public int getReplyIsDeleted() {
        return replyIsDeleted;
    }

    public void setReplyIsDeleted(int replyIsDeleted) {
        this.replyIsDeleted = replyIsDeleted;
    }
}
