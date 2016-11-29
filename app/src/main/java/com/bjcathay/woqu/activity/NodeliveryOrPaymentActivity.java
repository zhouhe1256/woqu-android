
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.AddressAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ErrorCode;
import com.bjcathay.woqu.model.ConsigneeListModel;
import com.bjcathay.woqu.model.ConsigneeModel;
import com.bjcathay.woqu.model.GiftModel;
import com.bjcathay.woqu.model.MessageModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.RoundCornerImageView;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by jiangm on 15-9-24.
 */
public class NodeliveryOrPaymentActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    WApplication wApplication;
    List<ConsigneeModel> consignees;
    private TextView pName;
    private TextView pNumber;
    private TextView pAddress;
    private TextView gName;
    private TextView gPrice;
    private RoundCornerImageView gImg;
    private WebView webView;
    private Button price;
    GiftModel giftModel;
    ConsigneeModel defaultConsignee;
    private int pos = -1;
    private LinearLayout hasAds;
    private TextView empAds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_deliverypay);
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }
private Handler handler=new Handler(){
  public void handleMessage(Message msg){
GiftModel giftModel=(GiftModel)msg.obj;
      switch (msg.what){
          case 1:
              if (giftModel != null) {
                  gName.setText(giftModel.getName());
                  gPrice.setText("快递费￥:" + (int) Math.floor(giftModel.getExpress().getPrice()));
                  ImageViewAdapter.adapt(gImg,
                          giftModel.getImageUrl(),
                          R.drawable.ic_launcher, R.drawable.ic_launcher,true);
                  webView.loadDataWithBaseURL(
                          null,
                          giftModel.getExpress().getContent()
                          ,
                          "text/html", "UTF-8", null);
                  price.setText("运费:￥" + (int) Math.floor(giftModel.getExpress().getPrice()));

              }
              break;
      }
  }
};
    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("拿走礼物");
        pName = ViewUtil.findViewById(this, R.id.express_person_name);
        pNumber = ViewUtil.findViewById(this, R.id.express_person_number);
        pAddress = ViewUtil.findViewById(this, R.id.express_person_address);
        gName = ViewUtil.findViewById(this, R.id.express_goods_name);
        gPrice = ViewUtil.findViewById(this, R.id.express_goods_price);
        gImg = ViewUtil.findViewById(this, R.id.express_goods_img);
        webView = ViewUtil.findViewById(this, R.id.express_webview);
        price = ViewUtil.findViewById(this, R.id.express_price_btn);
        hasAds = ViewUtil.findViewById(this, R.id.address_has);
        empAds = ViewUtil.findViewById(this, R.id.address_empty);
    }

    private void initData() {
        Intent intent = getIntent();
        long giftid=intent.getLongExtra("gift",0);
     //   giftModel = (GiftModel) intent.getSerializableExtra("gift");
        GiftModel.get(giftid).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                giftModel=arguments.get(0);
                Message msg=new Message();
                msg.obj=giftModel;
                msg.what=1;
                handler.sendMessage(msg);

            }
        });
        ConsigneeListModel.ConsigneeList().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                ConsigneeListModel consigneeListModel = arguments.get(0);
                consignees = consigneeListModel.getConsignees();
                if (consignees != null && !consignees.isEmpty()) {
                    for (ConsigneeModel consigneeModel : consignees) {
                        if (consigneeModel.isDef()) {
                            defaultConsignee = consigneeModel;
                            update();
                            break;
                        }
                    }
                } else {
                    empAds.setVisibility(View.VISIBLE);
                    hasAds.setVisibility(View.GONE);
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                String errorMessage = jsonObject.optString("message");
                if (!errorMessage.isEmpty())
                    DialogUtil.showMessage(errorMessage);
                else {
                    int code = jsonObject.optInt("code");
                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
                }
            }
        });
    }

    private void initEvent() {

    }

    private void update() {
        empAds.setVisibility(View.GONE);
        hasAds.setVisibility(View.VISIBLE);
        pName.setText(defaultConsignee.getName());
        pAddress.setText("收货地址: " + defaultConsignee.getDetailAddress());
        pNumber.setText(defaultConsignee.getContactWay());
    }

    private int addreqCode = 1;
    private int addresCode = 2;

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.express_take:
                if (defaultConsignee != null) {
                    intent = new Intent(NodeliveryOrPaymentActivity.this,
                            SelectPaymentActivity.class);
                    intent.putExtra("gift", giftModel.getId());
                    intent.putExtra("consigneeId", defaultConsignee.getId());
                    ViewUtil.startActivity(NodeliveryOrPaymentActivity.this, intent);
                } else {
                    DialogUtil.showMessage("请填写收货信息");
                }
                break;
            case R.id.title_back_img:
                finish();
                break;
            case R.id.express_select_address:
                intent = new Intent(NodeliveryOrPaymentActivity.this,
                        SelectAddressActivity.class);
                intent.putExtra("defaultConsignee", defaultConsignee);
                startActivityForResult(intent, addreqCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == requestCode && resultCode == addresCode) {
            defaultConsignee = (ConsigneeModel) data.getSerializableExtra("consignee");
            update();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("拿走礼物页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("拿走礼物页面");
        MobclickAgent.onPause(this);
    }
}
