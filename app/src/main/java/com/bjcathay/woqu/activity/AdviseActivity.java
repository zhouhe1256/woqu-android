package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ErrorCode;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.StringUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.DeleteInfoDialog;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by jiangm on 15-9-29.
 */
public class AdviseActivity extends Activity implements View.OnClickListener{
    private TopView topView;
    private EditText adviseEd;
    private EditText phoneEd;
    private WApplication wApplication;
    private int num=200;
    private LoadingDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise);
        wApplication=WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
      initEvent();
    }
    public void initView(){
        topView= ViewUtil.findViewById(this,R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setFinishVisiable();
        topView.setTitleText("用户反馈");
        adviseEd=ViewUtil.findViewById(this,R.id.advise_edit);
       // phoneEd=ViewUtil.findViewById(this,R.id.phone_edit);
        progress=new LoadingDialog(this);
    }

    public void initEvent(){

        adviseEd.addTextChangedListener(new TextWatcher() {
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
                selectionStart = adviseEd.getSelectionStart();
                selectionEnd = adviseEd.getSelectionEnd();
                adviseEd.setSelection(selectionStart);
//               if (number<=0){
//                   DialogUtil.showMessage("输入字数已经超出限制");
//                }
                if (temp.length() > num) {
                s.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionStart;
                 adviseEd.setText(s);

                  adviseEd.setSelection(tempSelection);//设置光标在最后
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
            case R.id.title_finish:
                progress.show();
                commit();
                break;
        }
    }
    public void commit(){
        String content=adviseEd.getText().toString().trim();
        if(content.isEmpty()||content==null){
            progress.dismiss();
            DialogUtil.showMessage("内容不能为空");
        }else{


        UserModel.feedBack(adviseEd.getText().toString().trim(),"").done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                progress.dismiss();
                if (jsonObject.optBoolean("success")) {
                    DialogUtil.showMessage("感谢参与！");
                    finish();
                } else {
                    String errorMessage = jsonObject.optString("message");
                    DialogUtil.showMessage(errorMessage);

                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {

                DialogUtil.showMessage(getString(R.string.empty_net_text));

            }
        });
    }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("意见反馈");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("意见反馈");
        MobclickAgent.onPause(this);
    }
}
