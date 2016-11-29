
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.InvolvedAdapter;
import com.bjcathay.woqu.adapter.OrderAdapter;
import com.bjcathay.woqu.adapter.WarehouseAdapter;
import com.bjcathay.woqu.model.DistrictListModel;
import com.bjcathay.woqu.model.GiftListModel;
import com.bjcathay.woqu.model.GiftModel;
import com.bjcathay.woqu.model.OrderModel;
import com.bjcathay.woqu.model.OrdersListModel;
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
public class MyOrderActivity extends Activity implements View.OnClickListener,
        AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback {
    private TopView topView;
    private AutoListView listView;
    private List<OrderModel> orderModels;
    private OrderAdapter orderAdapter;
    private int page = 1;
    private String DELIVERED = "已发货";
    private String DISTRIBUTION = "配送中";
    private String REACH = "已到达";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的订单");
        context = this;
        initView();
        initData();
        initEvent();
    }

    public void initView() {

        listView = ViewUtil.findViewById(this, R.id.listView);
        orderModels = new ArrayList<OrderModel>();
        orderAdapter = new OrderAdapter(this, orderModels);
        listView.setAdapter(orderAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setListViewEmptyImage(R.drawable.ic_empty_order);
        listView.setListViewEmptyMessage(getString(R.string.empty_free_order_text));

    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    public void initEvent() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= orderModels.size()) {
                }
                OrderModel orderModel = orderModels.get(position - 1);
                String expressId = orderModel.getExpressOrderId();

                if (expressId == null || expressId.isEmpty()) {
                    Intent intent = new Intent(MyOrderActivity.this, WaitingDeliveryActivity.class);
                    intent.putExtra("order", orderModel);
                    ViewUtil.startActivity(context, intent);
                } else {
                    Intent intent = new Intent(MyOrderActivity.this,
                            DistributionOrArrivalActivity.class);
                    intent.putExtra("order", orderModel);
                    ViewUtil.startActivity(context, intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_img:
                if (!getIntent().getBooleanExtra("pay", false))
                    finish();
                else {
                    Intent intent = new Intent(this, MainActivity.class);
                    ViewUtil.startTopActivity(this, intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!getIntent().getBooleanExtra("pay", false))
            finish();
        else {
            Intent intent = new Intent(this, MainActivity.class);
            ViewUtil.startTopActivity(this, intent);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            OrdersListModel result = (OrdersListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getOrders() != null && !result.getOrders().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        orderModels.clear();
                        orderModels.addAll(result.getOrders());
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        orderModels.addAll(result.getOrders());
                        break;
                }
                listView.setResultSize(orderModels.size(), hasNext);
                orderAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        break;
                }
                listView.setResultSize(orderModels.size(), hasNext);
                orderAdapter.notifyDataSetChanged();
            }
        }

        ;
    };

    private void loadData(final int what) {
        switch (what) {
            case AutoListView.REFRESH:
                page = 1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }
        OrdersListModel.achieveOrder(page).done(this).fail(new ICallback() {
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
        OrdersListModel ordersListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = ordersListModel;
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的订单页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的订单页面");
        MobclickAgent.onPause(this);
    }
}
