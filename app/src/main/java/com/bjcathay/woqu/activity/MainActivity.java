
package com.bjcathay.woqu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.fragment.UserCenterActivityFragment;
import com.bjcathay.woqu.fragment.WishActivityFragment;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.PushModel;
import com.bjcathay.woqu.recyle.WoQuTextActivityFragment;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private RadioGroup radioGroup;
    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private WoQuTextActivityFragment woQuActivityFragment;
    private WishActivityFragment wishActivityFragment;
    private UserCenterActivityFragment userActivityFragment;
    private Fragment currentFragment;
    private int pos = 0;
    private PushModel pushModel;
    private static boolean isExit = false;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        PreferencesUtils.putBoolean(this, PreferencesConstant.FRIST_OPEN,
                false);

        initView();
        initTab();
        initData();
        setListeners();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    /**
     * 初始化底部标签
     */
    private void initTab() {
        if (woQuActivityFragment == null) {
            woQuActivityFragment = new WoQuTextActivityFragment();
        }
        if (wishActivityFragment == null) {
            wishActivityFragment = new WishActivityFragment();
        }
        if (userActivityFragment == null) {
            userActivityFragment = new UserCenterActivityFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, woQuActivityFragment, "1")
                .add(R.id.content_layout, wishActivityFragment, "2")
                .add(R.id.content_layout, userActivityFragment, "3")
                .commit();
    }

    /**
     * 点击第一个tab
     */
    private void clickTab1Layout() {
        getSupportFragmentManager().beginTransaction()
                .show(woQuActivityFragment)
                .hide(wishActivityFragment)
                .hide(userActivityFragment)
                .commit();
    }

    /**
     * 点击第二个tab
     */
    private void clickTab2Layout() {
        getSupportFragmentManager().beginTransaction()
                .show(wishActivityFragment)
                .hide(woQuActivityFragment)
                .hide(userActivityFragment)
                .commit();
    }

    /**
     * 点击第三个tab
     */
    private void clickTab3Layout() {
        getSupportFragmentManager().beginTransaction()
                .show(userActivityFragment)
                .hide(woQuActivityFragment)
                .hide(wishActivityFragment)
                .commit();
    }

    private void setListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radio0:
                        pos = 0;
                        clickTab1Layout();
                        break;
                    case R.id.radio1:
                        pos = 1;
                        clickTab2Layout();
                        break;
                    case R.id.radio2:
                        pos = 2;
                        clickTab3Layout();
                        break;
                }
            }
        });
    }

    private void initView() {
        radioButton0 = (RadioButton) findViewById(R.id.radio0);
        radioButton1 = (RadioButton) findViewById(R.id.radio1);
        radioButton2 = (RadioButton) findViewById(R.id.radio2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        int w = convertDIP2PX(this, 25);
        Drawable[] drawables0 = radioButton0.getCompoundDrawables();
        drawables0[1].setBounds(0, 0, w, w);
        radioButton0.setCompoundDrawables(drawables0[0], drawables0[1], drawables0[2],
                drawables0[3]);
        Drawable[] drawables1 = radioButton1.getCompoundDrawables();
        drawables1[1].setBounds(0, 0, w, w);
        radioButton1.setCompoundDrawables(drawables1[0], drawables1[1], drawables1[2],
                drawables1[3]);
        Drawable[] drawables2 = radioButton2.getCompoundDrawables();
        drawables2[1].setBounds(0, 0, w, w);
        radioButton2.setCompoundDrawables(drawables2[0], drawables2[1], drawables2[2],
                drawables2[3]);
    }

    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }
    @Override
    public void onClick(View v) {
        switch (pos) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                ((UserCenterActivityFragment) userActivityFragment).doClick(v);
                break;
        }
    }
    private void initData() {
        Intent intent = getIntent();
        if ("web".equals(intent.getAction())) {
            Long webid = intent.getLongExtra("id", 0l);
        }
        pushModel = (PushModel) intent.getSerializableExtra("push");
        if (pushModel != null) {
            pushNoticeIntent(pushModel);
        }
    }

    private void pushNoticeIntent(final PushModel pushModel) {
        Intent intent;
        if ("REMIND".equals(pushModel.getT())) {
            PointPraiseModel.getActivitys(pushModel.getId()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    Intent intent = null;
                    PointPraiseModel pointPraiseMode = arguments.get(0);
                    int status = pointPraiseMode.getActivity().getStatus();
                    if (status == 0) {
                        intent = new Intent(MainActivity.this, PointPraiseActivity.class);

                    } else if (status == 1) {
                        intent = new Intent(MainActivity.this, PointPraiseActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, PointPraiseEndActivity.class);
                    }
                    intent.putExtra("id", pushModel.getId());
                    ViewUtil.startActivity(MainActivity.this, intent);
                }
            });
        } else if ("SYSTEM".equals(pushModel.getT())) {

        } else if ("LUCK".equals(pushModel.getT())) {
            if (WApplication.getInstance().isLogin()) {
                intent = new Intent(MainActivity.this, MyNewsActivity.class);
                intent.putExtra("id", Long.parseLong(pushModel.getId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            } else {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            ViewUtil.startActivity(MainActivity.this, intent);
        } else if ("AD".equals(pushModel.getT())) {
            intent = new Intent(MainActivity.this, ShoppingActivity.class);
            intent.putExtra("url", Long.parseLong(pushModel.getId()));
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            ViewUtil.startActivity(MainActivity.this, intent);
        } else if ("ACTIVITY".equals(pushModel.getT())) {
            PointPraiseModel.getActivitys(pushModel.getId()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    Intent intent = null;
                    PointPraiseModel pointPraiseMode = arguments.get(0);
                    int status = pointPraiseMode.getActivity().getStatus();
                    if (status == 0) {
                        intent = new Intent(MainActivity.this, PointPraiseActivity.class);

                    } else if (status == 1) {
                        intent = new Intent(MainActivity.this, PointPraiseActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, PointPraiseEndActivity.class);
                    }
                    intent.putExtra("id", pushModel.getId());
                    ViewUtil.startActivity(MainActivity.this, intent);
                }
            });
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("tab页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("tab页面");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isExit) {
                        isExit = true;

            DialogUtil.showMessage("再按一次退出程序");
                        new Timer().schedule(new TimerTask() {
                                    @Override
                                 public void run() {
                                        isExit = false;}
                        }, 2000);
                    } else {
                        finish();
                   }

        return false;
    }
}
