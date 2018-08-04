package com.weiwobang.paotui.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bean{
    public Comm getComment() {
        return comment;
    }

    public void setComment(Comm comment) {
        this.comment = comment;
    }

    @SerializedName("comment")
    private Comm comment;

    public ReplyUserInfo getReplyUserInfo() {
        return replyUserInfo;
    }

    public void setReplyUserInfo(ReplyUserInfo replyUserInfo) {
        this.replyUserInfo = replyUserInfo;
    }

    @SerializedName("replyUserInfo")
    private ReplyUserInfo replyUserInfo;
    public class Comm{
        @SerializedName("beanList")
        List<Comment> mComments;

        public List<Comment> getComments() {
            return mComments;
        }

        public void setComments(List<Comment> comments) {
            mComments = comments;
        }
    }
    public class ReplyUserInfo{
        private String nickname;
        private String headUri;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadUri() {
            return headUri;
        }

        public void setHeadUri(String headUri) {
            this.headUri = headUri;
        }
    }
}
