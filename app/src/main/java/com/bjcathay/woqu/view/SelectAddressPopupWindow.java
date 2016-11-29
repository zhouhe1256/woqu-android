
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.DistrictModel;
import com.bjcathay.woqu.model.GetCitysModel;
import com.bjcathay.woqu.model.ProvinceModel;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.widget.TosGallery;
import com.bjcathay.woqu.widget.WheelTextAdapter;
import com.bjcathay.woqu.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectAddressPopupWindow extends PopupWindow {

    public interface SelectResult {

        void resultData(long pid,long cid,long did);

    }

    private Button btn_take_sure;
    private WheelView provinceWheel, cityWheel, districtWheel;
    private View mMenuView;
    private Animation animShow;
    private Animation animHide;
    LinearLayout poplayout;
    private WheelTextAdapter provinceAdapter, cityAdapter, districtAdapter;
    private String year;
    private String month;
    private String day;
    private String addressText;
    private List<String> pms = new ArrayList<String>();
    private List<String> cms = new ArrayList<String>();
    private List<String> dms = new ArrayList<String>();


    private List<ProvinceModel> provinces;
    private List<GetCitysModel> cities;
    private List<DistrictModel> districts;


    private List<GetCitysModel> citiesModel=new ArrayList<GetCitysModel>();
    private List<DistrictModel> districtModels=new ArrayList<DistrictModel>();
    private TextView textView;

    SelectResult mSelectResult;
    public SelectAddressPopupWindow(Activity context,
                                    final TextView textView,
                                    List<ProvinceModel> provinces,
                                    List<GetCitysModel> cities,
                                    List<DistrictModel> districts,
                                    final SelectResult selectResult) {
        super(context);
        initAnim();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_address, null);
        poplayout = ViewUtil.findViewById(mMenuView, R.id.pop_layout);
        btn_take_sure = ViewUtil.findViewById(mMenuView, R.id.button);
        provinceWheel = ViewUtil.findViewById(mMenuView, R.id.id_province);
        cityWheel = ViewUtil.findViewById(mMenuView, R.id.id_city);
        districtWheel = ViewUtil.findViewById(mMenuView, R.id.id_district);
        this.textView=textView;
        this.provinces=provinces;
        this.cities=cities;
        this.districts=districts;


        provinceAdapter = new WheelTextAdapter(context);
        cityAdapter = new WheelTextAdapter(context);
        districtAdapter = new WheelTextAdapter(context);
        prepareData(0);
        provinceAdapter.setData(pms);
        cityAdapter.setData(cms);
        districtAdapter.setData(dms);


        // 确定按钮
        btn_take_sure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                textView.setText(pText + cText + dText);
                selectResult.resultData(pid,cid,did);
                dismiss();
            }
        });
        provinceWheel.setOnEndFlingListener(mListener);
        cityWheel.setOnEndFlingListener(mListener);
        districtWheel.setOnEndFlingListener(mListener);


        provinceWheel.setSoundEffectsEnabled(true);
        cityWheel.setSoundEffectsEnabled(true);
        districtWheel.setSoundEffectsEnabled(true);

        provinceWheel.setAdapter(provinceAdapter);
        cityWheel.setAdapter(cityAdapter);
        districtWheel.setAdapter(districtAdapter);



        this.setSoftInputMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
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
    String pText="北京市";
    String cText="市辖区";
    String dText="东城区";
    long prId=1;
    long ciId;
    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {

        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();

            if (provinces != null && !provinces.isEmpty()) {

                if (v == provinceWheel) {
                    prId = provinces.get(pos).getId();
                    setCity(cities, prId, pos);
                } else if (v == cityWheel) {
                    try {
                        ciId = citiesModel.get(pos).getId();
                        setDistrict(districts, ciId,prId);
                    }catch (Exception e){

                    }



                } else if (v == districtWheel) {
                    try {
                        dText = districtModels.get(pos).getName();
                        did=districtModels.get(pos).getId();
                    }catch (Exception e){

                    }


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

                dismiss();
            }
        });
    }


    private  long pid=1,cid=1,did=1;
    public void setProvince(List<ProvinceModel> provinces) {
        if (provinces != null && !provinces.isEmpty()) {
            List<String> pms = new ArrayList<String>();
            for (int i = 0; i < provinces.size(); i++) {
                pms.add(provinces.get(i).getName().toString());
            }
            provinceAdapter.setData(pms);
        }

    }

    public void setCity(List<GetCitysModel> cities, long id, int pos) {
        if (pid != id) {
            this.pid = id;
            cms.clear();
            dms.clear();
            citiesModel.clear();
            districtModels.clear();
            int j = -1;
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).getProvinceId() == id) {
                    citiesModel.add(cities.get(i));
                    cms.add(cities.get(i).getName().toString());
                    if (j < 0) {
                        j = i;
                    }
                }
            }
            if(cms!=null){
                cityAdapter.setData(cms);
                cityWheel.setSelection(0);
            }


            if (j >= 0) {
                this.cid = cities.get(j).getId();

                // dms.clear();
                for (int i = 0; i < districts.size(); i++) {
                    if (districts.get(i).getCityId() == cid) {
                        districtModels.add(districts.get(i));
                        dms.add(districts.get(i).getName().toString());

                    }
                }
                if(dms!=null){
                    districtAdapter.setData(dms);
                    cText=cities.get(j).getName();
                    pText = provinces.get(pos).getName();
                }


            }
        }
    }

    public void setDistrict(List<DistrictModel> districts, long cId,long pId) {

        if (cid != cId||pid!=pId) {
            this.cid = cId;
            this.pid=pId;
            for(int i = 0; i < cities.size(); i++){
                if(cities.get(i).getId()==cId){
                    cText=cities.get(i).getName();
                }
            }
            districtModels.clear();
            dms.clear();
            int j = -1;
            for (int i = 0; i < districts.size(); i++) {
                if (districts.get(i).getCityId() == cId) {
                    districtModels.add(districts.get(i));
                    dms.add(districts.get(i).getName().toString());
                    if (j < 0) {
                        j = i;
                    }
                }
            }
            if(dms!=null){
                districtAdapter.setData(dms);
                districtWheel.setSelection(0);
            }


            if (j >= 0) {
                this.did = districts.get(j).getId();
                dText=districts.get(j).getName();

            }
        }
    }

    public void prepareData(int a) {
        pms = new ArrayList<String>();
        for (int i = 0; i < provinces.size(); i++) {
            pms.add(provinces.get(i).getName().toString());
        }
        provinceAdapter.setData(pms);
        provinceWheel.setSelection(a);
        cms = new ArrayList<String>();
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getProvinceId() == 1) {
                citiesModel.add(cities.get(i));
                cms.add(cities.get(i).getName().toString());
            }
        }
        cityAdapter.setData(cms);
        cityWheel.setSelection(0);
        dms = new ArrayList<String>();
        for (int i = 0; i < districts.size(); i++) {
            if (districts.get(i).getCityId() == 1) {
                districtModels.add(districts.get(i));
                dms.add(districts.get(i).getName().toString());
            }
        }
        districtAdapter.setData(dms);
        districtWheel.setSelection(0);
    }
}
