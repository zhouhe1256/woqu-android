
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.WinningListAdapter;
import com.bjcathay.woqu.adapter.WinningListMoreAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.WinnerListModel;
import com.bjcathay.woqu.model.WinnerModel;
import com.bjcathay.woqu.util.StartTime;
import com.bjcathay.woqu.view.AutoListView;
import com.bjcathay.woqu.view.NotifyingScrollView;
import com.bjcathay.woqu.view.PullToRefreshView;
import com.bjcathay.woqu.view.ScrollViewWithListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class WinnerActivity extends Activity {
    private ImageView backImage;
    private ScrollViewWithListView list;
    private PullToRefreshView mPullToRefreshView;
    private WinnerModel winnerModel;
    private ImageView imageView;
    private ImageView tagImageView;
    private TextView giftNumberText;
    private TextView giftTimeText;
    private TextView giftMoneyText;
    private TextView giftNameText;
    private LinearLayout timeLinear;
    private NotifyingScrollView scrollView;
    private String id;
    private int page = 1;
    private Boolean hasNext;
    private List<WinnerListModel> winners;
    private WinningListMoreAdapter adapter;
    private RelativeLayout layoutHead;
    private View view_l;
    private PointPraiseModel pointPraiseModel;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            winnerModel = (WinnerModel) msg.obj;
            page = winnerModel.getPage();
            hasNext = winnerModel.getHasNext();
            winners = new ArrayList<WinnerListModel>();
            switch (msg.what) {
                case PullToRefreshView.REFRESH:
                    winners.clear();
                    winners = winnerModel.getWinners();
                    adapter = new WinningListMoreAdapter(WinnerActivity.this, winners);
                    list.setAdapter(adapter);
                    mPullToRefreshView.setIsLoadFull(!hasNext);
                    mPullToRefreshView.onHeaderRefreshComplete();
                    break;
                case PullToRefreshView.LOAD:
                    winners.addAll(winnerModel.getWinners());
                    adapter = new WinningListMoreAdapter(WinnerActivity.this, winners);
                    list.setAdapter(adapter);
                    mPullToRefreshView.setIsLoadFull(!hasNext);
                    mPullToRefreshView.onFooterRefreshComplete();
                    break;
            }
            adapter.notifyDataSetChanged();
            if (!hasNext) {
                mPullToRefreshView.setFooteINVisible(true);
            } else {
                mPullToRefreshView.setFooteINVisible(false);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        initView();
        setListeners();
        setData(AutoListView.REFRESH);
        layoutHead.getBackground().setAlpha(0);
        view_l.getBackground().setAlpha(0);
    }

    private void setData(int what) {
        switch (what) {
            case PullToRefreshView.REFRESH:
                page = 1;
                break;
            case PullToRefreshView.LOAD:
                page++;
                break;
        }
        WinnerModel.getWinner(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                winnerModel = arguments.get(0);
                if (winnerModel.getSuccess() && winnerModel.getWinners().size() > 0) {
                    Message msg = handler.obtainMessage();
                    if (page == 1)
                        msg.what = PullToRefreshView.REFRESH;
                    else {
                        msg.what = PullToRefreshView.LOAD;
                    }
                    msg.obj = winnerModel;
                    handler.sendMessage(msg);
                } else {
                    mPullToRefreshView.onFooterRefreshComplete();
                    mPullToRefreshView.onHeaderRefreshComplete();
                    mPullToRefreshView.setFooteINVisible(true);
                }

            }
        });
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

        mPullToRefreshView
                .setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
                    @Override
                    public void onHeaderRefresh(PullToRefreshView view) {
                        setData(PullToRefreshView.REFRESH);

                    }
                });
        mPullToRefreshView
                .setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
                    @Override
                    public void onFooterRefresh(PullToRefreshView view) {
                        setData(PullToRefreshView.LOAD);
                    }
                });
    }

    private void initView() {
        view_l = findViewById(R.id.view_l);
        giftNameText = (TextView) findViewById(R.id.woqu_gift_name);
        scrollView = (NotifyingScrollView) findViewById(R.id.scrollView);
        layoutHead = (RelativeLayout) findViewById(R.id.head_f);
        imageView = (ImageView) findViewById(R.id.woqu_gift_image);
        giftNumberText = (TextView) findViewById(R.id.woqu_gift_number);
        giftTimeText = (TextView) findViewById(R.id.woqu_gift_time);
        giftMoneyText = (TextView) findViewById(R.id.woqu_gift_money);
        tagImageView = (ImageView) findViewById(R.id.woqu_gift_pb);
        timeLinear = (LinearLayout) findViewById(R.id.time_linear);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
        backImage = (ImageView) findViewById(R.id.title_back_img);
        list = (ScrollViewWithListView) findViewById(R.id.woqu_gift_plist);
        // imageView = (ImageView) findViewById(R.id.woqu_gift_image);
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        }
        if (getIntent().getIntExtra("tag",1)==0) {
            pointPraiseModel = WApplication.getInstance().getpointPraiseMode();

        }
        giftNameText.setText(pointPraiseModel.getActivity().getGift().getName());
        ImageViewAdapter.adapt(
                imageView,
                pointPraiseModel.getActivity().getImageUrl(),true);
        ImageViewAdapter.adapt(
                tagImageView,
                pointPraiseModel.getActivity().getTagUrl(),true);

        if(pointPraiseModel.getActivity().getNumber()==0){
            giftNumberText.setText("不限");

        }else {
            giftNumberText.setText(
                    pointPraiseModel.getActivity().getNumber() + "");
        }
        giftMoneyText.setText(
                pointPraiseModel.getActivity().getPrice() + "0");
        if (pointPraiseModel.getActivity().getStatus() == 2) {
            timeLinear.setVisibility(View.INVISIBLE);
        } else {
            timeLinear.setVisibility(View.VISIBLE);
            String time = null;
            if (pointPraiseModel.getActivity().getStatus() == 0) {
                time = StartTime.setStartTime(
                        pointPraiseModel.getActivity().getStartAt(),
                        pointPraiseModel.getActivity().getNow());
            } else if (pointPraiseModel.getActivity().getStatus() == 1) {
                time = StartTime.setStartTime(
                        pointPraiseModel.getActivity().getEndAt(),
                        pointPraiseModel.getActivity().getNow());
            }
            giftTimeText.setText(time);
        }

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
        MobclickAgent.onPageStart("中奖名单页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("中奖名单页面");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WApplication.getInstance().clearPointPraiseModel();
    }
}
