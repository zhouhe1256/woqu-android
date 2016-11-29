
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.FileUtil;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.GiftModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.FileUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.NotifyingScrollView;
import com.bjcathay.woqu.view.PriceTextView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by jiangm on 15-9-29.
 */
public class CommodityDetailsActivity extends Activity implements View.OnClickListener, ICallback {
    private  WApplication wApplication;
    private  GiftModel giftModel;
    private  TextView name;
    private PriceTextView price;
    private  WebView webView;
    private ImageView imageView;
    protected LoadingDialog progress;
    private RelativeLayout layoutHead;
    private View view_l;
    private NotifyingScrollView scrollView;
    private TextView goShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
        initView();
        initData();
        initEvent();

    }

    private void initView() {
        name = ViewUtil.findViewById(this, R.id.commodity_name);
      //  price = ViewUtil.findViewById(this, R.id.commodity_price);
        webView = ViewUtil.findViewById(this, R.id.commodity_webview);
        imageView = ViewUtil.findViewById(this, R.id.commodity_image);
        view_l =ViewUtil.findViewById(this, R.id.view_l);
        layoutHead = ViewUtil.findViewById(this, R.id.head_f);
        scrollView=ViewUtil.findViewById(this,R.id.scrollView);
        goShop=ViewUtil.findViewById(this,R.id.go_to_shopper);
        layoutHead.getBackground().setAlpha(0);
        view_l.getBackground().setAlpha(0);
    }

    private void initData() {
        Intent intent = getIntent();
        giftModel = (GiftModel) intent.getSerializableExtra("gift");
        if (giftModel != null)
            if (progress == null)
                progress = new LoadingDialog(this);
        progress.show();
        GiftModel.get(giftModel.getId()).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
                progress.dismiss();
            }
        });
    }

    private void initEvent() {
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(true);
        scrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                layoutHead.getBackground().setAlpha(t<255?t:255);
                view_l.getBackground().setAlpha(t<255?t:255);
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.show_bt:
                intent = new Intent(CommodityDetailsActivity.this, PublishedActivity.class);
                intent.putExtra("content", "我获得了" + giftModel.getName() + ",快来参与吧!");
                intent.putExtra("imageId",giftModel.getImageId()+"");
                intent.putExtra("imageUrl",giftModel.getImageUrl());
                ViewUtil.startActivity(CommodityDetailsActivity.this, intent);
                break;
            case R.id.take_bt:
                intent = new Intent(CommodityDetailsActivity.this,
                        NodeliveryOrPaymentActivity.class);
                intent.putExtra("gift", giftModel.getId());

                ViewUtil.startActivity(CommodityDetailsActivity.this, intent);
                break;
            case R.id.back_image:
                finish();
                break;
            case R.id.go_to_shopper:
                intent = new Intent(this,
                        ShoppingActivity.class);
                intent.putExtra("url", giftModel.getFriendlyLink());
                ViewUtil.startActivity(this, intent);
                break;
        }
    }

    @Override
    public void call(Arguments arguments) {
        progress.dismiss();
        giftModel = arguments.get(0);
        name.setText(giftModel.getName());
       //price.setText(Double.toString(giftModel.getPrice()));
        webView.loadDataWithBaseURL(
                null,
                giftModel.getGiftDetail()
                ,
                "text/html", "UTF-8", null);
        ImageViewAdapter.adapt(imageView,
                giftModel.getImageUrls().get(0),true);
        if(giftModel.getFriendlyLink()==null||giftModel.getFriendlyLink().isEmpty()){
            goShop.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("仓库详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("仓库详情页面");
        MobclickAgent.onPause(this);
    }
}
