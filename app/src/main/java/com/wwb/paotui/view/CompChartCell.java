package com.wwb.paotui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wwb.paotui.R;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class CompChartCell extends FrameLayout {
    public enum CompChartCellModle{

        limitless (0), limited(1), partLimited(2); // textview长度限制

        private final int value;

        public int getValue() {
            return value;
        }

        CompChartCellModle(int value){
            this.value = value;
        }
    }
    public class ViewHolder {
        public ImageView mImgIcon;
        public LinearLayout mLabelLy;
        public TextView mTextName;
        public TextView mTextDescription;
        public TextView mLabelPerson;
        public TextView mLabelTel;
        public TextView mAddressType;
    }
    public ViewHolder viewHolder = new ViewHolder();
    private CompChartCellModle modle;
//    private LinearLayout mLeftAndCenterLayout;
//    private TextView mTextName, mTextDescription, mLabelPerson, mLabelTel;
//    private View mDividingLine;
//    private ViewGroup mLabelLy;
    private CompChartCellListener mCompChartCellListener;

    public CompChartCell(Context context) {
        super(context);
        init();
    }

    public CompChartCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setupViewsWithAttrs(context, attrs);
    }

    public CompChartCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setupViewsWithAttrs(context, attrs);
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.comp_chart_cell, this, true);
//        mLeftAndCenterLayout = (LinearLayout) view.findViewById(R.id.CompChartCell_mLeftAndCenterLayout);
//        mCenterLayout = (LinearLayout) view.findViewById(R.id.CompChartCell_mCenterLayout);
        viewHolder.mImgIcon = (ImageView) view.findViewById(R.id.CompChartCell_mImgIcon);
        viewHolder.mAddressType = (TextView) view.findViewById(R.id.CompChartCell_mAddressType);
        viewHolder.mTextName = (TextView) view.findViewById(R.id.CompChartCell_mTextName);
        viewHolder.mTextDescription = (TextView) view.findViewById(R.id.CompChartCell_mTextDescription);
        viewHolder.mLabelPerson = (TextView) view.findViewById(R.id.CompChartCell_mLabelPerson);
        viewHolder.mLabelTel = (TextView) view.findViewById(R.id.CompChartCell_mLabelTel);
        viewHolder.mLabelLy = (LinearLayout) view.findViewById(R.id.CompChartCell_mLabelLy);
    }

    private void setupViewsWithAttrs(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompChartCell);
        int modle = a.getInt(R.styleable.CompChartCell_chartCellModle, 0);
        setMode(modle);
    }

    public void setMode(int mode) {
        if (mode == CompChartCellModle.limited.getValue()) {
            viewHolder.mTextName.setSingleLine(true);
            viewHolder.mTextName.setEllipsize(TextUtils.TruncateAt.END);
            viewHolder.mTextDescription.setSingleLine();
            viewHolder.mTextDescription.setEllipsize(TextUtils.TruncateAt.END);
        } else if (mode == CompChartCellModle.partLimited.getValue()) {
            viewHolder.mTextName.setSingleLine(true);
            viewHolder.mTextName.setEllipsize(TextUtils.TruncateAt.END);
        } else if(mode == CompChartCellModle.limitless.getValue())
            viewHolder.mTextName.setSingleLine(false);
    }

    public interface CompChartCellListener{

        public void onItemClick(CompChartCell compChartCell);

        public void onMoreButtonClick(CompChartCell compChartCell);

    }
}
