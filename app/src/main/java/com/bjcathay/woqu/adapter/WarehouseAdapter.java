package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.GiftModel;
import com.bjcathay.woqu.util.SystemUtil;
import com.bjcathay.woqu.util.ViewUtil;

import java.util.List;

/**
 * Created by jiangm on 15-9-29.
 */
public class WarehouseAdapter extends BaseAdapter{
    private Context context;
    List<GiftModel> list;
    public WarehouseAdapter(Context context, List<GiftModel> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_warehouse_item,
                    parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        GiftModel giftModel=list.get(position);
        holder.name.setText(giftModel.getName());
        holder.date.setText(SystemUtil.getFormatedDateTime("yyyy-MM-dd",
                giftModel.getCreate()));
        ImageViewAdapter.adapt(holder.img, giftModel.getImageUrl(),true);
        return convertView;
    }
    class Holder {
        TextView name;
        TextView date;
        ImageView img;
        public Holder(View view) {
            name = ViewUtil.findViewById(view, R.id.gift_name);
            date = ViewUtil.findViewById(view, R.id.gift_date);
            img = ViewUtil.findViewById(view, R.id.gift_img);
        }
    }
}
