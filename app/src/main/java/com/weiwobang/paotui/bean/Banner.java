package com.weiwobang.paotui.bean;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class Banner {
    private int imageUrl;
    private String pic;

    public Banner() {
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public Banner(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}
