
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.alipay.Alipay;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.GiftModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.bjcathay.woqu.wxpay.WXpay;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by jiangm on 15-9-24.
 */
public class SelectPaymentActivity extends Activity implements View.OnClickListener,
        Alipay.PaySucessOrNot {
    private TopView topView;
    WApplication wApplication;
    private long consigneeId;
    private long giftId;
    private Activity activity;
    GiftModel giftModel;
    private WXPAYBroadcastReceiver wxpayBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pay);
        activity = this;
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initReceiver();
        initData();
        initEvent();

    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("支付");
    }

    private void initData() {
        Intent intent = getIntent();
        giftId =  intent.getLongExtra("gift",0);
        consigneeId = intent.getLongExtra("consigneeId", 0);
      //  giftId = giftModel.getId();
    }

    private void initEvent() {
        findViewById(R.id.pay_ipay).setOnClickListener(this);
        findViewById(R.id.pay_wx).setOnClickListener(this);
    }

    private void initReceiver() {
        wxpayBroadcastReceiver = new WXPAYBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WXPAY");
        this.registerReceiver(wxpayBroadcastReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.pay_wx:
                if (giftId != 0 && consigneeId != 0) {
                    findViewById(R.id.pay_wx).setOnClickListener(null);
                    WXpay wXpay = new WXpay(this, consigneeId, giftId);
                    wXpay.wxpay();
                }
                else {
                    DialogUtil.showMessage("信息填写错误");
                }
                break;
            case R.id.pay_ipay:
                if (giftId != 0 && consigneeId != 0) {
                    findViewById(R.id.pay_ipay).setOnClickListener(null);
                    Alipay alipay = new Alipay(this, consigneeId, giftId,
                            SelectPaymentActivity.this);
                    alipay.pay();
                }
                else {
                    DialogUtil.showMessage("信息填写错误");
                }
                break;
        }
    }

    @Override
    public void payStatus(String status, boolean isPay) {
        findViewById(R.id.pay_ipay).setOnClickListener(this);
        if (isPay) {
            if ("success".equals(status)) {
                DialogUtil.showMessage("支付成功");
                Intent intent = new Intent(SelectPaymentActivity.this, MyOrderActivity.class);
                intent.putExtra("pay",true);
                ViewUtil.startTopActivity(SelectPaymentActivity.this, intent);
                finish();
            } else if ("process".equals(status)) {
                DialogUtil.showMessage("支付结果确认中");
            } else if ("fail".equals(status)) {
                DialogUtil.showMessage("支付失败");
            }
        }
    }

    private class WXPAYBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            activity.findViewById(R.id.pay_wx).setOnClickListener(SelectPaymentActivity.this);
            if ("WXPAY".equals(action)) {
                activity.findViewById(R.id.pay_wx).setOnClickListener(SelectPaymentActivity.this);
                String tag = intent.getStringExtra("tag");
                if ("success".equals(tag)) {
                    Intent intent1 = new Intent(SelectPaymentActivity.this, MyOrderActivity.class);
                    intent.putExtra("pay",true);
                    ViewUtil.startTopActivity(SelectPaymentActivity.this, intent1);
                    finish();
                } else {
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择支付方式");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择支付方式");
        MobclickAgent.onPause(this);
    }
}
