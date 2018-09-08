package com.weiwobang.paotui.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class Module<T> {
    private String moduleId;

    private List<T> list = new ArrayList<>();

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
