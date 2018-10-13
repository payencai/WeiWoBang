package com.wwb.paotui.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

public class MySection extends SectionEntity<FinishOrder> {
    private boolean isMore;
    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }


    public MySection(FinishOrder o) {
        super(o);
    }
}
