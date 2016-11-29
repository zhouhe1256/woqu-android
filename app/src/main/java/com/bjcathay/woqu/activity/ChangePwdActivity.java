package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ErrorCode;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.StringUtils;
import com.bjcathay.woqu.util.TimeCount;
import com.bjcathay.woqu.util.ValidformUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by jiangm on 15-9-24.
 */
public class ChangePwdActivity extends Activity implements View.OnClickListener{
    private TopView topView;
    private Button changeBt;
    private EditText oldPwd;
    private EditText newPwd;
    private EditText surePwd;
    private Button codeBt;
    private TimeCount time;
    private WApplication wApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("修改密码");
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();

    }
    private void initView() {

        oldPwd = ViewUtil.findViewById(this, R.id.edit_old_user);
        newPwd = ViewUtil.findViewById(this, R.id.edit_new_pwd);
        surePwd = ViewUtil.findViewById(this, R.id.edit_sure_new_pwd);
    }



    private void edit() {
        String pwd1 = newPwd.getText().toString();
        String pwd2 = surePwd.getText().toString();
        if (pwd1.isEmpty() || pwd2.isEmpty()||oldPwd.getText().toString().isEmpty()) {
            DialogUtil.showMessage("密码不能为空");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            DialogUtil.showMessage("两次输入的密码不一样");
            return;
        }
        if (pwd2.length() >= 6 && pwd2.length() <= 20) {
            UserModel.changePassword(oldPwd.getText().toString(),
                    newPwd.getText().toString()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("修改成功");
                        PreferencesUtils.putString(ChangePwdActivity.this,
                                PreferencesConstant.USER_PASSWORD, newPwd.getText().toString());
                        finish();
                    } else {
                        String errorMessage = jsonObject.optString("message");
                     //   if (!StringUtils.isEmpty(errorMessage))
                            DialogUtil.showMessage(errorMessage);
//                        else {
//                            int code = jsonObject.optInt("code");
//                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
//                        }
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    DialogUtil.showMessage(getString(R.string.empty_net_text));
                }
            });
        } else {
            DialogUtil.showMessage("密码长度为6~20位");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.edit_sure_btn:
                edit();
                break;
            case R.id.title_back_img:
                finish();
                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("修改密码页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改密码页面");
        MobclickAgent.onPause(this);
    }
}
