
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
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by jiangm on 15-9-24.
 */
public class BindPhoneActivity extends Activity implements View.OnClickListener,
        TimeCount.TimeUpdate, ICallback {
    private TopView topView;
    private EditText phoneEd;
    private EditText codeEd;
    private Button codeBt;
    private WApplication wApplication;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        wApplication = WApplication.getInstance();
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("绑定手机号");
        phoneEd = ViewUtil.findViewById(this, R.id.phone_num);
        codeEd = ViewUtil.findViewById(this, R.id.validata_code);
        codeBt = ViewUtil.findViewById(this, R.id.code_bt);
    }
    String platName;
    String platuserid;
    String platuserName;
    String platjson;
    String plattoken;

    private void initData() {
        time = new TimeCount(60000, 1000, this);
        Intent intent = getIntent();
        platName = intent.getStringExtra("platName");
        platuserid = intent.getStringExtra("platuserid");
        platuserName = intent.getStringExtra("platuserName");
        platjson = intent.getStringExtra("platjson");
        plattoken = intent.getStringExtra("plattoken");
    }

    private void initEvent() {

    }

    private void register() {
        String phone = phoneEd.getText().toString().trim();
        String code = codeEd.getText().toString().trim();
        if (phone.length() == 0) {
            DialogUtil.showMessage("请输入手机号码");
            return;
        }
        if (code.length() == 0) {
            DialogUtil.showMessage("请输入验证码");
            return;
        }
        UserModel
                .thirdPartyBindphone(platName, platuserid, platuserName, platjson, plattoken,
                        phone, code).done(this).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
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
            UserModel.sendCheckCode(phone, "BIND").done(new ICallback() {
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
                        if (!StringUtils.isEmpty(errorMessage))
                            DialogUtil.showMessage(errorMessage);
                        else {
                            int code = jsonObject.optInt("code");
                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    codeBt.setClickable(true);
                    DialogUtil.showMessage("网络出现故障");
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
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
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            UserModel userModel = JSONUtil.load(UserModel.class, jsonObject.optJSONObject("user"));
            String token = userModel.getApiToken();
            PreferencesUtils.putString(wApplication, PreferencesConstant.API_TOKEN, token);
            PreferencesUtils.putString(wApplication, PreferencesConstant.USER_NAME, phoneEd
                    .getText().toString().trim());
            wApplication.setUser(userModel);
            wApplication.updateApiToken();
            DialogUtil.showMessage("注册成功");
            UserModel.updateUser("pushClientId", PushManager.getInstance().getClientid(this)).done(new ICallback() {
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
    public void onTick(long millisUntilFinished) {
        codeBt.setText((millisUntilFinished / 1000) + "秒后重发");
        codeBt.setClickable(false);
    }

    @Override
    public void onFinish() {
        codeBt.setText("获取验证码");
        codeBt.setClickable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("绑定手机号页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("绑定手机号页面");
        MobclickAgent.onPause(this);
    }
}
