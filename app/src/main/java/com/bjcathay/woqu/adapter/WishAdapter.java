package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.CampaignModel;
import com.bjcathay.woqu.model.WoQuGiftModel;
import com.bjcathay.woqu.model.WoQuModel;
import com.bjcathay.woqu.view.CircleImageView;
import com.bjcathay.woqu.view.RoundCornerImageView;
import com.bjcathay.woqu.view.ScrollViewWithListView;

import java.util.List;

/**
 * Created by zhouh on 15-9-25.
 */
public class WishAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CampaignModel> campaigns;
    private Context context;
    public WishAdapter(Context context ,List<CampaignModel> campaigns) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.campaigns = campaigns;


    }

    @Override
    public int getCount() {
        return campaigns==null?0:campaigns.size();
    }

    @Override
    public Object getItem(int position) {
        return campaigns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){

            convertView = inflater.inflate(R.layout.wish_fragment_item,null);
            holder = new ViewHolder();

            holder.headImage = (RoundCornerImageView) convertView.findViewById(R.id.wish_fragment_image);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.wish_fragment_name);
            holder.stateTextView = (TextView) convertView.findViewById(R.id.wish_fragment_state);
            holder.contentTextView = (TextView) convertView.findViewById(R.id.wish_fragment_content);
         //   holder.giftlist=(ScrollViewWithListView)convertView.findViewById(R.id.gift_list);
            holder.layout=(LinearLayout)convertView.findViewById(R.id.gift_layout);
//            holder.prodouctTextView_1 = (TextView) convertView.findViewById(R.id.wish_fragment_prodouct_name_1);
//            holder.prodouctTextView_2 = (TextView) convertView.findViewById(R.id.wish_fragment_prodouct_name_2);
//            holder.prodouctTextView_3 = (TextView) convertView.findViewById(R.id.wish_fragment_prodouct_name_3);
//            holder.prodouct_1 = (RoundCornerImageView) convertView.findViewById(R.id.wish_fragment_prodouct);
//            holder.prodouct_2 = (RoundCornerImageView) convertView.findViewById(R.id.wish_fragment_prodouct_2);
//            holder.prodouct_3 = (RoundCornerImageView) convertView.findViewById(R.id.wish_fragment_prodouct_3);
//            holder.numberTextView_1 = (TextView) convertView.findViewById(R.id.wish_fragment_number);
//            holder.numberTextView_2 = (TextView) convertView.findViewById(R.id.wish_fragment_number_2);
//            holder.numberTextView_3 = (TextView) convertView.findViewById(R.id.wish_fragment_number_3);
//            holder.progressBar_1 = (ProgressBar) convertView.findViewById(R.id.progressBar);
//            holder.progressBar_2 = (ProgressBar) convertView.findViewById(R.id.progressBar_2);
//            holder.progressBar_3 = (ProgressBar) convertView.findViewById(R.id.progressBar_3);
//            holder.tagImageView_1 = (ImageView) convertView.findViewById(R.id.wish_fragment_tag_1);
//            holder.tagImageView_2 = (ImageView) convertView.findViewById(R.id.wish_fragment_tag_2);
//            holder.tagImageView_3 = (ImageView) convertView.findViewById(R.id.wish_fragment_tag_3);
            //   holder.giftlist.setFocusable(false);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //convertView.setFocusable(true);
        ImageViewAdapter.adapt(holder.headImage, campaigns.
                get(position).getUser().getImageUrl(),R.drawable.ic_default_avatar,true);

//        ImageViewAdapter.adapt(holder.prodouct_1, campaigns.
//                        get(position).getGifts().get(0).getImageUrl(),true);
//        ImageViewAdapter.adapt(holder.prodouct_2, campaigns.
//                        get(position).getGifts().get(1).getImageUrl(),true);
//        ImageViewAdapter.adapt(holder.prodouct_3,campaigns.
//                        get(position).getGifts().get(2).getImageUrl(),true);

        String status = campaigns.get(position).getStatusLabel();
        holder.stateTextView.setText(status);
        holder.contentTextView.setText(campaigns.get(position).getTitle());
        if (status.equals("已参与")){
            holder.stateTextView.setText("已参与");
            holder.stateTextView.setTextColor(Color.parseColor("#FC4A5B"));
        }else if(status.equals("即将开始")){
            holder.stateTextView.setText("即将开始");
            holder.stateTextView.setTextColor(Color.parseColor("#C8C8C8"));
        }else if(status.equals("已结束")){
            holder.stateTextView.setText("已结束");
            holder.stateTextView.setTextColor(Color.parseColor("#C8C8C8"));
        }else if(status.equals("进行中")){
            holder.stateTextView.setText("进行中");
            holder.stateTextView.setTextColor(Color.parseColor("#323232"));
        }else {
            holder.stateTextView.setTextColor(Color.parseColor("#323232"));
        }
        Logger.i("getSelected",campaigns.get(position).getGifts().get(0).getSelected()+"55");

        holder.nameTextView.setText(
                campaigns.get(position).getUser().getNickname());

