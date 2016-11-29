package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by jiangm on 15-9-29.
 */
public class StatementActivity extends Activity implements View.OnClickListener{
    private TopView topView;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("免责声明");
        initView();
    }

    private void initView(){
        String url=getString(R.string.host)+"/disclaimer.html";
        webView=ViewUtil.findViewById(this,R.id.web_statement);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back_img:
                finish();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("免责声明界面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("免责声明界面");
        MobclickAgent.onPause(this);
    }
}
