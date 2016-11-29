package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.LikesModel;
import com.bjcathay.woqu.model.SunAwardDetailsTalkModel;
import com.bjcathay.woqu.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by zhouh on 15-10-21.
 */
public class SunAwardDetailsZanImageAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    List<LikesModel> likes;
    private int count;
    public SunAwardDetailsZanImageAdapter(Context context,List<LikesModel> likes){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.likes = likes;

    }

    @Override
    public int getCount() {

        return likes.size();
    }

    @Override
    public Object getItem(int position) {
        return likes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int postion, View convertView, ViewGroup viewGroup) {
         ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sunaward_grida_zan,null);
            holder.imageView = (RoundCornerImageView) convertView.findViewById(R.id.item_sgrida_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }



       if(likes.size()==14){
           holder.imageView.setImageBitmap
                   (BitmapFactory.decodeResource
                           (context.getResources(),R.drawable.sun_zan_more));
       }else if(likes.size()>14&&likes.size()-1==postion){
           holder.imageView.setImageBitmap
                   (BitmapFactory.decodeResource
                           (context.getResources(),R.drawable.sun_zan_smore));
       }else {

           ImageViewAdapter.adapt(holder.imageView,
                   likes.get(postion).getImageUrl(),
                   R.drawable.ic_default_avatar,true);

       }
        return convertView;
    }
    class ViewHolder{
        RoundCornerImageView imageView;


    }
}
