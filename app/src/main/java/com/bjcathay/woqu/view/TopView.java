
package com.bjcathay.woqu.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.util.ViewUtil;


/**
 * Created by jiangm on 15-9-24.
 */
public class TopView extends LinearLayout {
    private TextView leftbtn;
    private TextView rightbtn;
    private TextView savebtn;
    private TextView managebtn;
    private TextView title;
    private Activity activity;
    private ImageView titleBack;
    private ImageView homeBack;
    private ImageView share;
    private ImageView setting;
    private ImageView searchimg;
    private ImageView exchange;
    private ImageView closeimg;
    private ImageView phoneimg;
    private TextView finishbtn;
    private ImageView help;
    private TextView deletebtn;
    private TextView agreebtn;
    private TextView sendbtn;


    private void initView(final Context context) {
        View.inflate(context, R.layout.activity_title, this);
        leftbtn = ViewUtil.findViewById(this, R.id.title_back);
       // rightbtn = ViewUtil.findViewById(this, R.id.title_right);
        savebtn = ViewUtil.findViewById(this, R.id.title_save);
        managebtn = ViewUtil.findViewById(this, R.id.title_manager);
        title = ViewUtil.findViewById(this, R.id.title_title);
        titleBack = ViewUtil.findViewById(this, R.id.title_back_img);
        // homeBack = ViewUtil.findViewById(this, R.id.home_back_img);
//        share = ViewUtil.findViewById(this, R.id.title_share_img);
//        setting = ViewUtil.findViewById(this, R.id.title_setting_img);
//        searchimg = ViewUtil.findViewById(this, R.id.title_search_img);
//        exchange = ViewUtil.findViewById(this, R.id.title_exchange_img);
//        closeimg = ViewUtil.findViewById(this, R.id.title_delete_img);
//        phoneimg = ViewUtil.findViewById(this, R.id.title_phone_img);
        finishbtn = ViewUtil.findViewById(this, R.id.title_finish);
//        help=ViewUtil.findViewById(this,R.id.title_help_img);
        deletebtn=ViewUtil.findViewById(this,R.id.title_delete);
        agreebtn=ViewUtil.findViewById(this,R.id.title_agree);
        sendbtn=ViewUtil.findViewById(this,R.id.title_send);
        leftbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity != null) {
                    activity.finish();
                }
            }
        });

    }

    public void setTitleBackVisiable() {
        titleBack.setVisibility(VISIBLE);
    }

    public void setHomeBackVisiable() {
        homeBack.setVisibility(VISIBLE);
    }
    public ImageView getHomeBack(){
        return homeBack;
    }

    public void setShareVisiable() {
        share.setVisibility(GONE);
    }

    public void setSettingVisiable() {
        setting.setVisibility(VISIBLE);
    }

    public void setSearchVisiable() {
        searchimg.setVisibility(VISIBLE);
    }

    public void setExchangeVisiable() {
        exchange.setVisibility(VISIBLE);
    }

    public void setSaveBtnVisiable() {
        savebtn.setVisibility(VISIBLE);
    }

    public void setFinishVisiable() {
        finishbtn.setVisibility(VISIBLE);
    }

    public void setManagebtnVisiable() {
        managebtn.setVisibility(VISIBLE);
    }

    public void setPhonebtnVisiable() {
        phoneimg.setVisibility(VISIBLE);
    }

    public void setDeleteVisiable() {
        closeimg.setVisibility(VISIBLE);
    }
    public void setDeleteAllVisiable() {
        deletebtn.setVisibility(VISIBLE);
    }
    public void setDeleteAllInVisiable() {
        deletebtn.setVisibility(INVISIBLE);
    }
    public void setTitleUnVisiable() {
        title.setVisibility(INVISIBLE);
    }
    public void setTitleHelpVisiable(){
        help.setVisibility(VISIBLE);
    }
    public void setTitleAgreeVisiable(){agreebtn.setVisibility(VISIBLE);}
    public void setTitleSendVisiable() {
        sendbtn.setVisibility(VISIBLE);
    }
    /**
     * 设置控件可见性
     */
    public void setVisiable(int leftVisiable, int titleVisiable, int rightVisiable) {
        title.setVisibility(titleVisiable);
        leftbtn.setVisibility(leftVisiable);
        rightbtn.setVisibility(rightVisiable);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setContext(Context context) {

    }

    public void setTitleText(String titleText) {
        title.setText(titleText);
    }

    public void setLeftbtnText(String leftText, int resid) {
        if (leftText != null)
            leftbtn.setText(leftText);
        leftbtn.setBackgroundResource(resid);
    }

    public void setRightbtn(String rightText, int resid) {
        rightbtn.setText(rightText);
        if (resid != 0)
            rightbtn.setBackgroundResource(resid);
    }

    public TextView getRightbtn() {
        return rightbtn;
    }

    public TextView getTitle() {
        return title;

    }

    public TopView(Context context) {
        super(context);
        initView(context);
    }

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public TopView(Context context, Activity activity) {
        super(context);
        this.activity = activity;
        initView(context);
    }
}
