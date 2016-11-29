
package com.bjcathay.woqu.recyle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.ShoppingActivity;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.BannerListModel;
import com.bjcathay.woqu.model.BannerModel;
import com.bjcathay.woqu.model.WoQuActivitysModel;
import com.bjcathay.woqu.model.WoQuListModel;
import com.bjcathay.woqu.recyle.MainAdapter;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.AutoListView;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.PullToRefreshView;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-11-5.
 */
public class WoQuTextActivityFragment extends Fragment {
    private RecyclerView lst;
    private Activity context;
    private WoQuListModel woQuModel;
    private int page = 1;
    private BannerListModel bannerListModel;
    private List<WoQuActivitysModel> activities = new ArrayList<WoQuActivitysModel>();
    private View view;
    private static final String FILE_NAME = "woqu.png";
    public static String TEST_IMAGE;
    private Boolean hasNext;
    private SwipeRefreshLayout mSwipeLayout;
    public static String ACTION_NAME = "com.bjcathay.woqu.shouye";
    private IntentFilter myIntentFilter;
    private List<BannerModel> b;
    private boolean mLoadingLock = false;
    private MainAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WoQuListModel p = (WoQuListModel) msg.obj;
            page = p.getPage();
            hasNext = p.getHasNext();
            switch (msg.what) {
                case PullToRefreshView.REFRESH:
                    mSwipeLayout.setRefreshing(false);
                    activities.clear();
                    activities = p.getActivities();
                    adapter = new MainAdapter(getActivity(), b, activities);
                    lst.setAdapter(adapter);
                    break;
                case PullToRefreshView.LOAD:
                    mLoadingLock = false;
                    activities.addAll(p.getActivities());
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    };

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = ((LinearLayoutManager) layoutManager)
                    .findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            // lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
            // dy>0 表示向下滑动
            if (mLoadingLock) {
                return;
            }
            if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                if (!hasNext) {
                } else {
                    mLoadingLock = true;
                    setData(PullToRefreshView.LOAD);
                }
            }
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
            boolean enable = false;
            if (lst != null && lst.getChildCount() > 0) {
                boolean firstItemVisible = firstVisibleItem == 0;
                boolean topOfFirstItemVisible = lst.getChildAt(0).getTop() == 0;
                enable = firstItemVisible && topOfFirstItemVisible;
            }
            mSwipeLayout.setEnabled(enable);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_text_wo_qu, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initSwipeLayout();
        setListeners();
        new Thread() {
            public void run() {
                initImagePath();
            }
        }.start();
        setData(PullToRefreshView.REFRESH);
    }

    private void initSwipeLayout() {
        mSwipeLayout = (SwipeRefreshLayout) view
                .findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData(PullToRefreshView.REFRESH);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (WApplication.getInstance().refresh) {
            setData(PullToRefreshView.REFRESH);
            WApplication.getInstance().refresh = false;
        }

    }

    private void setData(int what) {
        switch (what) {
            case PullToRefreshView.REFRESH:
                page = 1;
                BannerListModel.getHomeBanners().done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        bannerListModel = arguments.get(0);
                        b = bannerListModel.getBanners();
                    }
                });
                break;
            case PullToRefreshView.LOAD:
                page++;
                break;
        }
        WoQuListModel.getWoQuList(page + "").done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                woQuModel = arguments.get(0);
                if (woQuModel.getSuccess()) {
                    Message msg = handler1.obtainMessage();
                    if (page == 1)
                        msg.what = AutoListView.REFRESH;
                    else {
                        msg.what = AutoListView.LOAD;
                    }
                    msg.obj = woQuModel;
                    handler1.sendMessage(msg);
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage("网络异常");
            }
        });
    }

    private void setListeners() {

    }

    private void initView() {
        context = getActivity();
        lst = (RecyclerView) view.findViewById(R.id.bj_woqu_list);
        lst.setHasFixedSize(false);
        adapter = new MainAdapter(getActivity(), b, activities);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lst.setLayoutManager(layoutManager);
        lst.setAdapter(adapter);
        lst.addOnScrollListener(onScrollListener);
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
                int position = intent.getIntExtra("position", 0);
                activities.get(position).setRemind(true);
                adapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    // 把图片从drawable复制到sdcard中
    private void initImagePath() {
        try {
            String cachePath = com.mob.tools.utils.R.getCachePath(getActivity(), null);
            TEST_IMAGE = cachePath + FILE_NAME;
            Logger.i("urlsw", TEST_IMAGE);
            File file = new File(TEST_IMAGE);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }

        } catch (Throwable t) {
            t.printStackTrace();
            TEST_IMAGE = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // cancelHomeDataTask();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }
}
