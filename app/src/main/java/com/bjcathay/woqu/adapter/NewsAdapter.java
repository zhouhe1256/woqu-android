package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.MessageModel;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangm on 15-9-29.
 */
public class NewsAdapter extends BaseAdapter{
    private Context context;
    private List<MessageModel> list;
    private LayoutInflater inflaterf;
    public NewsAdapter(Context context,List<MessageModel> list){
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
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=inflaterf.inflate(R.layout.list_news_item,null);
            viewHolder= new ViewHolder();
            viewHolder.newsTitle=(TextView)convertView.findViewById(R.id.news_title);
            viewHolder.viewline=(View)convertView.findViewById(R.id.view_line);
            viewHolder.viewlayout=(RelativeLayout)convertView.findViewById(R.id.view_layout);
            convertView.setTag(viewHolder);
        }
        viewHolder=(ViewHolder)convertView.getTag();
        MessageModel messageModel= list.get(position);
        viewHolder.newsTitle.setText(messageModel.getTitle());
        if(messageModel.getType().equals("SYSTEM_MESSAGE")){
            viewHolder.viewline.setVisibility(View.GONE);
            viewHolder.viewlayout.setVisibility(View.GONE);
        }else{
            viewHolder.viewline.setVisibility(View.VISIBLE);
            viewHolder.viewlayout.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder{
        TextView newsTitle;
        View viewline;
        RelativeLayout viewlayout;
    }

}
