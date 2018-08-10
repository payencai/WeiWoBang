package com.weiwobang.paotui.mvp.model;

import com.weiwobang.paotui.bean.Comment;
import com.weiwobang.paotui.bean.News;

import java.util.List;

public interface MvpCallback<T> {
    void loadSuccess(T data);
    void loadError(String error);
    void loadEmpty();
}
