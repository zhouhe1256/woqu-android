
package com.bjcathay.woqu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.util.LogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.widget.TosGallery;
import com.bjcathay.woqu.widget.WheelTextAdapter;
import com.bjcathay.woqu.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectDatePopupWindow extends PopupWindow {

    public interface SelectResult {
        void resultData();
    }

    private Button btn_take_sure;
    private WheelView yearWheel, monthWheel, dayWheel;
    private View mMenuView;
    private Animation animShow;
    private Animation animHide;
    LinearLayout poplayout;
    private WheelTextAdapter yearAdapter, monthAdapter, dayAdapter;
    private String year;
    private String month;
    private String day;
    private String birthdayText;
    List<String> yearList = new ArrayList<String>();
    List<String> monthList = new ArrayList<String>();
    List<String> dayList = new ArrayList<String>();

    public SelectDatePopupWindow(Activity context,final TextView textView, final SelectResult selectResult) {
        super(context);
        initAnim();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_date, null);
        poplayout = ViewUtil.findViewById(mMenuView, R.id.pop_layout);
        btn_take_sure = ViewUtil.findViewById(mMenuView, R.id.button);
        yearWheel = ViewUtil.findViewById(mMenuView, R.id.id_year);
        monthWheel = ViewUtil.findViewById(mMenuView, R.id.id_month);
        dayWheel = ViewUtil.findViewById(mMenuView, R.id.id_day);
        yearAdapter = new WheelTextAdapter(context);
        monthAdapter = new WheelTextAdapter(context);
        dayAdapter = new WheelTextAdapter(context);
        prepareData();
        yearAdapter.setData(yearList);
        monthAdapter.setData(monthList);
        dayAdapter.setData(dayList);

        // 确定按钮
        btn_take_sure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框

                if(Integer.valueOf(month)<10){
                    month="0"+month;
                }
                if (Integer.valueOf(day)<10){
                    day="0"+day;
                }
                birthdayText=year+"-"+month+"-"+day;
                textView.setText(birthdayText);
                selectResult.resultData();
                dismiss();
                // poplayout.setVisibility(View.VISIBLE);
                // poplayout.clearAnimation();
                // poplayout.startAnimation(animHide);
            }
        });
        yearWheel.setOnEndFlingListener(mListener);
        monthWheel.setOnEndFlingListener(mListener);
        dayWheel.setOnEndFlingListener(mListener);


        yearWheel.setSoundEffectsEnabled(true);
        monthWheel.setSoundEffectsEnabled(true);
        dayWheel.setSoundEffectsEnabled(true);

        yearWheel.setAdapter(yearAdapter);
        monthWheel.setAdapter(monthAdapter);
        dayWheel.setAdapter(dayAdapter);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        // dismiss();

                        poplayout.setVisibility(View.VISIBLE);
                        poplayout.clearAnimation();
                        poplayout.startAnimation(animHide);
                    }
                }
                return true;
            }
        });
        poplayout.setVisibility(View.VISIBLE);
        poplayout.clearAnimation();
        poplayout.startAnimation(animShow);
    }

    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {

        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();

            if (yearList != null && !yearList.isEmpty()) {
                if (v == yearWheel) {
                    prepareDay(Integer.valueOf(2015-(pos)), Integer.valueOf(month));
                    year = String.valueOf(2015-(pos));
                } else if (v == monthWheel) {
                    String   m = String.valueOf(pos+1);
                    prepareDay(Integer.valueOf(year), Integer.valueOf(m));
                } else if (v == dayWheel) {
                    day=String.valueOf(pos+1);
                }
            }
        }
    };

    private void initAnim() {
        animShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        animShow.setDuration(300);

        animHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        animHide.setDuration(300);
        animHide.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                // poplayout.setVisibility(View.GONE);
                dismiss();
            }
        });
    }


    int mYear, mMonth, mDay;

    public void prepareData() {
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        mYear = dateAndTime.get(Calendar.YEAR);
        mMonth = dateAndTime.get(Calendar.MONTH);
        mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        for (int i = mYear; i >=1915; i--) {
            yearList.add(String.valueOf(i));
        }
        for (int i = 1; i <=12; i++) {
            monthList.add(String.valueOf(i));
        }
        for (int i = 1; i <= 31; i++) {
            dayList.add(String.valueOf(i));
        }
        year=yearList.get(0);
        month=monthList.get(0);
        day=dayList.get(0);
    }

    public void prepareDay(int mYear, int mMonth) {
        //   Log.i("BBBB",mYear+"++"+mMonth);
        if(Integer.valueOf(month)!=mMonth||Integer.valueOf(year)!=mYear){
            dayList.clear();
            int j = days(mYear, mMonth);

            int f = -1;
            for (int i = 1; i <= j; i++) {
                dayList.add(String.valueOf(i));
                if (f < 0) {
                    f = i;
                }
            }
            dayAdapter.setData(dayList);
            dayWheel.setSelection(0);
            if (f >= 0) {
                this.month = String.valueOf(mMonth);

            }
        }


    }

    public int days(int year, int b) {
        int i = 0;
        switch (b) {
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    i = 29;
                } else {
                    i = 28;
                }
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                i = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                i = 30;
                break;

        }
        return i;
    }
}
