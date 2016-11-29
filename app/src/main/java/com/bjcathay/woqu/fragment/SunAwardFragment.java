
package com.bjcathay.woqu.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.SunAwardAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.TalkModel;
import com.bjcathay.woqu.model.TalkListModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.view.AutoListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouh on 15-9-24.
 */
public class SunAwardFragment extends Fragment {
    private View view;
    private AutoListView sunAwardList;
    private TalkListModel talk;
    private int page = 1;
    private List<TalkModel> talks = new ArrayList<TalkModel>();
    private SunAwardAdapter adapter;
    private Boolean hasNext;
    public static String ACTION_NAME = "com.bjcathay.woqu";
    private IntentFilter myIntentFilter;

    public SunAwardFragment() {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TalkListModel p = (TalkListModel) msg.obj;
            hasNext = p.getHasNext();
            page = p.getPage();
            switch (msg.what) {
                case AutoListView.REFRESH:
                    talks.clear();
                    talks = p.getTalks();
                    setcontent();
                    sunAwardList.onRefreshComplete();
                    break;
                case AutoListView.LOAD:
                    talks.addAll(p.getTalks());
                    // setcontent();
                    sunAwardList.onLoadComplete();
                    break;
            }
            sunAwardList.setResultSize(p.getTalks().size(), hasNext);

            adapter.notifyDataSetChanged();

        }
    };

    @SuppressLint("ValidFragment")
    public SunAwardFragment(TalkListModel talk) {
        this.talk = talk;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sunaward_fragment, null, false);
        WApplication.getInstance();
        initView();
        registerBoradcastReceiver();
        setData(AutoListView.REFRESH);
        setcontent();
        setListeners();

        return view;
    }

    private void setData(final int what) {
        switch (what) {
            case AutoListView.REFRESH:
                page = 1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }

        TalkListModel.talks(page + "").done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                TalkListModel t = arguments.get(0);
                if (t.getSuccess()) {
                    Message msg = handler.obtainMessage();
                    if (page == 1)
                        msg.what = AutoListView.REFRESH;
                    else {
                        msg.what = AutoListView.LOAD;
                    }
                    msg.obj = t;
                    handler.sendMessage(msg);
                }

            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage("网络异常");
                if (sunAwardList != null) {
                    sunAwardList.onRefreshComplete();
                    sunAwardList.setResultSize(-1, false);
                }
            }
        });
    }

    private void setListeners() {
        sunAwardList.setListViewEmptyImage(R.drawable.yuechang);
        sunAwardList.setListViewEmptyMessage("没有查到相关信息～");

        sunAwardList.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData(AutoListView.REFRESH);
            }
        });
        sunAwardList.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                setData(AutoListView.LOAD);
            }
        });
    }

    private void setcontent() {
        adapter = new SunAwardAdapter(getActivity(), talks);
        sunAwardList.setAdapter(adapter);

    }

    private void initView() {
        sunAwardList = (AutoListView) view.findViewById(R.id.sunaward_fragment_list);

    }

    public void registerBoradcastReceiver() {
        myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_NAME)) {

                if (intent.getIntExtra("tag", 1) == 0) {
                    boolean ifzan = intent.getBooleanExtra("ifzan", true);
                    String xiaoxi = intent.getStringExtra("xiaoxi");
                    int position = intent.getIntExtra("position", 0);
                    int zanNumber = intent.getIntExtra("zanNumber", 0);
                    Logger.i("brocast", "zanNumber" + zanNumber);
                    adapter.setInformation(ifzan, xiaoxi, position, zanNumber);
                } else {
                    talks.clear();
                    setData(AutoListView.REFRESH);
                }
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
