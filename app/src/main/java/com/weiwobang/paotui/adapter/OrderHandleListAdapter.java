package com.weiwobang.paotui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.Operation;

import java.util.List;

public class OrderHandleListAdapter extends BaseAdapter {
    private Context context;
    private List<Operation> lists;

    public OrderHandleListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = new viewHolder();
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_handle_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.item1 = (TextView) convertView.findViewById(R.id.item1);
            holder.item2 = (TextView) convertView.findViewById(R.id.item2);
            //holder.item3 = (TextView) convertView.findViewById(R.id.item3);
            convertView.setTag(holder);
        }else{
            holder = (viewHolder) convertView.getTag();
        }
        Operation entity = lists.get(position);
        holder.title.setText(entity.getOperationTime());
        holder.item1.setText(context.getString(R.string.operation_type_str, entity.getOperationType()));
        if("courier".equals(entity.getOperatorRoleName())) {
            String[] describe = entity.getDescribe().split(" ");
            String describeStr = "";
            if("成员接单".equals(entity.getOperationType().trim()) && describe.length >= 3) {
                describeStr = String.format("%s <font color=\"#ffde1e\">%s %s", describe[0], describe[1], describe[2]);
            } else if("申请移交".equals(entity.getOperationType().trim()) && describe.length >= 5) {
                describeStr = String.format("<font color=\"#ffde1e\">%s %s</font> %s <font color=\"#ffde1e\">%s %s</font>",
                        describe[0], describe[1], describe[2], describe[3], describe[4]);
            }
            if("".equals(describeStr))
                describeStr = entity.getDescribe();
            holder.item2.setText(Html.fromHtml(describeStr));
            holder.item2.setVisibility(View.VISIBLE);
        } else if("business".equals(entity.getOperatorRoleName()) && "商家投诉".equals(entity.getOperationType().trim())) {
            holder.item2.setText(entity.getDescribe());
            holder.item2.setVisibility(View.VISIBLE);
        } else
            holder.item2.setVisibility(View.GONE);

        return convertView;
    }

    public List<Operation> getLists() {
        return lists;
    }

    public void setLists(List<Operation> lists) {
        this.lists = lists;
    }

    public class viewHolder{
        public TextView title;
        public TextView item1;
        public TextView item2;
        //public TextView item3;
    }
}
