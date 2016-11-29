
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.uptutil.DownloadManager;
import com.bjcathay.woqu.util.DataCleanManager;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.DeleteInfoDialog;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * Created by jiangm on 15-9-24.
 */
public class SettingActivity extends Activity
        implements View.OnClickListener, DeleteInfoDialog.DeleteInfoDialogResult {
    TopView topView;
    private long CLEAN = 0l;
//  private long UPDATE = 1l;
    private LinearLayout linearLayout;
    private View view;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("设置");
        initView();
    }


    public void initView(){
        linearLayout=ViewUtil.findViewById(this,R.id.changepwd_layout);
        view=ViewUtil.findViewById(this,R.id.view_1);
        logout=ViewUtil.findViewById(this,R.id.logout_bt);
        if(!WApplication.getInstance().isLogin()){
            linearLayout.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changepwd_layout:
                Intent intent5 = new Intent(SettingActivity.this, ChangePwdActivity.class);
                ViewUtil.startActivity(SettingActivity.this, intent5);
                break;
            case R.id.clean_layout:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(SettingActivity.this,
                        R.style.InfoDialog, getString(R.string.clean_dialog_text), CLEAN,
                        SettingActivity.this);
                infoDialog.show();
                break;
            case R.id.opinion_layout:
                Intent intent3 = new Intent(SettingActivity.this, AdviseActivity.class);
                ViewUtil.startActivity(SettingActivity.this, intent3);
                break;
            case R.id.about_layout:
                Intent intent2 = new Intent(SettingActivity.this, AboutWoQuActivity.class);
                ViewUtil.startActivity(SettingActivity.this, intent2);
                break;
            case R.id.state_layout:
                Intent intent4 = new Intent(SettingActivity.this, StatementActivity.class);
                ViewUtil.startActivity(SettingActivity.this, intent4);
                break;
            case R.id.logout_bt:
//                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
//                ViewUtil.startActivity(SettingActivity.this, intent);
                PreferencesUtils.putString(this, PreferencesConstant.API_TOKEN, "");
                WApplication.getInstance().updateApiToken();
                DialogUtil.showMessage("退出成功");
                WApplication.getInstance().refresh = true;
              //  DataCleanManager.cleanInternalCache(SettingActivity.this);
//               WApplication.getInstance().exit();
                finish();
                break;
            case R.id.supdate_layout:
                DownloadManager downManger = new DownloadManager(this, true);
                downManger.checkDownload();
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {

        if (isDelete) {
            DataCleanManager.cleanFiles(SettingActivity.this);
            DataCleanManager.cleanInternalCache(SettingActivity.this);
            DataCleanManager.cleanDatabases(SettingActivity.this);
            DataCleanManager.cleanSharedPreference(SettingActivity.this);
            //清除图片及请求数据
            clearBuffer(WApplication.getInstance().getBaseDir());
            DialogUtil.showMessage("清除成功");
        }

    }


    private void clearBuffer(File baseDir) {
        delete(new File(baseDir.getPath() + "/images"));
        delete(new File(baseDir.getPath() + "/api"));
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置界面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("设置界面");
        MobclickAgent.onPause(this);
    }
}
