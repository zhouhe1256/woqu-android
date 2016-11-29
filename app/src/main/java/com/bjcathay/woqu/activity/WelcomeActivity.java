
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.WelcomeViewPagerAdapter;
import com.bjcathay.woqu.model.PushModel;
import com.bjcathay.woqu.model.SplashModel;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.SizeUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.JazzyViewPager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-10-28.
 */
public class WelcomeActivity extends Activity {
    private JazzyViewPager viewPager;
    private int[] imgIdArray;
    private LinearLayout dotoParendLinearLayout;
    ImageView[] dots;
    private ImageView imageView;
    private String action;
    private String competionId;
    private PushModel pushModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        getWebEvent();
        viewPager = (JazzyViewPager) findViewById(R.id.viewPager_welcome);
        dotoParendLinearLayout = ViewUtil.findViewById(this, R.id.doto_ly);
        imageView = ViewUtil.findViewById(this, R.id.splash_bg);
        imgIdArray = new int[] {
                R.drawable.loading1, R.drawable.loading2, R.drawable.loading3
        };
        splash();
    }

    private void getWebEvent() {
        Intent i_getvalue = getIntent();
        String action_ = i_getvalue.getAction();

        if (Intent.ACTION_VIEW.equals(action_)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                competionId = uri.getQueryParameter("id");
                action = "web";
            }
        }
    }

    private void splash() {
       boolean firstOpen = PreferencesUtils.getBoolean(this, PreferencesConstant.FRIST_OPEN,
                true);
        String imgurl = PreferencesUtils.getString(this, PreferencesConstant.SPLASH_LAST);
        if (firstOpen) {
            imageView.setVisibility(View.GONE);
            setupBanner(imgIdArray);
            viewPager.setVisibility(View.VISIBLE);
        }
        else {
            if (imgurl == null) {
                imageView.setImageResource(R.drawable.splash_default);
            } else {
                ImageViewAdapter.adapt(imageView, imgurl, R.drawable.splash_default,true);
            }
            imageView.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            startMainActivity();
            SplashModel.getImg().done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    SplashModel splashModel = arguments.get(0);
                    if (splashModel != null && splashModel.getImageUrl() != null)
                        PreferencesUtils.putString(WelcomeActivity.this,
                                PreferencesConstant.SPLASH_LAST, splashModel.getImageUrl());
                }
            });
        }
    }

    private void startMainActivity() {
        pushModel = (PushModel) getIntent().getSerializableExtra("push");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                if (action != null && competionId != null) {
                    intent.setAction(action);
                    intent.putExtra("id", Long.valueOf(competionId));
                }
                if (pushModel != null)
                    intent.putExtra("push", pushModel);
                ViewUtil.startActivity(WelcomeActivity.this, intent);
                finish();
            }
        }, 1000);
    }

    private void setupBanner(int[] items) {
        viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        viewPager.setAdapter(new WelcomeViewPagerAdapter(this, viewPager, items));
        viewPager.setPageMargin(0);

        int count = items.length;
        int widthAndHeight = SizeUtil.dip2px(this, 7);
        dots = new ImageView[count];
        int margin = SizeUtil.dip2px(this, 10);
        dotoParendLinearLayout.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView mImageView = new ImageView(this);
            dots[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(widthAndHeight, widthAndHeight));
            layoutParams.rightMargin = margin;

            mImageView.setBackgroundResource(R.drawable.pic_22);
            dotoParendLinearLayout.setGravity(Gravity.CENTER);
            dotoParendLinearLayout.addView(mImageView, layoutParams);
        }
        setImageBackground(0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(final int i) {
                setImageBackground(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void setImageBackground(int currentPosition) {
        if (currentPosition == 4) {
            dotoParendLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            dotoParendLinearLayout.setVisibility(View.VISIBLE);
        }
        if (dots != null) {
            for (int i = 0; i < dots.length; i++) {
                if (i == currentPosition) {
                    dots[i].setBackgroundResource(R.drawable.pic_11);
                } else {
                    dots[i].setBackgroundResource(R.drawable.pic_22);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("闪屏页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("闪屏页面");
        MobclickAgent.onPause(this);
    }
}
