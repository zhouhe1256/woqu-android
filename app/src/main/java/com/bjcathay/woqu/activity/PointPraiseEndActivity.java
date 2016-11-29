
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.WinningListAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.WinnerListModel;
import com.bjcathay.woqu.model.WinnerModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.NotifyingScrollView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class PointPraiseEndActivity extends Activity {
    private ListView pListView;
    private ImageView backImage;
    private ImageView imageView;
    private ImageView tagImageView;
    private TextView giftTitleText;
    private TextView giftNumberText;
    private TextView giftMoneyrText;
    private RelativeLayout layoutHead;
    private View view_l;
    private NotifyingScrollView scrollView;
    protected LoadingDialog progress;
    private WebView webView;
    private TextView shoppingTextView;
    private LinearLayout winnerLinear;
    private PointPraiseModel pointPraiseModel;
    private String id;
    private String url;
    private WinnerModel winnerModel;
    private RelativeLayout winnerStatus;
    private View view;
    private RelativeLayout winnerStatusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_praise_end);
        initView();
        if (pointPraiseModel == null) {
            setData();
        }

      //  setData();
        setListeners();
        layoutHead.getBackground().setAlpha(0);
        view_l.getBackground().setAlpha(0);
    }

    private void setListeners() {
        scrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                layoutHead.getBackground().setAlpha(t < 255 ? t : 255);
                view_l.getBackground().setAlpha(t < 255 ? t : 255);
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        winnerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PointPraiseEndActivity.this, WinnerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("pointPraiseModel", pointPraiseModel);
                startActivity(intent);
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        shoppingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PointPraiseEndActivity.this, ShoppingActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }

    private void setData() {
        if(pointPraiseModel==null){
        if (progress == null) {
            progress = new LoadingDialog(this);
        }
        progress.show();
        PointPraiseModel.getActivitys(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                pointPraiseModel = arguments.get(0);
                if (pointPraiseModel.getSuccess()) {
                    setDataMethod();

                } else {
                    DialogUtil.showMessage(pointPraiseModel.getMessage());
                }
                progress.dismiss();
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
                DialogUtil.showMessage("网络异常");
            }
        });
        }
        WinnerModel.getWinner(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                winnerModel = arguments.get(0);
                if (winnerModel.getSuccess()) {
                    List<WinnerListModel> winners = new ArrayList<WinnerListModel>();
                    for (int i = 0; i < winnerModel.getWinners().size(); i++) {
                        if (i < 5) {
                            winners.add(winnerModel.getWinners().get(i));
                        }
                    }
                    if (winnerModel.getWinners().size() > 5) {
                        winnerLinear.setVisibility(View.VISIBLE);
                    } else {
                        winnerLinear.setVisibility(View.GONE);
                        // winnerLinear.setVisibility(View.VISIBLE);
                    }
                    pListView.setAdapter(
                            new WinningListAdapter(PointPraiseEndActivity.this, winners));
                    setListViewHeightBasedOnChildren(pListView);
                    if (winners.size() <= 0) {
                        winnerStatus.setVisibility(View.GONE);
                        pListView.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        winnerStatusList.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    private void setDataMethod(){
        url = pointPraiseModel.getActivity().getGift().getFriendlyLink();
        if (url != null && !url.equals("")) {
            shoppingTextView.setVisibility(View.VISIBLE);
        }
        giftTitleText.setText(pointPraiseModel.getActivity().getTitle());
        if(pointPraiseModel.getActivity().getNumber()==0){
            giftNumberText.setText("不限");

        }else {
            giftNumberText.setText(
                    pointPraiseModel.getActivity().getNumber() + "");
        }
        giftMoneyrText.setText(pointPraiseModel.getActivity().getPrice() + "0");
        webView.loadDataWithBaseURL(null,
                pointPraiseModel.getActivity().getGift().getGiftDetail(),
                "text/html", "utf-8", null);
        String tagImageUrl = pointPraiseModel.getActivity().getTagUrl();
        ImageViewAdapter.adapt(tagImageView, tagImageUrl, true);
        String imageUrl = pointPraiseModel.getActivity().getImageUrl();
        ImageViewAdapter.adapt(imageView, imageUrl, true);
    }
    private void initView() {
        view_l = findViewById(R.id.view_l);
        view = findViewById(R.id.view_l1);
        winnerStatusList = (RelativeLayout) findViewById(R.id.winner_status_list);
        layoutHead = (RelativeLayout) findViewById(R.id.head_f);
        scrollView = (NotifyingScrollView) findViewById(R.id.scrollView);
        pListView = (ListView) findViewById(R.id.woqu_gift_plist);
        backImage = (ImageView) findViewById(R.id.title_back_img);
        giftTitleText = (TextView) findViewById(R.id.woqu_gift_name);
        giftNumberText = (TextView) findViewById(R.id.woqu_gift_number);
        giftMoneyrText = (TextView) findViewById(R.id.woqu_gift_money);
        webView = (WebView) findViewById(R.id.woqu_gift_webview);
        winnerLinear = (LinearLayout) findViewById(R.id.woqu_gift_more);
        imageView = (ImageView) findViewById(R.id.woqu_gift_image);
        tagImageView = (ImageView) findViewById(R.id.woqu_gift_pb);
        shoppingTextView = (TextView) findViewById(R.id.shopping);
        winnerStatus = (RelativeLayout) findViewById(R.id.winner_status);
        winnerLinear.setVisibility(View.GONE);
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        }
//        if(getIntent().getSerializableExtra("pointPraiseModel")!=null){
//            pointPraiseModel = (PointPraiseModel) getIntent().getSerializableExtra("pointPraiseModel");
//            id = pointPraiseModel.getActivity().getId();
//            setDataMethod();
//        }
        if (getIntent().getStringExtra("id")== null) {
            pointPraiseModel = WApplication.getInstance().getpointPraiseMode();
            id = pointPraiseModel.getActivity().getId();
            setDataMethod();
        }
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(true);

    }

    /** * @param listView */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("瀑布流已结束详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("瀑布流已结束详情页面");
        MobclickAgent.onPause(this);
    }

}
