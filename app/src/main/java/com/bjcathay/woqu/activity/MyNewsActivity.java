package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.NewsAdapter;
import com.bjcathay.woqu.adapter.WarehouseAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.CampaignModel;
import com.bjcathay.woqu.model.ConsigneeModel;
import com.bjcathay.woqu.model.GiftListModel;
import com.bjcathay.woqu.model.GiftModel;
import com.bjcathay.woqu.model.MessageListModel;
import com.bjcathay.woqu.model.MessageModel;
import com.bjcathay.woqu.model.PointPraiseListModel;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.WoQuActivitysModel;
import com.bjcathay.woqu.model.WoQuListModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.AutoListView;
import com.bjcathay.woqu.view.DeleteInfoDialog;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jiangm on 15-9-29.
 */
public class MyNewsActivity extends Activity implements DeleteInfoDialog.DeleteInfoDialogResult,View.OnClickListener,AutoListView.OnLoadListener,AutoListView.OnRefreshListener,ICallback{
    private TopView topView;
    private AutoListView listView;
    private List<MessageModel> messageModels;
    private int page=1;
    private NewsAdapter newsAdapter;
    private WApplication wApplication;
    Context context;
    private int pos;
    private long DELETE=1l;
    private long DELETEALL=2l;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_news);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的消息");
        wApplication=WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        context=this;
        initView();
        initData();
        initEvent();
    }

    public void initView() {
        listView = ViewUtil.findViewById(this, R.id.listView);
        messageModels = new ArrayList<MessageModel>();
        newsAdapter = new NewsAdapter(this, messageModels);
        listView.setAdapter(newsAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setListViewEmptyImage(R.drawable.ic_empty_message);
        listView.setListViewEmptyMessage(getString(R.string.empty_free_message_text));
        loadingDialog=new LoadingDialog(this);
    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }
    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                readMsg(messageModels.get(position - 1).getId());
                MessageModel messageModel=messageModels.get(position-1);

                String  target=messageModel.getTarget();
                if(messageModel.getType().equals("CAMPAIGN")){
                    loadingDialog.show();
                    Intent intent=new Intent(context,WishDetailsActivity.class);
                    intent.putExtra("id",messageModel.getTarget());
                    ViewUtil.startActivity(context,intent);
                }else if(messageModel.getType().equals("ACTIVITY")){
                    loadingDialog.show();
                    PointPraiseModel.getActivitys(target).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            PointPraiseModel pointPraiseModel = arguments.get(0);
                            Intent intent;
                            if (pointPraiseModel.getSuccess()) {
                                if (pointPraiseModel.getActivity().getStatus() == 2) {
                                    intent = new Intent(context, PointPraiseEndActivity.class);
                                } else {
                                    intent = new Intent(context, PointPraiseActivity.class);
                                }
                                loadingDialog.dismiss();
                                intent.putExtra("id", pointPraiseModel.getActivity().getId());
                                ViewUtil.startActivity(context, intent);
                            } else {
                                JSONObject jsonObject = arguments.get(0);
                                try {
                                    DialogUtil.showMessage(jsonObject.getString("message"));
                                } catch (JSONException e) {

                                }

                            }

                        }
                    }).fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            DialogUtil.showMessage(getString(R.string.empty_net_text));
                            loadingDialog.dismiss();
                        }
                    });


                }else if(messageModel.getType().equals("AD")){
                    loadingDialog.show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(messageModel.getTarget()));
                    intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    ViewUtil.startActivity(context,intent);
                }

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position - 1;
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(MyNewsActivity.this,
                        R.style.InfoDialog, "确认删除此消息", DELETE, MyNewsActivity.this);
                infoDialog.show();
                return false;
            }
        });
    }

    private void removeAll(){
        MessageListModel.emptyMsg().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject=arguments.get(0);
                try {
                    if(jsonObject.getBoolean("success")){
                        messageModels.clear();
                        newsAdapter.notifyDataSetChanged();
                        topView.setDeleteAllInVisiable();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void readMsg(long id){
        MessageListModel.readMsg(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject=arguments.get(0);
                try {
                    if(jsonObject.getBoolean("success")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);

    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if(isDelete){
            if(targetId==DELETE){
                MessageListModel.deleteMsg(messageModels.get(pos).getId()).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject=arguments.get(0);
                        messageModels.remove(pos);
                        newsAdapter.notifyDataSetChanged();
                        boolean flag = jsonObject.optBoolean("success");
                        if(flag==true){
                            DialogUtil.showMessage("删除成功!");
                        }

                    }
                });
            }else{
                removeAll();


            }


        }

    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_delete:
                if(!messageModels.isEmpty()){
                    DeleteInfoDialog infoDialog = new DeleteInfoDialog(MyNewsActivity.this,
                            R.style.InfoDialog, "确认清空消息?",DELETEALL,MyNewsActivity.this);
                    infoDialog.show();
                }else{
                    // DialogUtil.showMessage(getString(R.string.empty_free_message_text));
                }
                break;
        }
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
        MessageListModel.msgList(page).done(this).fail(new ICallback() {
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
        MessageListModel messageListModel=arguments.get(0);
        if(messageListModel.getMessages().isEmpty()||messageListModel.getMessages()==null){
            topView.setDeleteAllInVisiable();
        }else{
            topView.setDeleteAllVisiable();
        }
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = messageListModel;
        handler.sendMessage(msg);
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MessageListModel result = (MessageListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getMessages() != null && !result.getMessages().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        messageModels.clear();
                        messageModels.addAll(result.getMessages());
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        messageModels.addAll(result.getMessages());
                        break;
                }
                listView.setResultSize(messageModels.size(), hasNext);
                newsAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        break;
                }
                listView.setResultSize(messageModels.size(), hasNext);
                newsAdapter.notifyDataSetChanged();
            }
        }

        ;
    };
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的消息");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的消息");
        MobclickAgent.onPause(this);
        loadingDialog.dismiss();
    }
}
