package com.wwb.paotui.bean;

import android.graphics.Bitmap;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class News extends LitePalSupport implements Serializable{


    /**
     * bbsUserId : string
     * categoryId : string
     * categoryName : string
     * commentNum : 0
     * contactInfomation : string
     * content : string
     * createTime : 2018-07-31T10:01:03.253Z
     * id : string
     * image1 : string
     * image1Uri : string
     * image2 : string
     * image2Uri : string
     * image3 : string
     * image3Uri : string
     * image4 : string
     * image4Uri : string
     * image5 : string
     * image5Uri : string
     * image6 : string
     * image6Uri : string
     * isDeleted : 0
     * linkman : string
     * readNum : 0
     * title : string
     * updateTime : 2018-07-31T10:01:03.254Z
     */

    private String image1Type;
    private String image2Type;
    private String image3Type;
    private String image4Type;
    private String image5Type;
    private String image6Type;
    private String bbsUserId;
    private String categoryId;
    private String categoryName;
    private int commentNum;
    private String contactInfomation;
    private String content;
    private String createTime;
    private String id;
    private String image1;
    private String image1Uri;
    private String image2;
    private String image2Uri;
    private String image3;
    private String image3Uri;
    private String image4;
    private String image4Uri;
    private String image5;
    private String image5Uri;
    private String image6;
    private String image6Uri;
    private int isDeleted;
    private String linkman;
    private int readNum;
    private String title;
    private String updateTime;
    private Bitmap video1Img;
    private Bitmap video2Img;
    private Bitmap video3Img;
    private Bitmap video4Img;

    public Bitmap getVideo1Img() {
        return video1Img;
    }

    public void setVideo1Img(Bitmap video1Img) {
        this.video1Img = video1Img;
    }

    public Bitmap getVideo2Img() {
        return video2Img;
    }

    public void setVideo2Img(Bitmap video2Img) {
        this.video2Img = video2Img;
    }

    public Bitmap getVideo3Img() {
        return video3Img;
    }

    public void setVideo3Img(Bitmap video3Img) {
        this.video3Img = video3Img;
    }

    public Bitmap getVideo4Img() {
        return video4Img;
    }

    public void setVideo4Img(Bitmap video4Img) {
        this.video4Img = video4Img;
    }

    public Bitmap getVideo5Img() {
        return video5Img;
    }

    public void setVideo5Img(Bitmap video5Img) {
        this.video5Img = video5Img;
    }

    public Bitmap getVideo6Img() {
        return video6Img;
    }

    public void setVideo6Img(Bitmap video6Img) {
        this.video6Img = video6Img;
    }

    private Bitmap video5Img;
    private Bitmap video6Img;
    public String getImage1Type() {
        return image1Type;
    }

    public void setImage1Type(String image1Type) {
        this.image1Type = image1Type;
    }

    public String getImage2Type() {
        return image2Type;
    }

    public void setImage2Type(String image2Type) {
        this.image2Type = image2Type;
    }

    public String getImage3Type() {
        return image3Type;
    }

    public void setImage3Type(String image3Type) {
        this.image3Type = image3Type;
    }

    public String getImage4Type() {
        return image4Type;
    }

    public void setImage4Type(String image4Type) {
        this.image4Type = image4Type;
    }

    public String getImage5Type() {
        return image5Type;
    }

    public void setImage5Type(String image5Type) {
        this.image5Type = image5Type;
    }

    public String getImage6Type() {
        return image6Type;
    }

    public void setImage6Type(String image6Type) {
        this.image6Type = image6Type;
    }


    public String getBbsUserId() {
        return bbsUserId;
    }

    public void setBbsUserId(String bbsUserId) {
        this.bbsUserId = bbsUserId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getContactInfomation() {
        return contactInfomation;
    }

    public void setContactInfomation(String contactInfomation) {
        this.contactInfomation = contactInfomation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage1Uri() {
        return image1Uri;
    }

    public void setImage1Uri(String image1Uri) {
        this.image1Uri = image1Uri;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage2Uri() {
        return image2Uri;
    }

    public void setImage2Uri(String image2Uri) {
        this.image2Uri = image2Uri;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage3Uri() {
        return image3Uri;
    }

    public void setImage3Uri(String image3Uri) {
        this.image3Uri = image3Uri;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage4Uri() {
        return image4Uri;
    }

    public void setImage4Uri(String image4Uri) {
        this.image4Uri = image4Uri;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage5Uri() {
        return image5Uri;
    }

    public void setImage5Uri(String image5Uri) {
        this.image5Uri = image5Uri;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getImage6Uri() {
        return image6Uri;
    }

    public void setImage6Uri(String image6Uri) {
        this.image6Uri = image6Uri;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "News{" +
                "image1Type='" + image1Type + '\'' +
                ", image2Type='" + image2Type + '\'' +
                ", image3Type='" + image3Type + '\'' +
                ", image4Type='" + image4Type + '\'' +
                ", image5Type='" + image5Type + '\'' +
                ", image6Type='" + image6Type + '\'' +
                ", bbsUserId='" + bbsUserId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", commentNum=" + commentNum +
                ", contactInfomation='" + contactInfomation + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", id='" + id + '\'' +
                ", image1='" + image1 + '\'' +
                ", image1Uri='" + image1Uri + '\'' +
                ", image2='" + image2 + '\'' +
                ", image2Uri='" + image2Uri + '\'' +
                ", image3='" + image3 + '\'' +
                ", image3Uri='" + image3Uri + '\'' +
                ", image4='" + image4 + '\'' +
                ", image4Uri='" + image4Uri + '\'' +
                ", image5='" + image5 + '\'' +
                ", image5Uri='" + image5Uri + '\'' +
                ", image6='" + image6 + '\'' +
                ", image6Uri='" + image6Uri + '\'' +
                ", isDeleted=" + isDeleted +
                ", linkman='" + linkman + '\'' +
                ", readNum=" + readNum +
                ", title='" + title + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", video1Img=" + video1Img +
                ", video2Img=" + video2Img +
                ", video3Img=" + video3Img +
                ", video4Img=" + video4Img +
                ", video5Img=" + video5Img +
                ", video6Img=" + video6Img +
                '}';
    }
}
