package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.LoginActivity;
import com.bjcathay.woqu.activity.SmoothImageActivity;
import com.bjcathay.woqu.activity.SunAwardDetailsActivity;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ApiUrl;
import com.bjcathay.woqu.model.TalkModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.SystemUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.GridViewForScrollView;
import com.bjcathay.woqu.view.RoundCornerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouh on 15-9-25.
 */
public class SunAwardAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<TalkModel> talks;
    private Boolean click = true;
    private boolean f;
    int width;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout.LayoutParams layoutParams2;
    private ListView listView;
    int _start_index;
    int _end_index;
    public SunAwardAdapter(Context context,List<TalkModel> talks) {
        this.context = context;
        this.talks = talks;
        width=SystemUtil.getScriptWidth()-30;
        layoutParams=new LinearLayout.LayoutParams(width/3*2+25, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return talks.size();
    }

    @Override
    public Object getItem(int position) {
        return talks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.sunaward_fragment_item,null);
            holder = new ViewHolder();
            holder.headImage = (RoundCornerImageView) convertView.findViewById(R.id.sunaward_fragment_image);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.sunaward_fragment_name);
            holder.contentTextView = (TextView) convertView.findViewById(R.id.sunaward_fragment_content);
            holder.zanImage = (LinearLayout) convertView.findViewById(R.id.sunaward_fragment_image_zan);
            holder.addressImage = (ImageView) convertView.findViewById(R.id.sunaward_fragment_image_address);
            holder.informationImage = (LinearLayout) convertView.findViewById(R.id.sunaward_fragment_image_information);
            holder.addressTextView = (TextView) convertView.findViewById(R.id.sunaward_fragment_address);
            holder.informationTextView = (TextView) convertView.findViewById(R.id.sunaward_fragment_information);
            holder.zanTextView = (TextView) convertView.findViewById(R.id.sunaward_fragment_zan);
            holder.sendTextView = (TextView) convertView.findViewById(R.id.sunaward_fragment_send);
            holder.lis = (LinearLayout) convertView.findViewById(R.id.lis);
            holder.imageGridView = (GridViewForScrollView) convertView.findViewById(R.id.sunaward_fragment_gridview);
            holder.zanIconImage = (ImageView) convertView.findViewById(R.id.zan_image);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.sunaward_fragment_time);
            holder.deviceNameTeView = (TextView) convertView.findViewById(R.id.sunaward_fragment_from);
            holder.addressLinear = (LinearLayout) convertView.findViewById(R.id.sunaward_fragment_address_linear);
            holder.sunAwardDetailsImageAdapter=new SunAwardDetailsImageAdapter(context, talks.get(position).getImageUrls());
            holder.imageGridView.setAdapter(holder.sunAwardDetailsImageAdapter);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.sunAwardDetailsImageAdapter.notifyDataSetChanged();
        if(position == 0){
            if(WApplication.getInstance().send_){
                holder.sendTextView.setVisibility(View.VISIBLE);
                if(WApplication.getInstance().send){
                    holder.sendTextView.setVisibility(View.GONE);
                }
            }else{
                holder.sendTextView.setVisibility(View.GONE);
            }
        }
        if(talks.get(position).getLocation()!=null){
            holder.addressTextView.setText(talks.get(position).getLocation());
            holder.addressLinear.setVisibility(View.VISIBLE);
        }else{
            holder.addressLinear.setVisibility(View.GONE);
        }
        if(talks.get(position).getUser()!=null){
            holder.nameTextView.setText(talks.get(position).getUser().getNickname());

            ImageViewAdapter.adapt(holder.headImage, talks.get(position).getUser().getImageUrl(), R.drawable.ic_default_avatar,true);
        }

        holder.informationTextView.setText(talks.get(position).getCommentNumber());
        holder.contentTextView.setText(talks.get(position).getContent());
        holder.timeTextView.setText(talks.get(position).getHumanDate());
        holder.deviceNameTeView.setText("来自:"+talks.get(position).getDeviceName());


        if(talks.get(position).getImageUrls()!=null){
            if(talks.get(position).getImageUrls().size()<=4){
                holder.imageGridView.setNumColumns(2);
                holder.imageGridView.setLayoutParams(layoutParams);
            }else{
                holder.imageGridView.setNumColumns(3);
                holder.imageGridView.setLayoutParams(layoutParams2);
            }
            holder.sunAwardDetailsImageAdapter.setData(talks.get(position).getImageUrls());

            //  holder.imageGridView.setColumnWidth(180);
        }


        int number = talks.get(position).getLikeNumber();
        holder.zanTextView.setText(number + "");
        if(talks.get(position).getIsLike()){
            holder.zanIconImage.setBackgroundResource(R.drawable.zan_is);
        }else{
            holder.zanIconImage.setBackgroundResource(R.drawable.zan);
        }

        holder.zanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(WApplication.getInstance().isLogin()){


                    if(click){
                        click = false;
                        f = talks.get(position).getIsLike();
                        if(f){
                            talks.get(position).setIsLike(false);
                            int p = Integer.parseInt(holder.zanTextView.getText().toString().trim());
                            holder.zanIconImage.setBackgroundResource(R.drawable.zan);
                            holder.zanTextView.setText(String.valueOf(--p));
                            talks.get(position).setLikeNumber(p);
                            Http.instance().post(ApiUrl.getTalksLike(talks.get(position).getId())).
                                    param("_method","DELETE").isCache(false).run().
                                    done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            JSONObject jsonObject = arguments.get(0);
                                            Boolean j = false;
                                            click = true;
                                            try {
                                                j = jsonObject.getBoolean("success");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }).fail(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    DialogUtil.showMessage("取消赞失败,当前网络异常");
                                    int p = Integer.parseInt(holder.zanTextView.getText().toString().trim());
                                    holder.zanIconImage.setBackgroundResource(R.drawable.zan_is);
                                    holder.zanTextView.setText(String.valueOf(++p));
                                    talks.get(position).setIsLike(true);
                                    talks.get(position).setLikeNumber(p);
                                }
                            });
                        }else{
                            click = false;
                            talks.get(position).setIsLike(true);
                            int p = Integer.parseInt(holder.zanTextView.getText().toString().trim());
                            p = p+1;
                            holder.zanTextView.setText(p+"");
                            talks.get(position).setLikeNumber(p);
                            holder.zanIconImage.setBackgroundResource(R.drawable.zan_is);
                            Http.instance().
                                    post(ApiUrl.getTalksLike(talks.get(position).getId())).
                                    isCache(false).run().
                                    done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            JSONObject jsonObject = arguments.get(0);
                                            click = true;

                                        }
                                    }).fail(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    DialogUtil.showMessage("点赞失败,当前网络异常");
                                    talks.get(position).setIsLike(false);
                                    int p = Integer.parseInt(holder.zanTextView.getText().toString().trim());
                                    holder.zanIconImage.setBackgroundResource(R.drawable.zan);
                                    holder.zanTextView.setText(String.valueOf(--p));
                                    talks.get(position).setLikeNumber(p);
                                }
                            });

                        }
                    }
                }else{
                    Intent intent = new Intent(context, LoginActivity.class);
                    ViewUtil.startActivity(context, intent);
                }
            }

        });
        holder.informationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SunAwardDetailsActivity.class);
                intent.putExtra("id",talks.get(position).getId());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SunAwardDetailsActivity.class);
                intent.putExtra("id",talks.get(position).getId());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        final int p = position;
        holder.imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Logger.i("imageurls",talks.get(p).getImageUrls().get(0)+","+position);
                Intent intent = new Intent(context, SmoothImageActivity.class);
                Bundle bundleObject = new Bundle();
                bundleObject.putStringArrayList("t",(ArrayList<String>)talks.get(p).getImageUrls());
                bundleObject.putInt("ID",position);
                intent.putExtras(bundleObject);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
    public void setInformation(Boolean ifzan,String xiaoxi,int position,int zanNumber){
        talks.get(position).setIsLike(ifzan);
        talks.get(position).setCommentNumber(xiaoxi);
        talks.get(position).setLikeNumber(zanNumber);
        this.notifyDataSetChanged();
    }
    public class ViewHolder{
        LinearLayout lis;
        RoundCornerImageView headImage;
        LinearLayout zanImage;
        LinearLayout informationImage;
        TextView nameTextView;
        TextView contentTextView;
        ImageView addressImage;
        ImageView zanIconImage;
        TextView  addressTextView;
        TextView  informationTextView;
        TextView  zanTextView;
        TextView  sendTextView;
        TextView timeTextView;
        TextView deviceNameTeView;
        GridViewForScrollView imageGridView;
        LinearLayout addressLinear;
        SunAwardDetailsImageAdapter sunAwardDetailsImageAdapter;
    }

}
