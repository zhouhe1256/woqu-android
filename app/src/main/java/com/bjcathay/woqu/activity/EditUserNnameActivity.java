package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiangm on 15-9-24.
 */
public class EditUserNnameActivity extends Activity implements View.OnClickListener{
    private TopView topView;
    private WApplication wApplication;
    private String KEY="nickname";
    private EditText name;
    private int num=8;
    private boolean flag;
    private LoadingDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_username);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("修改昵称");
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initEvent();
    }

    public void initView(){
        name=ViewUtil.findViewById(this,R.id.name_edit);
        name.setText(PreferencesUtils.getString(EditUserNnameActivity.this,PreferencesConstant.NICK_NAME));
        progress=new LoadingDialog(this);
    }
    public void initEvent(){
        name.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp=s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int number=num-s.length();
                selectionStart = name.getSelectionStart();
                selectionEnd = name.getSelectionEnd();
                name.setSelection(selectionStart);

                if (temp.length() > num) {
                   //  s.delete(selectionStart - 1, selectionEnd);
                   // int tempSelection = selectionStart;
                    int tempSelection = selectionStart;
                    flag=true;
                  //  name.setText(s);
                    name.setSelection(tempSelection);//设置光标在最后
                }else{
                    flag=false;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.save_bt:
                progress.show();
                save();
                break;
        }
    }
    public void save(){
        final String nickName= name.getText().toString().trim();
        if(flag){
            progress.dismiss();
            DialogUtil.showMessage("昵称长度 ");
            return;
        }
        if(nickName!=null&&!nickName.isEmpty()){
            UserModel.updateUser(KEY,nickName).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject=arguments.get(0);
                    progress.dismiss();
                    try {
                        if(jsonObject.getBoolean("success")==true){
                            DialogUtil.showMessage("昵称修改成功");
                            PreferencesUtils.putString(wApplication, PreferencesConstant.NICK_NAME,nickName);
                            finish();
                        }else{
                            DialogUtil.showMessage("昵称修改失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    progress.dismiss();
                    DialogUtil.showMessage(getString(R.string.empty_net_text));
                }
            });
        }else{
            DialogUtil.showMessage("昵称不能为空");
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("修改昵称");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改昵称");
        MobclickAgent.onPause(this);
    }
}
