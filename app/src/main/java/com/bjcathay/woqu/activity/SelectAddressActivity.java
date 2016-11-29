
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.SelectAddressAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ErrorCode;
import com.bjcathay.woqu.model.ConsigneeListModel;
import com.bjcathay.woqu.model.ConsigneeModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.IsLoginUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-10-20.
 */
public class SelectAddressActivity extends Activity implements View.OnClickListener {
    TopView topView;
    ListView listView;
    List<ConsigneeModel> consignees;
    WApplication wApplication;
    SelectAddressAdapter addressAdapter;
    ConsigneeModel defaultConsignee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_address);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setManagebtnVisiable();
        topView.setTitleText("选择收货地址");
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }

    public void initView() {
        listView = ViewUtil.findViewById(this, R.id.address_list);
        findViewById(R.id.add_address).setVisibility(View.GONE);
        findViewById(R.id.address_list).setVisibility(View.VISIBLE);
    }

    public void initData() {
        Intent intent = getIntent();
        defaultConsignee = (ConsigneeModel) intent.getSerializableExtra("defaultConsignee");
        if (consignees!=null&&!consignees.isEmpty())
            consignees.clear();
        ConsigneeListModel.ConsigneeList().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                ConsigneeListModel consigneeListModel = arguments.get(0);
                consignees.addAll(consigneeListModel.getConsignees());
                if (consignees != null && !consignees.isEmpty()) {
                    if (defaultConsignee != null)
                        for (int i = 0; i < consignees.size(); i++) {
                            if (consignees.get(i).getId() == defaultConsignee.getId()) {
                                addressAdapter.setCur_pos(i);
                                break;
                            }
                        }
                    else
                        addressAdapter.notifyDataSetChanged();
                } else {
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                String errorMessage = jsonObject.optString("message");
                if (!errorMessage.isEmpty())
                    DialogUtil.showMessage(errorMessage);
                else {
                    int code = jsonObject.optInt("code");
                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
                }
            }
        });

    }

    public void initEvent() {
        consignees = new ArrayList<>();
        addressAdapter = new SelectAddressAdapter(SelectAddressActivity.this,
                consignees);
        listView.setAdapter(addressAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= consignees.size()) {
                    addressAdapter.setCur_pos(position);
                    Logger.e("select address", "" + position);
                     Intent intent = new Intent();
                     intent.putExtra("consignee", consignees.get(position));
                     setResult(2, intent);
                     finish();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.add_address:
                break;
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_manager:
                intent = new Intent(this, MyAddressActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择收货地址");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择收货地址");
        MobclickAgent.onPause(this);
    }
}
