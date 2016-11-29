package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.TimeCount;
import com.bjcathay.woqu.util.ValidformUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by jiangm on 15-9-24.
 */
public class ForgetPasswordActivity extends Activity implements View.OnClickListener, TimeCount.TimeUpdate, ICallback {
    private TopView topView;
    private EditText phoneEd;
    private EditText codeEd;
    private EditText pwdEd;
    private EditText confirmPwdEd;
    private Button codeBt;
    private TimeCount time;
    private LoadingDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("重置密码");
        initView();
        initData();
    }

    public void initView() {
        phoneEd = ViewUtil.findViewById(this, R.id.phone_num);
        codeEd = ViewUtil.findViewById(this, R.id.validata_code);
        pwdEd = ViewUtil.findViewById(this, R.id.pwd_edit);
        confirmPwdEd = ViewUtil.findViewById(this, R.id.confirm_edite);
        codeBt = ViewUtil.findViewById(this, R.id.code_bt);
        progress=new LoadingDialog(this);
    }

    public void initData() {
        time = new TimeCount(60000, 1000, ForgetPasswordActivity.this);
    }

    public void sendCheckCode() {
        String phone = phoneEd.getText().toString().trim();
        if (!ValidformUtil.isMobileNo(phone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (phone.length() > 0) {
            codeBt.setClickable(false);
            //codeBt.setBackgroundResource(R.drawable.code_click_bg);
            UserModel.sendCheckCode(phone,"FORGET_PWD").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        time.start();
                    } else {
                        time.cancel();
                        codeBt.setText("获取验证码");
                        codeBt.setClickable(true);
                        String errorMessage = jsonObject.optString("message");
                        DialogUtil.showMessage(errorMessage);
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    codeBt.setClickable(true);
                    // userCodeBtn.setBackgroundResource(R.drawable.code_bg);
                    DialogUtil.showMessage(getString(R.string.empty_net_text));
                }
            });
        }

    }

    private void forget() {
        String phone = phoneEd.getText().toString().trim();
        String code = codeEd.getText().toString().trim();
        String pwd = pwdEd.getText().toString().trim();
        String conPwd = confirmPwdEd.getText().toString().trim();
        if (phone.length() == 0) {
            DialogUtil.showMessage("请输入手机号码");
            return;
        }
        if (!ValidformUtil.isMobileNo(phone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (code.length() == 0) {
            DialogUtil.showMessage("请输入验证码");
            return;
        }
        if (pwd.isEmpty() || conPwd.isEmpty()) {
            DialogUtil.showMessage("密码不能为空");
            return;
        }

        if (!pwd.equals(conPwd)) {
            DialogUtil.showMessage("两次输入的密码不一样");
            return;
        }
        if (phone.length() > 0 && code.length() > 0)
            if (conPwd.length() >= 6 && conPwd.length() <= 20) {
                progress.show();
                UserModel.resetPassword(phone, pwdEd.getText().toString().trim(), code).done(this).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        progress.dismiss();
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                    }
                });
            }else{
                DialogUtil.showMessage("密码长度为6~20位");
            }
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        progress.dismiss();
        if (jsonObject.optBoolean("success")) {
            DialogUtil.showMessage("密码设置成功");
            PreferencesUtils.putString(ForgetPasswordActivity.this,
                    PreferencesConstant.USER_PASSWORD, "");
            PreferencesUtils.putString(ForgetPasswordActivity.this,
                    PreferencesConstant.USER_NAME, phoneEd.getText().toString().trim());
            Intent intent=new Intent(ForgetPasswordActivity.this,LoginActivity.class);
            ViewUtil.startActivity(ForgetPasswordActivity.this,intent);
            finish();
        }else {
            String errorMessage = jsonObject.optString("message");
                DialogUtil.showMessage(errorMessage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_bt:
                sendCheckCode();
                break;
            case R.id.finish_bt:
                forget();
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void onFinish() {
        codeBt.setText("获取验证码");
        codeBt.setClickable(true);
        // codeBt.setBackgroundResource(R.drawable.code_bg);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        codeBt.setText((millisUntilFinished / 1000) + "秒后重发");
        codeBt.setClickable(false);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("重置密码页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("重置密码页面");
        MobclickAgent.onPause(this);
    }
}
