package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.ConsigneeModel;
import com.bjcathay.woqu.model.ExpressModel;
import com.bjcathay.woqu.model.OrderModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.SystemUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.RoundCornerImageView;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by jiangm on 15-10-21.
 */
public class WaitingDeliveryActivity extends Activity implements View.OnClickListener{
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
    private WebView webView;
    private LoadingDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_delivery);
        topView= ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        wApplication=WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        context=this;
        initView();
        initData();
    }
    public void initView(){
        nameText=ViewUtil.findViewById(this,R.id.express_person_name);
        phoneText=ViewUtil.findViewById(this,R.id.express_person_number);
        addressText=ViewUtil.findViewById(this,R.id.express_person_address);
        giftTitleText=ViewUtil.findViewById(this,R.id.express_goods_name);
        priceText=ViewUtil.findViewById(this,R.id.express_goods_price);
        orderText=ViewUtil.findViewById(this,R.id.order_num);
        timeText=ViewUtil.findViewById(this,R.id.create_time);
        giftImage=ViewUtil.findViewById(this,R.id.express_goods_img);
        webView=ViewUtil.findViewById(this,R.id.express_webview);
        loadingDialog=new LoadingDialog(this);

    }
    public void initData(){
        Intent intent= getIntent();
        OrderModel orderModel=(OrderModel)intent.getSerializableExtra("order");
        loadingDialog.show();
        OrderModel.orderDetails(orderModel.getId()).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                OrderModel orderModel=arguments.get(0);
                ConsigneeModel consigneeModel=orderModel.getConsignee();
                ExpressModel expressModel=orderModel.getExpress();
                nameText.setText(consigneeModel.getName());
                phoneText.setText(consigneeModel.getContactWay());
                addressText.setText(consigneeModel.getDetailAddress());
                webView.loadDataWithBaseURL(
                        null,
                        expressModel.getContent()
                        ,
                        "text/html", "UTF-8", null);
                ImageViewAdapter.adapt(giftImage,
                        orderModel.getImageUrl(),true);
                giftTitleText.setText(orderModel.getName());
                priceText.setText("快递费:" + (int) Math.floor(expressModel.getPrice()));
                orderText.setText(String.valueOf(orderModel.getOrderId()));
                timeText.setText(SystemUtil.getFormatedDateTime("yyyy-MM-dd HH:mm:ss", orderModel.getCreatedAt()));
                topView.setTitleText(orderModel.getStatus()==null?"":orderModel.getStatus());
                loadingDialog.dismiss();
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("等待配送界面");
        MobclickAgent.onResume(this);
        loadingDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("等待配送界面");
        MobclickAgent.onPause(this);
    }
}
