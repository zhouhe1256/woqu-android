package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.ConsigneeModel;

import java.util.List;

/**
 * Created by dengt on 15-10-20.
 */
public class SelectAddressAdapter extends BaseAdapter {
    private Context context;
    private List<ConsigneeModel> list;
    private LayoutInflater inflaterf;
    private int cur_pos = -1;

    public SelectAddressAdapter(Context context, List<ConsigneeModel> list) {
        this.context = context;
        this.list = list;
        inflaterf = LayoutInflater.from(context);

    }

    public int getCur_pos() {
        return cur_pos;
    }

    public void setCur_pos(int cur_pos) {
        this.cur_pos = cur_pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
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

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        if (position == cur_pos) {
            viewHolder.def.setVisibility(View.VISIBLE);
        } else {
            viewHolder.def.setVisibility(View.INVISIBLE);

        }
        // viewHolder.def.setVisibility(View.list.get(position).isDef());

        viewHolder.name.setText(list.get(position).getName().toString());
        viewHolder.phone.setText(list.get(position).getContactWay()==null?"":list.get(position).getContactWay().toString());
        viewHolder.address.setText(list.get(position).getDetailAddress().toString());
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView phone;
        TextView address;
        ImageView def;
    }
}