
package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.ConsigneeModel;

import java.util.List;

/**
 * Created by jiangm on 15-9-29.
 */
public class AddressAdapter extends BaseAdapter {
    private Context context;
    private List<ConsigneeModel> list;
    private LayoutInflater inflaterf;

    public AddressAdapter(Context context, List<ConsigneeModel> list) {
        this.context = context;
        this.list = list;
        inflaterf = LayoutInflater.from(context);

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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflaterf.inflate(R.layout.list_address_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.consignee_text);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.phone_text);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address_text);
            viewHolder.def = (ImageView) convertView.findViewById(R.id.checkBox);
            viewHolder.layout=(LinearLayout)convertView.findViewById(R.id.address_layout);
            viewHolder.text1= (TextView) convertView.findViewById(R.id.text_1);
            viewHolder.text2= (TextView) convertView.findViewById(R.id.text_2);
            viewHolder.text3= (TextView) convertView.findViewById(R.id.text_3);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        if (list.get(position).isDef()) {
            viewHolder.def.setVisibility(View.VISIBLE);

          viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.blue_btn_color));
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.phone.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.address.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.text1.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.text2.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.text3.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.def.setVisibility(View.INVISIBLE);
            viewHolder.layout.setBackgroundResource(R.drawable.item_click);
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.main_textcolor));
            viewHolder.phone.setTextColor(context.getResources().getColor(R.color.main_textcolor));
            viewHolder.address.setTextColor(context.getResources().getColor(R.color.main_textcolor));
            viewHolder.text1.setTextColor(context.getResources().getColor(R.color.main_textcolor));
            viewHolder.text2.setTextColor(context.getResources().getColor(R.color.main_textcolor));
            viewHolder.text3.setTextColor(context.getResources().getColor(R.color.main_textcolor));

        }
        // viewHolder.def.setVisibility(View.list.get(position).isDef());

        viewHolder.name.setText(list.get(position).getName().toString());
        viewHolder.phone.setText(list.get(position).getContactWay()==null?"":list.get(position).getContactWay());
        viewHolder.address.setText(list.get(position).getDetailAddress().toString());
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView phone;
        TextView address;
        ImageView def;
        LinearLayout layout;
        TextView text1;
        TextView text2;
        TextView text3;
    }
}
