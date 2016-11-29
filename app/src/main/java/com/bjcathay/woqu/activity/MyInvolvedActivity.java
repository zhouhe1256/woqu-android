package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.InvolvedAdapter;
import com.bjcathay.woqu.adapter.NewsAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.ActivityModel;
import com.bjcathay.woqu.model.InvolvedModel;
import com.bjcathay.woqu.model.MessageListModel;
import com.bjcathay.woqu.model.MessageModel;
import com.bjcathay.woqu.util.StartTime;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.AutoListView;
import com.bjcathay.woqu.view.PriceTextView;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jiangm on 15-9-29.
 */
public class MyInvolvedActivity extends Activity implements View.OnClickListener,
        AutoListView.OnRefreshListener,AutoListView.OnLoadListener,ICallback{
    TopView topView;
    AutoListView listView;
    List<ActivityModel> activityModels;
    InvolvedAdapter involvedAdapter;
    WApplication wApplication;
    private int page=1;
    private boolean timing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_involved);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("我参与的");
        wApplication=WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }
    public void initView() {

        listView = ViewUtil.findViewById(this, R.id.listView);
        activityModels = new ArrayList<ActivityModel>();
        involvedAdapter = new InvolvedAdapter(this, activityModels);
        listView.setAdapter(involvedAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setListViewEmptyImage(R.drawable.ic_empty_involved);
        listView.setListViewEmptyMessage(getString(R.string.empty_free_involved));
    }




    public void initData() {
        loadData(AutoListView.REFRESH);
    }
    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityModel activityModel=activityModels.get(position-1);

                if(activityModel.getStatus()==0){//0即将开始1进行中2已结束
                    Intent intent=new Intent(MyInvolvedActivity.this,PointPraiseActivity.class);
                    intent.putExtra("id",activityModel.getId());
                    intent.putExtra("tagImageUrl",activityModel.getTagUrl());
                    intent.putExtra("imageUrl",activityModel.getImageUrl());
                    ViewUtil.startActivity(MyInvolvedActivity.this,intent);
                }else if(activityModel.getStatus()==1){
                    Intent intent=new Intent(MyInvolvedActivity.this,PointPraiseActivity.class);
                    intent.putExtra("id",activityModel.getId());
                    intent.putExtra("tagImageUrl",activityModel.getTagUrl());
                    intent.putExtra("imageUrl",activityModel.getImageUrl());
                    ViewUtil.startActivity(MyInvolvedActivity.this,intent);
                }else if(activityModel.getStatus()==2){
                    Intent intent=new Intent(MyInvolvedActivity.this,PointPraiseEndActivity.class);
                    intent.putExtra("id",activityModel.getId());
                    intent.putExtra("tagImageUrl",activityModel.getTagUrl());
                    intent.putExtra("imageUrl",activityModel.getImageUrl());
                    ViewUtil.startActivity(MyInvolvedActivity.this,intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back_img:
                finish();
                break;
        }
    }
    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);

    }
    private void loadData(final int what) {
        switch (what) {
            case AutoListView.REFRESH:
                page = 1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }
       // handler.sendEmptyMessage(3);
        InvolvedModel.getActivityList(page).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (listView != null) {
                    listView.onRefreshComplete();
                    listView.setResultSize(-1, false);
                }
            }
        });

    }

    @Override
    public void call(Arguments arguments) {
        InvolvedModel involvedModel=arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1) {
            msg.what = AutoListView.REFRESH;
        }else {
            msg.what = AutoListView.LOAD;
        }


        msg.obj = involvedModel;
        handler.sendMessage(msg);
        if(timing==false){
            handler.sendEmptyMessage(3);
            timing=true;
        }
    }
    boolean hasNext;
    boolean isNeedCountTime = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            InvolvedModel result = (InvolvedModel) msg.obj;

            if(result!=null){
                hasNext = result.isHasNext();
            }

            if (result != null && result.getActivities() != null && !result.getActivities().isEmpty()) {

                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        activityModels.clear();
                        activityModels.addAll(result.getActivities());
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        activityModels.addAll(result.getActivities());
                        break;
                }
                listView.setResultSize(activityModels.size(), hasNext);
                involvedAdapter.notifyDataSetChanged();
            } else {

                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        break;
                }
                listView.setResultSize(activityModels.size(), hasNext);
                involvedAdapter.notifyDataSetChanged();
            }
            if(msg.what==3){

                long times=0;
                for(int i=0;i<activityModels.size();i++){
                    long time = activityModels.get(i).getEndAt();
                    if(time>1000){
                        isNeedCountTime = true;
                        times=time-1000;
                        activityModels.get(i).setEndAt(times);
                    }else{
                        activityModels.get(i).setEndAt(0);
                    }
                }
                if(isNeedCountTime){
                    handler.sendEmptyMessageDelayed(3, 1000);
                }
            }
        }

        ;
    };
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我参与的");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我参与的");
        MobclickAgent.onPause(this);
    }
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

        ViewHolder holder;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


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
            holder.giftImage.setTag(activityModel.getImageUrl());
            //holder.giftStatusText.setText(activityModel.getStatusLabel());
            holder.giftNameText.setText(activityModel.getTitle());
            holder.giftNumberText.setText(String.valueOf(activityModel.getNumber()==0?"不限":activityModel.getNumber()));
            holder.giftMoneyText.setTextSize(10);
            holder.giftMoneyText.setText(Double.toString(activityModel.getPrice()));
            if(holder.giftImage.getTag().equals(activityModel.getImageUrl())){
                ImageViewAdapter.adapt(holder.giftImage, activityModel.getImageUrl(),true);
            }

            ImageViewAdapter.adapt(holder.giftPbImage, activityModel.getTagUrl(),true);
            //  holder.giftTimeText.setText(activityModel.getEndAt()+"");
            if (activityModel.getStatusLabel().equals("进行中")) {
                String time =  StartTime.setStartTime(activityModel.getEndAt(),
                        activityModel.getNow());
                if (activityModel.getEndAt()>=0) {

                    holder.giftTimeText.setText(time);
                    holder.giftTimeTag.setText("距结束:");
                    holder.giftTime.setVisibility(View.VISIBLE);
                } else {
                    holder.giftTimeText.setText("不限时");
                }


            }
            if (activityModel.getStatusLabel().equals("已结束")) {
                holder.giftTime.setVisibility(View.GONE);

            }
            if (activityModel.getStatusLabel().equals("未开始")) {
                //   holder.giftTixingButton.setVisibility(View.VISIBLE);
                holder.giftTime.setVisibility(View.VISIBLE);
                String time = StartTime.setStartTime(activityModel.getStartAt(),
                        activityModel.getNow());
                holder.giftTimeText.setText(time);
                holder.giftTimeTag.setText("距开始:");


            }

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
}
