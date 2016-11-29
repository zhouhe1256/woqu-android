package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by jiangm on 15-9-29.
 */
public class AboutWoQuActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_woqu);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("关于喔去");
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    private void initData(){
        textView.setText("当前版本V"+getAppVersionName(this));

    }
    private void initView(){
        textView=ViewUtil.findViewById(this,R.id.version_text);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关于页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于页面");
        MobclickAgent.onPause(this);
    }

    public String getAppVersionName(Context context) {

        String versionName = "";

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }

        } catch (Exception e) {

        }
        return versionName;
    }
}
