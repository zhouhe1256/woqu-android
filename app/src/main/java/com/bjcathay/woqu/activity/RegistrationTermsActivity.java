package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;

/**
 * Created by jiangm on 15-10-22.
 */
public class RegistrationTermsActivity extends Activity implements View.OnClickListener{
    TopView topView;
    boolean flag;
    WApplication wApplication;
    private WebView webView;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqistration_terms);
        topView= ViewUtil.findViewById(this,R.id.top_title);
        topView.setTitleBackVisiable();
       // topView.setTitleAgreeVisiable();
        wApplication=WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        topView.setTitleText("服务条款及隐私规则");
        initView();
    }
    private void initView(){
        webView=ViewUtil.findViewById(this,R.id.web_registration);
        String url=getString(R.string.host)+"/disclaimer.html";
       // webView.getSettings().setJavaScriptEnabled(true);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back_img:
                finish();
                break;
//            case R.id.title_agree:
//                agree();
//                finish();
//                break;
        }
    }
    public void agree(){
        wApplication.setAgreeflag(true);
    }
}
