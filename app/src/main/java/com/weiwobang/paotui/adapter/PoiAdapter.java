package com.weiwobang.paotui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.PoiBean;


import java.util.List;

public class PoiAdapter extends ArrayAdapter<PoiBean>{
    private Context mContext;
    private int resId;
    public PoiAdapter(@NonNull Context context, int resource, @NonNull List<PoiBean> objects) {
        super(context, resource, objects);
        mContext=context;
        resId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PoiBean poiBean=getItem(position);
        ViewHolder viewHolder;
        View view;
        if (convertView == null){
            // inflate出子项布局，实例化其中的图片控件和文本控件
            view = LayoutInflater.from(getContext()).inflate(resId, null);
            viewHolder = new ViewHolder();
            // 通过id得到图片控件实例
            viewHolder.name = (TextView) view.findViewById(R.id.wwb_poi_name);
            // 通过id得到文本空间实例
            viewHolder.address = (TextView) view.findViewById(R.id.wwb_poi_address);
            // 缓存图片控件和文本控件的实例
            view.setTag(viewHolder);
        }else{
            view = convertView;
            // 取出缓存
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.name.setTextColor(getContext().getResources().getColor(R.color.color_333));
        }
        if (poiBean.isIsupdateColor()){
            viewHolder.name.setTextColor(getContext().getResources().getColor(R.color.red_f7a));
        }
        // 直接使用缓存中的图片控件和文本控件的实例
        // 图片控件设置图片资源

        viewHolder.name.setText(poiBean.getName());
        // 文本控件设置文本内容
        viewHolder.address.setText(poiBean.getAddress());
        return view;
    }
    class ViewHolder{
        TextView name;
        TextView address;
    }
}
