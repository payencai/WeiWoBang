package com.wwb.paotui.mvp.model;

import com.wwb.paotui.bean.Comment;
import com.wwb.paotui.bean.News;

import java.util.List;

public interface MvpCallback<T> {
    void loadSuccess(T data);
    void loadError(String error);
    void loadEmpty();
}