//        holder.progressBar_1.setMax(
//                campaigns.get(position).getExpect());
//        holder.progressBar_2.setMax(
//                campaigns.get(position).getExpect());
//        holder.progressBar_3.setMax(
//                campaigns.get(position).getExpect());
//
//
//        holder.progressBar_1.setProgress(
//                (int)campaigns
//                        .get(position).getGifts().get(0).getNumber()
//        );
//        holder.progressBar_2.setProgress(
//                (int)campaigns
//                        .get(position).getGifts().get(1).getNumber()
//        );
//        holder.progressBar_3.setProgress(
//                (int)campaigns
//                        .get(position).getGifts().get(2).getNumber()
//        );
//
//        holder.prodouctTextView_1.setText(campaigns
//                .get(position).getGifts().get(0).getName());
//        holder.prodouctTextView_2.setText(campaigns
//                .get(position).getGifts().get(1).getName());
//        holder.prodouctTextView_3.setText(campaigns
//                .get(position).getGifts().get(2).getName());
//
//
//
//        holder.numberTextView_1.setText(
//                (int)campaigns
//                        .get(position).getGifts().get(0).getNumber()+"");
//        holder.numberTextView_2.setText(
//                (int)campaigns
//                        .get(position).getGifts().get(1).getNumber()+"");
//        holder.numberTextView_3.setText(
//                (int)campaigns
//                        .get(position).getGifts().get(2).getNumber()+"");
//
//        Boolean select_1 = campaigns.
//                get(position).getGifts().get(0).getSelected();
//        Boolean select_2 = campaigns.
//                get(position).getGifts().get(1).getSelected();
//        Boolean select_3 = campaigns.
//                get(position).getGifts().get(2).getSelected();
//        Logger.i("select",select_1+","+position);
//        if(select_1){
//            holder.tagImageView_1.setVisibility(View.VISIBLE);
//            holder.tagImageView_2.setVisibility(View.INVISIBLE);
//            holder.tagImageView_3.setVisibility(View.INVISIBLE);
//        }else if(select_2){
//            holder.tagImageView_1.setVisibility(View.INVISIBLE);
//            holder.tagImageView_2.setVisibility(View.VISIBLE);
//            holder.tagImageView_3.setVisibility(View.INVISIBLE);
//        }else if(select_3){
//            holder.tagImageView_1.setVisibility(View.INVISIBLE);
//            holder.tagImageView_2.setVisibility(View.INVISIBLE);
//            holder.tagImageView_3.setVisibility(View.VISIBLE);
//        }else{
//            holder.tagImageView_1.setVisibility(View.INVISIBLE);
//            holder.tagImageView_2.setVisibility(View.INVISIBLE);
//            holder.tagImageView_3.setVisibility(View.INVISIBLE);
//        }


//        WishGifts wishGifts=new WishGifts(campaigns.get(position).getGifts(),context,campaigns.get(position).getExpect());
//        holder.giftlist.setAdapter(wishGifts);
        holder.layout.removeAllViews();
        if(holder.layout.getChildCount()>=campaigns.get(position).getGifts().size()){

        }else
        for(int i=0;i<campaigns.get(position).getGifts().size();i++){
            View view=LayoutInflater.from(context).inflate(R.layout.wish_gift_item,null);
            TextView giftname=(TextView)view.findViewById(R.id.wish_fragment_prodouct_name_1);
            TextView num=(TextView)view.findViewById(R.id.wish_fragment_number);
            RoundCornerImageView roundCornerImageView=(RoundCornerImageView)view.findViewById(R.id.wish_fragment_prodouct);
            ProgressBar progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            ImageView selectImage=(ImageView)view.findViewById(R.id.wish_fragment_tag_1);
            WoQuGiftModel gift= campaigns.get(position).getGifts().get(i);
            giftname.setText(
                    gift.getName());
            progressBar.setMax(campaigns.get(position).getExpect());
            progressBar.setProgress((int)gift.getNumber());
            num.setText((int)gift.getNumber()+"");
            if(gift.getImageUrl()!=null&&!gift.getImageUrl().isEmpty()){
                ImageViewAdapter.adapt(roundCornerImageView,gift.getImageUrl(),true);
            }
            Logger.i("getSelected",gift.getSelected()+"");
            if(gift.getSelected()){
                selectImage.setVisibility(View.VISIBLE);
            }else{
                selectImage.setVisibility(View.INVISIBLE);
            }
           // selectImage.setVisibility(gift.getSelected()?View.VISIBLE:View.INVISIBLE);
            holder.layout.addView(view);
        }

        return convertView;
    }
    class ViewHolder{
        RoundCornerImageView headImage;
        TextView nameTextView;
        TextView stateTextView;
        TextView contentTextView;
        TextView prodouctTextView_1;
        TextView prodouctTextView_2;
        TextView prodouctTextView_3;
        RoundCornerImageView prodouct_1;
        RoundCornerImageView prodouct_2;
        RoundCornerImageView prodouct_3;
        TextView  numberTextView_1;
        TextView  numberTextView_2;
        TextView  numberTextView_3;
        ProgressBar progressBar_1;
        ProgressBar progressBar_2;
        ProgressBar progressBar_3;
        ImageView tagImageView_1;
        ImageView tagImageView_2;
        ImageView tagImageView_3;
        ScrollViewWithListView giftlist;
        LinearLayout layout;

    }



}
