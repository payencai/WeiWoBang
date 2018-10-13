package com.wwb.paotui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wwb.paotui.R;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class CompCourierCell extends FrameLayout {
    public class ViewHolder {
//        public ImageView mImgIcon;
//        public LinearLayout mLabelLy;
        public TextView mTextName;
        public TextView mTextDescription;
//        public TextView mLabelPerson;
//        public TextView mLabelTel;
//        public TextView mAddressType;
    }
    public ViewHolder viewHolder = new ViewHolder();
//    private LinearLayout mLeftAndCenterLayout;
//    private TextView mTextName, mTextDescription, mLabelPerson, mLabelTel;
//    private View mDividingLine;
//    private ViewGroup mLabelLy;
//    private CompChartCellListener mCompChartCellListener;

    public CompCourierCell(Context context) {
        super(context);
        init();
    }

    public CompCourierCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //setupViewsWithAttrs(context, attrs);
    }

    public CompCourierCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        //setupViewsWithAttrs(context, attrs);
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.comp_courier_cell, this, true);
//        mLeftAndCenterLayout = (LinearLayout) view.findViewById(R.id.CompChartCell_mLeftAndCenterLayout);
//        mCenterLayout = (LinearLayout) view.findViewById(R.id.CompChartCell_mCenterLayout);
//        viewHolder.mImgIcon = (ImageView) view.findViewById(R.id.CompChartCell_mImgIcon);
//        viewHolder.mAddressType = (TextView) view.findViewById(R.id.CompChartCell_mAddressType);
        viewHolder.mTextName = (TextView) view.findViewById(R.id.CompChartCell_mTextName);
        viewHolder.mTextDescription = (TextView) view.findViewById(R.id.CompChartCell_mTextDescription);
//        viewHolder.mLabelPerson = (TextView) view.findViewById(R.id.CompChartCell_mLabelPerson);
//        viewHolder.mLabelTel = (TextView) view.findViewById(R.id.CompChartCell_mLabelTel);
//        viewHolder.mLabelLy = (LinearLayout) view.findViewById(R.id.CompChartCell_mLabelLy);
    }

    public void setData() {
    }

    public interface CompChartCellListener{

        public void onItemClick(CompCourierCell compChartCell);

    }
}
