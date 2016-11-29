package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.WinnerListModel;
import com.bjcathay.woqu.model.WinnerModel;
import com.bjcathay.woqu.util.StartTime;
import com.bjcathay.woqu.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by zhouh on 15-9-24.
 */
public class WinningListMoreAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<WinnerListModel> winners;
    public WinningListMoreAdapter(Context context, List<WinnerListModel> winners) {
        inflater = LayoutInflater.from(context);
        this.winners = winners;
    }

    @Override
    public int getCount() {
        return winners == null?0:winners.size();
    }

    @Override
    public Object getItem(int position) {
        return winners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.woqu_winning_more_item,null);
            holder = new ViewHolder();
            holder.headImage = (RoundCornerImageView) convertView.findViewById(R.id.woqu_winning_image);
            holder.addressTextView = (TextView) convertView.findViewById(R.id.woqu_winning_address);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.woqu_winning_name);
            holder.fromTextView = (TextView) convertView.findViewById(R.id.woqu_winning_from);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }



            ImageViewAdapter.adapt(
                    holder.headImage,
                    winners.get(position).getUser().getImageUrl(),
                    R.drawable.ic_default_avatar,true);
            holder.nameTextView.setText(winners.get(position).getUser().getNickname());



        return convertView;
    }
    class ViewHolder{
        RoundCornerImageView headImage;
        TextView nameTextView;
        TextView addressTextView;
        TextView fromTextView;


    }
}
