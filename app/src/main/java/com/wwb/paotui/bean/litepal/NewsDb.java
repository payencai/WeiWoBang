package com.wwb.paotui.bean.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class NewsDb extends LitePalSupport  {
    private String title;
    @Column(unique = true, defaultValue = "unknown")
    private String newsId;
    private String content;
    private int commentNum;
    private int readNum;
    private String url;

    @Override
    public String toString() {
        return "NewsDb{" +
                "title='" + title + '\'' +

                ", content='" + content + '\'' +
                ", commentNum=" + commentNum +
                ", readNum=" + readNum +
                ", url='" + url + '\'' +
                ", type=" + type +
                '}';
    }

    private int type; //0图片1视频;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
