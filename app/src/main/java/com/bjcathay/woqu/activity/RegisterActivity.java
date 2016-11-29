
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.SystemUtil;
import com.bjcathay.woqu.util.TimeCount;
import com.bjcathay.woqu.util.ValidformUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.TopView;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Created by jiangm on 15-9-24.
 */
public class RegisterActivity extends Activity implements View.OnClickListener,
        TimeCount.TimeUpdate, ICallback {
    private TopView topView;
    private Button registerBt;
    private EditText phoneEd;
    private EditText codeEd;
    private EditText pwdEd;
    private Button codeBt;
    private TimeCount time;
    private WApplication wApplication;
    private TextView clauseText;
    private boolean flag = true;
    private CheckBox checkBox;
    private LoadingDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("注册");
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }

    public void initView() {
        // registerBt=ViewUtil.findViewById(this,R.id.register_bt);
        phoneEd = ViewUtil.findViewById(this, R.id.phone_edit);
        codeEd = ViewUtil.findViewById(this, R.id.code_edit);
        pwdEd = ViewUtil.findViewById(this, R.id.pwd_edit);
        codeBt = ViewUtil.findViewById(this, R.id.code_bt);
        clauseText = ViewUtil.findViewById(this, R.id.clause_text);
        checkBox = ViewUtil.findViewById(this, R.id.radioButton);
        progress=new LoadingDialog(this);
    }

    public void initData() {
        time = new TimeCount(60000, 1000, RegisterActivity.this);
        clauseText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        // radioButton.setChecked(wApplication.isAgreeflag());
    }
    public void initEvent(){

        clauseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, RegistrationTermsActivity.class);
                ViewUtil.startActivity(RegisterActivity.this, intent);
            }
        });
    }

    public void sendCheckCode() {
        String phone = phoneEd.getText().toString().trim();
        if (!ValidformUtil.isMobileNo(phone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (phone.length() > 0) {
            codeBt.setClickable(false);
            // codeBt.setBackgroundResource(R.drawable.code_click_bg);
            UserModel.sendCheckCode(phone,"REGISTER").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        time.start();
                    } else {
                        time.cancel();
                        codeBt.setText("获取验证码");
                        codeBt.setClickable(true);
                        // codeBt.setBackgroundResource(R.drawable.code_bg);
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

    private void register() {
        String phone = phoneEd.getText().toString().trim();
        String password = pwdEd.getText().toString();
        String code = codeEd.getText().toString().trim();
        // String inviteCode = userInvite.getText().toString().trim();
        if (phone.length() == 0) {
            DialogUtil.showMessage("请输入手机号码");
            return;
        }
        if (code.length() == 0) {
            DialogUtil.showMessage("请输入验证码");
            return;
        }
        if (checkBox.isChecked() == false) {
            DialogUtil.showMessage("请阅读并同意法律条款及隐私规则");
            return;
        }
        if (password.length() >= 6 && password.length() <= 18) {
            if (phone.length() > 0 && password.length() > 0 && code.length() > 0)
                progress.show();
                UserModel.register(phone, password, code).done(this)
                        .fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage(getString(R.string.empty_net_text));
                            }
                        });
        } else {
            DialogUtil.showMessage("密码长度必须大于6位小于18位");
        }
    }

    @Override
    public void call(Arguments arguments) {
        progress.dismiss();
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            WApplication.getInstance().refresh = true;
            UserModel userModel = JSONUtil.load(UserModel.class, jsonObject.optJSONObject("user"));
            String token = userModel.getApiToken();
            PreferencesUtils.putString(wApplication, PreferencesConstant.API_TOKEN, token);
            PreferencesUtils.putString(wApplication, PreferencesConstant.USER_NAME, phoneEd
                    .getText().toString().trim());
            PreferencesUtils.putString(wApplication, PreferencesConstant.USER_PASSWORD, pwdEd
                    .getText().toString());
            PreferencesUtils.putString(wApplication, PreferencesConstant.NICK_NAME,
                    userModel.getNickname());

            wApplication.setUser(userModel);
            wApplication.updateApiToken();
            DialogUtil.showMessage("注册成功");
            UserModel.updateUser("pushClientId", PushManager.getInstance().getClientid(this)).done(
                    new ICallback() {
                        @Override
                        public void call(Arguments arguments) {

                        }
                    });
            Intent intent = new Intent(this, MainActivity.class);
            ViewUtil.startTopActivity(this, intent);
        } else {
            String errorMessage = jsonObject.optString("message");

            DialogUtil.showMessage(errorMessage);

        }
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.register_bt:

                register();
                break;
            case R.id.code_bt:
                sendCheckCode();
                break;
            case R.id.title_back_img:

                finish();
                break;


        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        codeBt.setText((millisUntilFinished / 1000) + "秒后重发");
        codeBt.setClickable(false);
        // codeBt.setBackgroundResource(R.drawable.code_click_bg);
    }

    @Override
    public void onFinish() {
        codeBt.setText("获取验证码");
        codeBt.setClickable(true);
        // codeBt.setBackgroundResource(R.drawable.code_bg);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // flag=wApplication.isAgreeflag();
        // radioButton.setChecked(wApplication.isAgreeflag());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("注册界面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("注册界面");
        MobclickAgent.onPause(this);
    }

}
