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
import com.bjcathay.woqu.model.OrderModel;
import com.bjcathay.woqu.view.RoundCornerImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangm on 15-9-29.
 */
public class OrderAdapter extends BaseAdapter{
    private Context context;
    private List<OrderModel> list;
    private LayoutInflater inflaterf;
    public OrderAdapter(Context context, List<OrderModel> list){
        this.context=context;
        this.list=list;
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
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=inflaterf.inflate(R.layout.list_order_item,null);
            viewHolder=new ViewHolder();
            viewHolder.image=(RoundCornerImageView)convertView.findViewById(R.id.order_img);
            viewHolder.title=(TextView)convertView.findViewById(R.id.order_name);
            viewHolder.status=(TextView)convertView.findViewById(R.id.order_status);
            convertView.setTag(viewHolder);
        }
        viewHolder=(ViewHolder)convertView.getTag();
        OrderModel orderModel=list.get(position);
        viewHolder.title.setText(orderModel.getName());
        if(orderModel.getStatus()!=null){


//        if(orderModel.getStatus().equals("支付成功")){
//            viewHolder.status.setText("支付成功");
//        }else{
            viewHolder.status.setText(orderModel.getStatus());
 //       }
        }else{
            viewHolder.status.setText("");
        }
        ImageViewAdapter.adapt(viewHolder.image,
                orderModel.getImageUrl(),true);
        return convertView;
    }

    class ViewHolder{
        RoundCornerImageView image;
        TextView title;
        TextView status;
    }
}
