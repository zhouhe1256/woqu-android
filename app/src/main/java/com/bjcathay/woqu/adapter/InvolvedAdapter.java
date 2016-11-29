package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.ActivityModel;
import com.bjcathay.woqu.util.StartTime;
import com.bjcathay.woqu.view.AutoListView;
import com.bjcathay.woqu.view.PriceTextView;

import java.util.Date;
import java.util.List;

/**
 * Created by jiangm on 15-9-29.
 */
public class InvolvedAdapter extends BaseAdapter {
    private Context context;
    private List<ActivityModel> list;
    private LayoutInflater inflaterf;

    public InvolvedAdapter(Context context, List<ActivityModel> list) {
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

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflaterf.inflate(R.layout.list_involved_item, null);
            holder = new ViewHolder();
            holder.giftPbImage = (ImageView) convertView.findViewById(R.id.woqu_gift_pb);
            holder.giftMoneyText = (PriceTextView) convertView.findViewById(R.id.woqu_gift_money);
            holder.giftNameText = (TextView) convertView.findViewById(R.id.woqu_gift_name);
            holder.giftNumberText = (TextView) convertView.findViewById(R.id.woqu_gift_number_m);
            holder.giftTimeText = (TextView) convertView.findViewById(R.id.woqu_gift_time);
            // holder.giftStatusText = (TextView) convertView.findViewById(R.id.woqu_gift_status_text);
            // holder.giftStatusRelative = (RelativeLayout) convertView.findViewById(R.id.woqu_gift_staus);
            // holder.giftTixingButton = (Button) convertView.findViewById(R.id.woqu_gift_tixing);
            holder.giftTimeTag = (TextView) convertView.findViewById(R.id.woqu_gift_time_tag);
            holder.giftTime = (LinearLayout) convertView.findViewById(R.id.woqu_gift_time_line);
            holder.giftImage = (ImageView) convertView.findViewById(R.id.woqu_gift_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.giftTixingButton.setVisibility(View.GONE);
        ActivityModel activityModel = list.get(position);
        //holder.giftStatusText.setText(activityModel.getStatusLabel());
        holder.giftNameText.setText(activityModel.getTitle());
        holder.giftNumberText.setText(String.valueOf(activityModel.getNumber()));
        holder.giftMoneyText.setTextSize(10);
        holder.giftMoneyText.setText(Double.toString(activityModel.getPrice()));
        ImageViewAdapter.adapt(holder.giftImage, activityModel.getImageUrl(),true);
        ImageViewAdapter.adapt(holder.giftPbImage, activityModel.getTagUrl(),true);
        holder.giftTimeText.setText(activityModel.getEndAt()+"");
//        if (activityModel.getStatusLabel().equals("进行中")) {
//            if (activityModel.getEndAt() != 0) {
//                String time =  StartTime.setStartTime(activityModel.getEndAt(),
//                        activityModel.getNow());
//                holder.giftTimeText.setText(time);
//                holder.giftTimeTag.setText("距结束");
//                holder.giftTime.setVisibility(View.VISIBLE);
//            } else {
//                holder.giftTimeText.setText("无限期");
//            }
//
//
//        }
//        if (activityModel.getStatusLabel().equals("已结束")) {
//            holder.giftTime.setVisibility(View.GONE);
//
//        }
//        if (activityModel.getStatusLabel().equals("未开始")) {
//            //   holder.giftTixingButton.setVisibility(View.VISIBLE);
//            holder.giftTime.setVisibility(View.VISIBLE);
//            String time = StartTime.setStartTime(activityModel.getStartAt(),
//                    activityModel.getNow());
//            holder.giftTimeText.setText(time);
//            holder.giftTimeTag.setText("距开始");
//
//
//        }

        return convertView;

    }

    class ViewHolder{
        ImageView giftPbImage;
        TextView giftNumberText;
        TextView giftTimeText;
        PriceTextView giftMoneyText;
        TextView giftStatusText;
        TextView giftNameText;
        TextView giftTimeTag;
        LinearLayout giftTime;
        //  RelativeLayout giftStatusRelative;
        ImageView giftImage;
    }


}
