package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.WishBannerViewPagerAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.BannerModel;
import com.bjcathay.woqu.model.ShareModel;
import com.bjcathay.woqu.model.WishDetailsModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ShareUtil;
import com.bjcathay.woqu.util.SizeUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.DeleteInfoDialog;
import com.bjcathay.woqu.view.JazzyViewPager;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.RoundCornerImageView;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class WishDetailsActivity extends Activity implements View.OnClickListener,DeleteInfoDialog.DeleteInfoDialogResult{
    private TopView topView;
    private TextView ruleTextview;
    private TextView statusTextView;
    private TextView prodouctNameTextView;
    private TextView priceTextView;
    private JazzyViewPager bannerViewPager;
    private WebView webView;
    private ProgressBar progressBar;
    private TextView peopleNumberTextView;
    private LinearLayout dotoParendLinearLayout;
    private LinearLayout attendDetailsLinearLayout;
    private TextView shoppingTextView;
    private ImageView image_1;
    private ImageView image_2;
    private RoundCornerImageView headImage;
    private Button shareButton;
    private Runnable runnable;
    private ImageView[] dots;
    private int page = 1;
    private Activity context;
    private String id;
    private WishDetailsModel wishDetails;
    private LinearLayout isStatus;
    private List<BannerModel> b;
    private ShareModel shareModel;
    public static Boolean fk = false;
    protected LoadingDialog progress;
    private String url;
    private String giftId;
    private int pg;
    public static String ACTION_NAME = "com.bjcathay.woqu.wish";
    private IntentFilter myIntentFilter;
    private Handler handler = new Handler();
    private Handler handler_1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_details);
        initView();
        setListeners();
        setData();
    }

    private void setData() {

        if (progress == null){
            progress = new LoadingDialog(this);
        }
        if(flag==1){
            progress.show();
        }

        WishDetailsModel.getWishList(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                wishDetails = arguments.get(0);
                if (wishDetails.getSuccess()) {

                /*if(wishDetails.getCampaign().getAttend()){
                    shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));

                }*/
//                if(wishDetails.getCampaign().getAttend()){
//                    shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
//                }

                    //}


                    pg = bannerViewPager.getCurrentItem();
                    if (WApplication.getInstance().isLogin()) {
                        if (wishDetails.getCampaign().getAttend()) {
                            if (!wishDetails.getCampaign().getGifts().get(pg).getSelected()) {
                                shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
                                attendDetailsLinearLayout.setVisibility(View.GONE);
                            } else {
                                shareButton.setBackgroundColor(Color.parseColor("#FC4A5B"));
                                attendDetailsLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    url = wishDetails.getCampaign().getGifts().get(pg).getLink();
                    if (url != null && !url.isEmpty()) {
                        shoppingTextView.setVisibility(View.VISIBLE);
                    }
                    ruleTextview.setText(wishDetails.getCampaign().getRule());
                    String status = wishDetails.getCampaign().getStatusLabel();
                    statusTextView.setText(status);
                    if (status.equals("已参与")) {
                        statusTextView.setText("已参与");
                        statusTextView.setTextColor(Color.parseColor("#FC4A5B"));
                    } else if (status.equals("即将开始")) {
                        statusTextView.setText("即将开始");
                        statusTextView.setTextColor(Color.parseColor("#C8C8C8"));
                    } else if (status.equals("已结束")) {
                        statusTextView.setText("已结束");
                        statusTextView.setTextColor(Color.parseColor("#C8C8C8"));
                    } else if (status.equals("进行中")) {
                        statusTextView.setText("进行中");
                        statusTextView.setTextColor(Color.parseColor("#323232"));
                    }
                    ImageViewAdapter.adapt(headImage, wishDetails.getCampaign().getUser().getImageUrl(), R.drawable.ic_default_avatar, true);
                    prodouctNameTextView.setText(wishDetails.getCampaign().getGifts().get(pg).getName());
                    progressBar.setMax(wishDetails.getCampaign().getExpect());
                    progressBar.setProgress((int) wishDetails.getCampaign().getGifts().get(pg).getNumber());
                    peopleNumberTextView.setText("已有" + (int) wishDetails.getCampaign().getGifts().get(pg).getNumber() + "人参与");
                    priceTextView.setText("【市场价】¥" + (int) wishDetails.getCampaign().getGifts().get(pg).getPrice() + "");
                    b = new ArrayList<BannerModel>();
                    if (wishDetails.getCampaign().getGifts().get(pg).getSelected() == true) {
                        isStatus.setVisibility(View.VISIBLE);
                    } else {
                        isStatus.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < wishDetails.getCampaign().getGifts().size(); i++) {
                        BannerModel bannerModel = new BannerModel();
                        bannerModel.setImageUrl(wishDetails.getCampaign().getGifts().get(i).getBannerImageUrl());
                        b.add(bannerModel);
                    }
                    setupBanner(b);
                    webView.loadDataWithBaseURL(null, wishDetails.getCampaign().getGifts().get(0).getGiftDetail(),
                            "text/html", "utf-8", null);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    DeleteInfoDialog infoDialog = new DeleteInfoDialog(WishDetailsActivity.this, R.style.InfoDialog,
                            wishDetails.getMessage(), 1l, WishDetailsActivity.this);
                    infoDialog.f = true;
                    infoDialog.show();
                    //  infoDialog.setDialogConfirmText("我知道了");
                }

            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });

        flag=0;
    }

    private void setListeners() {
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = bannerViewPager.getCurrentItem();
                if(WApplication.getInstance().isLogin()){
                    if(wishDetails.getCampaign().getGifts().get(p).getSelected()||!wishDetails.getCampaign().getAttend()){
                        if(wishDetails.getCampaign().getGifts().get(p).getSelected()==true){
                            pg = bannerViewPager.getCurrentItem();
                            giftId = wishDetails.getCampaign().getGifts().get(pg).getId();

                            ShareModel.jingXuanShare(id,giftId).done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    shareModel = arguments.get(0);
                                    if (shareModel.getSuccess()) {
                                        ShareUtil.showShare(WishDetailsActivity.this, shareModel, id, "wish", giftId);

                                    }else{
                                        DialogUtil.showMessage(shareModel.getMessage());

                                    }
                                    progress.dismiss();
                                }
                            }).fail(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    DialogUtil.showMessage(getString(R.string.empty_net_text));
                                    progress.dismiss();
                                }
                            });
                        }else{
                            DeleteInfoDialog infoDialog = new DeleteInfoDialog(WishDetailsActivity.this, R.style.InfoDialog,
                                    "此活动里的商品只能被分享一个哦", 0l, WishDetailsActivity.this);
                            infoDialog.f = true;
                            infoDialog.show();
                        }


                    }else{
                        // shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
                    }
                }else{
                    DialogUtil.showMessage("用户未登录");
                    Intent intent = new Intent(WishDetailsActivity.this, LoginActivity.class);
                    ViewUtil.startActivity(WishDetailsActivity.this, intent);
                }
            }
        });
        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = bannerViewPager.getCurrentItem();
                Logger.i("banner",p+","+wishDetails.getCampaign().getGifts().get(p).getSelected()+"ff");
                if(b!=null){
                    if (p != 0) {
                        bannerViewPager.setCurrentItem(p - 1, true);
                        webView.loadDataWithBaseURL(null,wishDetails.getCampaign().getGifts().get(p - 1).getGiftDetail(),
                                "text/html","utf-8",null);
                        prodouctNameTextView.setText(wishDetails.getCampaign().getGifts().get(p).getName());
                        progressBar.setMax(wishDetails.getCampaign().getExpect());
                        progressBar.setProgress((int)wishDetails.getCampaign().getGifts().get(p).getNumber());
                        peopleNumberTextView.setText("已有"+(int)wishDetails.getCampaign().getGifts().get(p).getNumber()+"人参与");
                        priceTextView.setText("【市场价】¥" + (int)wishDetails.getCampaign().getGifts().get(p).getPrice()+"");
                        if(WApplication.getInstance().isLogin()){
                            if (wishDetails.getCampaign().getAttend()) {
                                if(!wishDetails.getCampaign().getGifts().get(p-1).getSelected()){
                                    shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
                                    attendDetailsLinearLayout.setVisibility(View.GONE);
                                }else{
                                    shareButton.setBackgroundColor(Color.parseColor("#FC4A5B"));
                                    attendDetailsLinearLayout.setVisibility(View.VISIBLE);
                                }}}
                    }

                }

            }
        });
        image_2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           int p = bannerViewPager.getCurrentItem();
                                           if (b != null) {
                                               if (p < b.size() - 1) {
                                                   bannerViewPager.setCurrentItem(p + 1, true);
                                                   webView.loadDataWithBaseURL(null, wishDetails.getCampaign().getGifts().get(p + 1).getGiftDetail(),
                                                           "text/html", "utf-8", null);
                                                   prodouctNameTextView.setText(wishDetails.getCampaign().getGifts().get(p).getName());
                                                   progressBar.setMax(wishDetails.getCampaign().getExpect());
                                                   progressBar.setProgress((int)wishDetails.getCampaign().getGifts().get(p).getNumber());
                                                   peopleNumberTextView.setText("已有"+(int)wishDetails.getCampaign().getGifts().get(p).getNumber()+"人参与");
                                                   priceTextView.setText("【市场价】¥" + (int)wishDetails.getCampaign().getGifts().get(p).getPrice()+"");

                                                   if (WApplication.getInstance().isLogin()) {
                                                       if (wishDetails.getCampaign().getAttend()) {
                                                           if (!wishDetails.getCampaign().getGifts().get(p + 1).getSelected()) {
                                                               shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
                                                               attendDetailsLinearLayout.setVisibility(View.GONE);
                                                           } else {
                                                               shareButton.setBackgroundColor(Color.parseColor("#FC4A5B"));
                                                               attendDetailsLinearLayout.setVisibility(View.VISIBLE);
                                                           }}}
                                               } else {
                                                   bannerViewPager.setCurrentItem(0, true);
                                                   webView.loadDataWithBaseURL(null, wishDetails.getCampaign().getGifts().get(0).getGiftDetail(),
                                                           "text/html", "utf-8", null);
                                                   prodouctNameTextView.setText(wishDetails.getCampaign().getGifts().get(p).getName());
                                                   progressBar.setMax(wishDetails.getCampaign().getExpect());
                                                   progressBar.setProgress((int)wishDetails.getCampaign().getGifts().get(p).getNumber());
                                                   peopleNumberTextView.setText("已有"+(int)wishDetails.getCampaign().getGifts().get(p).getNumber()+"人参与");
                                                   priceTextView.setText("【市场价】¥" + (int)wishDetails.getCampaign().getGifts().get(p).getPrice()+"");
                                                   if (WApplication.getInstance().isLogin()) {
                                                       if (wishDetails.getCampaign().getAttend()) {
                                                           if (!wishDetails.getCampaign().getGifts().get(0).getSelected()) {
                                                               shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
                                                               attendDetailsLinearLayout.setVisibility(View.GONE);
                                                           } else {
                                                               shareButton.setBackgroundColor(Color.parseColor("#FC4A5B"));
                                                               attendDetailsLinearLayout.setVisibility(View.VISIBLE);
                                                           }
                                                       }
                                                   }
                                               }
                                           }

                                       }
                                   }

        );
        shoppingTextView.setOnClickListener(new View.OnClickListener()

                                            {
                                                @Override
                                                public void onClick (View view){
                                                    Intent intent = new Intent(WishDetailsActivity.this, ShoppingActivity.class);
                                                    intent.putExtra("url", url);
                                                    startActivity(intent);
                                                }
                                            }

        );
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if(targetId==0l){
            pg = bannerViewPager.getCurrentItem();
            giftId = wishDetails.getCampaign().getGifts().get(pg).getId();
            if (progress == null){
                progress = new LoadingDialog(this);
            }
            progress.show();
            ShareModel.jingXuanShare(id,giftId).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    shareModel = arguments.get(0);
                    if (shareModel.getSuccess()) {
                        ShareUtil.showShare(WishDetailsActivity.this, shareModel, id, "wish", giftId);
//                    Message msg = handler_1.obtainMessage();
//                    msg.what = 1;
//                    Http.instance().post(ApiUrl.getJingXuanShare(id)).param("giftId",giftId).
//                            param("data", platjson).
//                            run().
//                            done(new ICallback() {
//                                @Override
//                                public void call(Arguments arguments) {
//                                    JSONObject js = arguments.get(0);
//                                    Logger.i("shares", js.toString());
//                                    WishDetailsActivity.fk = true;
//                                    Intent intent = new Intent(WishDetailsActivity.ACTION_NAME);
//                                    context.sendBroadcast(intent);
//                                }
//                            });
                    }else{
                        DialogUtil.showMessage(shareModel.getMessage());

                    }
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
        if(targetId==1l){
            WishDetailsActivity.this.finish();
        }
    }



    private void initView() {
        context = this;
        attendDetailsLinearLayout = (LinearLayout) findViewById(R.id.wish_attend_details);
        ruleTextview = (TextView) findViewById(R.id.woqu_gift_rule_count);
        topView = (TopView) findViewById(R.id.top_placelist_layout);
        bannerViewPager = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
        dotoParendLinearLayout = (LinearLayout) findViewById(R.id.doto_main_ly);
        shareButton = (Button) findViewById(R.id.wish_details_share);
        isStatus = (LinearLayout) findViewById(R.id.status_is);
        statusTextView = (TextView) findViewById(R.id.wish_details_state);
        prodouctNameTextView = (TextView) findViewById(R.id.wish_details_prodouct_name_1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        peopleNumberTextView = (TextView) findViewById(R.id.wish_details_number);
        priceTextView = (TextView) findViewById(R.id.wish_details_text_1);
        shoppingTextView = (TextView) findViewById(R.id.shopping);
        image_1 = (ImageView) findViewById(R.id.image_1);
        image_2 = (ImageView) findViewById(R.id.image_2);
        webView = (WebView) findViewById(R.id.woqu_wish_webview);
        headImage = (RoundCornerImageView) findViewById(R.id.wish_details_image);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(true);

        topView.setTitleBackVisiable();
        topView.setTitleText("活动详情");
        id = getIntent().getStringExtra("id");
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                bannerViewPager.setCurrentItem(page, true);
//            }
//        }, 5000);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    private void setupBanner(final List<BannerModel> bannerModels) {
        bannerViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        bannerViewPager.setAdapter(new WishBannerViewPagerAdapter(context, bannerViewPager,
                bannerModels));
        bannerViewPager.setPageMargin(0);
        if (bannerModels == null || bannerModels.isEmpty()) {
            return;
        }
        int count = bannerModels.size();
        int widthAndHeight = SizeUtil.dip2px(context, 7);
        dots = new ImageView[count];
        int margin = SizeUtil.dip2px(context, 10);
        dotoParendLinearLayout.removeAllViews();
        if (bannerModels.size() == 1)
            dotoParendLinearLayout.setVisibility(View.INVISIBLE);
        else
            dotoParendLinearLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < count; i++) {
            ImageView mImageView = new ImageView(context);
            dots[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(widthAndHeight, widthAndHeight));
            layoutParams.rightMargin = margin;

            mImageView.setBackgroundResource(R.drawable.pic_22);
            dotoParendLinearLayout.setGravity(Gravity.CENTER);
            dotoParendLinearLayout.addView(mImageView, layoutParams);
        }
        setImageBackground(0);

        bannerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(final int i) {
                webView.loadDataWithBaseURL(null,wishDetails.getCampaign().getGifts().get(i).getGiftDetail(),
                        "text/html","utf-8",null);
                progressBar.setProgress((int)wishDetails.getCampaign().getGifts().get(i).getNumber());
                priceTextView.setText("【市场价】¥" + (int)+wishDetails.getCampaign().getGifts().get(i).getPrice());
                peopleNumberTextView.setText("已有"+(int)wishDetails.getCampaign().getGifts().get(i).getNumber()+"人参与");
                if(wishDetails.getCampaign().getGifts().get(i).getSelected()==true){
                    isStatus.setVisibility(View.VISIBLE);
                }else{
                    isStatus.setVisibility(View.INVISIBLE);
                }
                setImageBackground(i);
                page = i;
                handler.removeCallbacks(runnable);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (page < bannerModels.size() - 1) {
                            bannerViewPager.setCurrentItem(page + 1, true);
                        } else {
                            bannerViewPager.setCurrentItem(0, true);
                        }
                    }
                };
               // handler.postDelayed(runnable, 5000);
                prodouctNameTextView.setText(wishDetails.getCampaign().getGifts().get(i).getName());
                progressBar.setMax(wishDetails.getCampaign().getExpect());
                progressBar.setProgress((int)wishDetails.getCampaign().getGifts().get(i).getNumber());
                peopleNumberTextView.setText("已有"+(int)wishDetails.getCampaign().getGifts().get(i).getNumber()+"人参与");
                priceTextView.setText("【市场价】¥" + (int)+wishDetails.getCampaign().getGifts().get(i).getPrice());
                if(WApplication.getInstance().isLogin()){
                    if (wishDetails.getCampaign().getAttend()) {
                        if (!wishDetails.getCampaign().getGifts().get(i).getSelected()) {
                            shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
                            attendDetailsLinearLayout.setVisibility(View.GONE);
                        } else {
                            shareButton.setBackgroundColor(Color.parseColor("#FC4A5B"));
                            attendDetailsLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }}
                Logger.i("banner",i+","+wishDetails.getCampaign().getGifts().get(i).getSelected());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
    private void setImageBackground(int currentPosition) {
        if (dots != null) {
            for (int i = 0; i < dots.length; i++) {
                if (i == currentPosition) {
                    dots[i].setBackgroundResource(R.drawable.pic_11);
                } else {
                    dots[i].setBackgroundResource(R.drawable.pic_22);
                }
            }
        }
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_NAME)){
                setData();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("瀑布流已结束详情页面");
        MobclickAgent.onResume(this);
    }

    public void registerBoradcastReceiver(){
        myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setData();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("瀑布流已结束详情页面");
        MobclickAgent.onPause(this);
    }
}
