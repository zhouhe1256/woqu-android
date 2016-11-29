
package com.bjcathay.woqu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.WishDetailsActivity;
import com.bjcathay.woqu.adapter.WishAdapter;
import com.bjcathay.woqu.model.CampaignModel;
import com.bjcathay.woqu.model.WoQuModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.view.AutoListView;
import com.bjcathay.woqu.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouh on 15-9-24.
 */
public class WishFragment extends Fragment {
    private View view;
    private AutoListView wishList;
    private WoQuModel wishModel;
    private String id;
    private int page = 1;
    private Boolean hasNext;
    private WishAdapter adapter;
    private List<CampaignModel> campaigns = new ArrayList<CampaignModel>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WoQuModel p = (WoQuModel) msg.obj;
            page = p.getPage();
            hasNext = p.getHasNext();
            switch (msg.what) {
                case AutoListView.REFRESH:
                    campaigns.clear();
                    campaigns.addAll(p.getCampaigns());
                    adapter.notifyDataSetChanged();
                    wishList.onRefreshComplete();
                    break;
                case AutoListView.LOAD:
                    campaigns.addAll(p.getCampaigns());
                    adapter.notifyDataSetChanged();
                    wishList.onLoadComplete();
                    break;
            }
                int size;
                if(p.getCampaigns()==null||p.getCampaigns().size()==0){
                    size = 1;
                }else{
                    size = p.getCampaigns().size();
                }
                wishList.setResultSize(size, hasNext);
                adapter.notifyDataSetChanged();


        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wish_fragment, null, false);
        initView();
        setData(AutoListView.REFRESH);
        setListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setListeners() {

            wishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long idl) {

                    id = wishModel.getCampaigns().get(position - 1).getId();
                    Intent intent = new Intent(getActivity(), WishDetailsActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("position", position - 1);
                    startActivity(intent);


                }
            });

        wishList.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.i("jk","fff");
                setData(AutoListView.REFRESH);
            }
        });
        wishList.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                setData(AutoListView.LOAD);
            }
        });
    }

    private void setData(int what) {
        switch (what) {
            case AutoListView.REFRESH:
                page = 1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }
        Logger.i("page", page + "");
        WoQuModel.getWoQuList(page + "").done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                wishModel = arguments.get(0);
                if (wishModel.getSuccess()) {
                    Message msg = handler.obtainMessage();
                    if (page == 1)
                        msg.what = AutoListView.REFRESH;
                    else {
                        msg.what = AutoListView.LOAD;
                    }
                    msg.obj = wishModel;
                    handler.sendMessage(msg);
                } else {
                    if (wishList != null) {
                        DialogUtil.showMessage(wishModel.getMessage());
                        wishList.onRefreshComplete();
                        wishList.setResultSize(-1, false);
                    }
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage("网络异常");
                if (wishList != null) {
                    wishList.onRefreshComplete();
                    wishList.setResultSize(-1, false);
                }
            }
        });

    }

    private void initView() {
        wishList = (AutoListView) view.findViewById(R.id.wish_fragment_list);
        wishList.setEmptyINVisibale(true);
        adapter = new WishAdapter(getActivity(), campaigns);
        wishList.setAdapter(adapter);
    }
}
