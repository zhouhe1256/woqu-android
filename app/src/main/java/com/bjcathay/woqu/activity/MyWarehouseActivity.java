
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.NewsAdapter;
import com.bjcathay.woqu.adapter.WarehouseAdapter;
import com.bjcathay.woqu.model.GiftListModel;
import com.bjcathay.woqu.model.GiftModel;
import com.bjcathay.woqu.util.ShareUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.AutoListView;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangm on 15-9-29.
 */
public class MyWarehouseActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, View.OnClickListener, ICallback {
    TopView topView;
    AutoListView listView;
    Context context;
    List<Map<String, String>> list;
    private int page = 1;
    List<GiftModel> giftModels;
    WarehouseAdapter warehouseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_warehouse);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        context = this;
        topView.setTitleBackVisiable();
        topView.setTitleText("我的仓库");
        initView();
        initData();
        initEvent();
    }

    public void initView() {
        listView = ViewUtil.findViewById(this, R.id.listView);
        giftModels = new ArrayList<GiftModel>();
        warehouseAdapter = new WarehouseAdapter(this, giftModels);
        listView.setAdapter(warehouseAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setListViewEmptyImage(R.drawable.ic_empty_gift);
        listView.setListViewEmptyMessage(getString(R.string.empty_free_gift_text));

    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= giftModels.size()) {
                    Intent intent = new Intent(MyWarehouseActivity.this,
                            CommodityDetailsActivity.class);
                    intent.putExtra("gift", giftModels.get(position - 1));
                    ViewUtil.startActivity(MyWarehouseActivity.this, intent);

                }
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            GiftListModel result = (GiftListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getGifts() != null && !result.getGifts().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        giftModels.clear();
                        giftModels.addAll(result.getGifts());
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        giftModels.addAll(result.getGifts());
                        break;
                }
                listView.setResultSize(giftModels.size(), hasNext);
                warehouseAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        break;
                }
                listView.setResultSize(giftModels.size(), hasNext);
                warehouseAdapter.notifyDataSetChanged();
            }
        }

        ;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的仓库页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的仓库页面");
        MobclickAgent.onPause(this);
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
        GiftListModel.getGift(page).done(this).fail(new ICallback() {
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
        GiftListModel giftListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = giftListModel;
        handler.sendMessage(msg);
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

}
