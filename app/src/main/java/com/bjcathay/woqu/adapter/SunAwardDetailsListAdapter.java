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
import com.bjcathay.woqu.model.CommentModel;
import com.bjcathay.woqu.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by zhouh on 15-10-14.
 */
public class SunAwardDetailsListAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private CommentModel commentModel;
    public SunAwardDetailsListAdapter(Context context,CommentModel commentModel){
        inflater = LayoutInflater.from(context);
        this.commentModel = commentModel;
    }
    @Override
    public int getCount() {
        return commentModel.getComments().size();
    }

    @Override
    public Object getItem(int position) {
        return commentModel.getComments().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_sundetails_list,null);
            holder = new ViewHolder();
            holder.headImageview = (RoundCornerImageView) convertView.findViewById(R.id.sunaward_details_image);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.sunaward_details_name);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.sunaward_details_time);
            holder.contentTextView = (TextView) convertView.findViewById(R.id.sunaward_details_content);
            holder.xiaoxiImage = (ImageView) convertView.findViewById(R.id.xiaoxi);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
       /* if (position==0){
            holder.xiaoxiImage.setVisibility(View.VISIBLE);
        }else{
            holder.xiaoxiImage.setVisibility(View.INVISIBLE);
        }*/
        holder.contentTextView.setText(commentModel.getComments().get(position).getContent());
        ImageViewAdapter.adapt(holder.headImageview,
                commentModel.getComments().get(position).getImageUrl(),R.drawable.ic_default_avatar,true);
        holder.nameTextView.setText(commentModel.getComments().get(position).getNickname());
        holder.timeTextView.setText(commentModel.getComments().get(position).getHumanDate());
        return convertView;
    }
    class ViewHolder{
        RoundCornerImageView headImageview;
        ImageView xiaoxiImage;
        TextView nameTextView;
        TextView timeTextView;
        TextView contentTextView;

    }
}
