
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.AddressAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ErrorCode;
import com.bjcathay.woqu.model.ConsigneeListModel;
import com.bjcathay.woqu.model.ConsigneeModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by jiangm on 15-9-24.
 */
public class MyAddressActivity extends Activity implements View.OnClickListener {
    TopView topView;
    ListView listView;
    List<ConsigneeModel> consignees;
    WApplication wApplication;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_address);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的收货地址");
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }

    public void initView() {
        listView = ViewUtil.findViewById(this, R.id.address_list);
        linearLayout=ViewUtil.findViewById(this,R.id.empty_layout);
    }

    public void initData() {

        ConsigneeListModel.ConsigneeList().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                ConsigneeListModel consigneeListModel = arguments.get(0);
                consignees = consigneeListModel.getConsignees();
                if (consignees != null && !consignees.isEmpty()) {
                    listView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    AddressAdapter addressAdapter = new AddressAdapter(MyAddressActivity.this,
                            consignees);
                    listView.setAdapter(addressAdapter);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyAddressActivity.this, EditAddressActivity.class);
                intent.putExtra("consignee",consignees.get(position));
                ViewUtil.startActivity(MyAddressActivity.this, intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.add_address:
                Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);

                ViewUtil.startActivity(MyAddressActivity.this, intent);
                break;
            case R.id.title_back_img:
                finish();
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
        MobclickAgent.onPageStart("我的收货地址");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的收货地址");
        MobclickAgent.onPause(this);
    }
}
