
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ErrorCode;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.SystemUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by jiangm on 15-9-24.
 */
public class LoginActivity extends Activity implements View.OnClickListener, ICallback,
        PlatformActionListener, Handler.Callback {
    private WApplication wApplication;
    private EditText userName;
    private EditText userPwd;
    private String name;
    private String password;
    private TopView topView;
    private boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("登录");
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        // thirdLogin();
        initView();
        initData();
        handler = new Handler(this);
    }

    public void initView() {
        userName = ViewUtil.findViewById(this, R.id.user_name);
        userPwd = ViewUtil.findViewById(this, R.id.user_pwd);
    }

    public void initData() {
        String user_name = PreferencesUtils.getString(wApplication, PreferencesConstant.USER_NAME,
                "");
        String pass_word = PreferencesUtils.getString(wApplication,
                PreferencesConstant.USER_PASSWORD, "");
        userName.setText(user_name);
        userPwd.setText(pass_word);
    }

    public void login() {
        flag=false;
        name = userName.getText().toString();

        password = userPwd.getText().toString();
        if (name.isEmpty() || password.isEmpty()) {
            DialogUtil.showMessage("用户名或密码不能为空");
            return;
        }
        UserModel.login(name, password).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
                flag=true;
            }
        });
    }

    @Override
    public void call(Arguments arguments) {
        flag=true;
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            WApplication.getInstance().refresh = true;
            UserModel userModel = JSONUtil.load(UserModel.class, jsonObject.optJSONObject("user"));
            String token = userModel.getApiToken();
            String nickname=userModel.getNickname();
            // 保存用户名和密码
            PreferencesUtils.putString(wApplication, PreferencesConstant.USER_NAME, userName
                    .getText().toString().trim());
            PreferencesUtils.putString(wApplication, PreferencesConstant.USER_PASSWORD, userPwd
                    .getText().toString().trim());
            PreferencesUtils.putString(wApplication, PreferencesConstant.API_TOKEN, token);
            PreferencesUtils.putString(wApplication, PreferencesConstant.NICK_NAME, nickname);
            wApplication.setUser(userModel);
            wApplication.updateApiToken();
            DialogUtil.showMessage("登录成功");
            UserModel.updateUser("pushClientId", PushManager.getInstance().getClientid(this)).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {

                }
            });
            finish();
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
        Intent intent;
        switch (v.getId()) {
            case R.id.login_bt:

                if(flag){
                    login();
                }
                break;
            case R.id.register_bt:
                intent = new Intent(this, RegisterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.forgetpwd_text:
                intent = new Intent(this, ForgetPasswordActivity.class);
                ViewUtil.startActivity(this, intent);
                finish();
                break;
            case R.id.title_back_img:
                finish();
                break;
            case R.id.login_wechat:
                ShareSDK.initSDK(this);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.login_qq:
                ShareSDK.initSDK(this);
                // qq
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;
            case R.id.login_sinaweibo:
                ShareSDK.initSDK(this);
                // 新浪微博
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("登录页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登录页面");
        MobclickAgent.onPause(this);
    }

    // 第三方登录
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    private Handler handler;
    // 执行授权,获取用户信息
    // 文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {
        plat.setPlatformActionListener(this);
        // 关闭SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        // 解析部分用户资料字段
        String resjson = JSONUtil.dump(res);
        Logger.e("resjson", resjson);
        if (action == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();// 获取数平台数据DB
            Logger.e("json", platDB.exportData());
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[] {
                    platform.getName(), res, platform
            };
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        t.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    @Override
    public boolean handleMessage(final Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消授权
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                // 授权失败
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                // 授权成功
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                Object[] objs = (Object[]) msg.obj;
                Platform platform = (Platform) objs[2];
                PlatformDb platDB = platform.getDb();// 获取数平台数据DB
                final String platName = platform.getName();
                final String platuserid = platDB.getUserId();
                final String platuserName = platDB.getUserName();
                final String platjson = platDB.exportData();
                final String plattoken = platDB.getToken();
                UserModel.thirdPartyLogin(platName, platuserid, platuserName, platjson, plattoken)
                        .done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    UserModel userModel = JSONUtil.load(UserModel.class,
                                            jsonObject.optJSONObject("user"));
                                    String token = userModel.getApiToken();
                                    // 保存用户名和密码
                                    PreferencesUtils.putString(wApplication,
                                            PreferencesConstant.USER_NAME,
                                            "");
                                    PreferencesUtils.putString(wApplication,
                                            PreferencesConstant.USER_PASSWORD, "");
                                    PreferencesUtils.putString(wApplication,
                                            PreferencesConstant.API_TOKEN,
                                            token);
                                    wApplication.setUser(userModel);
                                    wApplication.updateApiToken();
                                    DialogUtil.showMessage("登录成功");
                                    UserModel.updateUser("pushClientId", PushManager.getInstance().getClientid(LoginActivity.this)).done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {

                                        }
                                    });
                                    finish();
                                } else {
                                    String errorMessage = jsonObject.optString("message");
                                    int code = jsonObject.optInt("code");
                                    if (code == 10012) {
                                        Intent intent = new Intent(LoginActivity.this,
                                                BindPhoneActivity.class);
                                        intent.putExtra("platName", platName);
                                        intent.putExtra("platuserid", platuserid);
                                        intent.putExtra("platuserName", platuserName);
                                        intent.putExtra("platjson", platjson);
                                        intent.putExtra("plattoken", plattoken);
                                        ViewUtil.startActivity(LoginActivity.this, intent);
                                    }
                                    DialogUtil.showMessage(errorMessage);
                                }
                            }
                        });
            }
            break;
        }
        return false;
    }

}
