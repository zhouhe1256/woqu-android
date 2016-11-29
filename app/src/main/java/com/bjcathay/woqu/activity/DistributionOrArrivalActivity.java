
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.ConsigneeModel;
import com.bjcathay.woqu.model.DataModel;
import com.bjcathay.woqu.model.ExpressModel;
import com.bjcathay.woqu.model.OrderModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.SystemUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.ExpressComposeView;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.RoundCornerImageView;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangm on 15-9-24.
 */
public class DistributionOrArrivalActivity extends Activity implements View.OnClickListener {
    TopView topView;
    WApplication wApplication;
    Context context;
    private TextView nameText;
    private TextView phoneText;
    private TextView addressText;
    private TextView giftTitleText;
    private TextView priceText;
    private TextView orderText;
    private TextView timeText;
    private RoundCornerImageView giftImage;

    private ListView listView;
    private List<DataModel> dataModels;
    private ExpressComposeView expressView;
    private Button sunBt;
    private OrderModel orderModel;
    private static final int SET = 1;
    protected LoadingDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_arrival);
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        context = this;
        initView();
        initData();

    }

    public void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        nameText = ViewUtil.findViewById(this, R.id.express_person_name);
        phoneText = ViewUtil.findViewById(this, R.id.express_person_number);
        addressText = ViewUtil.findViewById(this, R.id.express_person_address);
        giftTitleText = ViewUtil.findViewById(this, R.id.express_goods_name);
        priceText = ViewUtil.findViewById(this, R.id.express_goods_price);
        orderText = ViewUtil.findViewById(this, R.id.order_num);
        timeText = ViewUtil.findViewById(this, R.id.create_time);
        giftImage = ViewUtil.findViewById(this, R.id.express_goods_img);

        // listView=ViewUtil.findViewById(this,R.id.express_list);
        expressView = ViewUtil.findViewById(this, R.id.express_view);
        sunBt = ViewUtil.findViewById(this, R.id.express_take);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    expressView.setList((List<DataModel>) msg.obj);
                    break;
            }
        }
    };

    public void initData() {
        if (progress == null)
            progress = new LoadingDialog(this);
        progress.show();
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("order");
        topView.setTitleText(orderModel.getStatus());
        if (orderModel.getStatus().equals("已签收")) {
            sunBt.setVisibility(View.VISIBLE);
        }
        dataModels = new ArrayList<DataModel>();
        OrderModel.orderDetails(orderModel.getId()).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                orderModel = arguments.get(0);
                ConsigneeModel consigneeModel = orderModel.getConsignee();
                ExpressModel expressModel = orderModel.getExpress();
                try {
                    JSONObject jsonObject = new JSONObject(orderModel.getExpressData());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        DataModel dataModel = new DataModel();
                        dataModel.setTime(jsonObject1.getString("time"));
                        dataModel.setContext(jsonObject1.getString("context"));
                        dataModel.setLocation(jsonObject1.getString("location"));
                        dataModels.add(dataModel);

                    }
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = dataModels;
                    handler.sendMessage(msg);
                } catch (Exception e) {

                }

                nameText.setText(consigneeModel.getName());
                phoneText.setText(consigneeModel.getContactWay());
                addressText.setText(consigneeModel.getDetailAddress());
                ImageViewAdapter.adapt(giftImage,
                        orderModel.getImageUrl(),
                        R.drawable.ic_launcher, R.drawable.ic_launcher, true);
                giftTitleText.setText(orderModel.getName());
                priceText.setText("快递费:" + (int) Math.floor(expressModel.getPrice()));
                orderText.setText(String.valueOf(orderModel.getOrderId()));
                timeText.setText(SystemUtil.getFormatedDateTime("yyyy-MM-dd HH:mm:ss",
                        orderModel.getCreatedAt()));
                progress.dismiss();
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
                progress.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.express_take:
                Intent intent = new Intent(DistributionOrArrivalActivity.this,
                        PublishedActivity.class);
                intent.putExtra("content", "我获得了" + orderModel.getName() + ",快来参与吧!");
                if (orderModel.getImageId() != 0) {
                    intent.putExtra("imageId", orderModel.getImageId() + "");
                    intent.putExtra("imageUrl", orderModel.getImageUrl());
                }

                ViewUtil.startActivity(DistributionOrArrivalActivity.this, intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("配送中快递详情");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("配送中快递详情");
        MobclickAgent.onPause(this);
    }
}
