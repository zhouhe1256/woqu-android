
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.CityListModel;
import com.bjcathay.woqu.model.ConsigneeModel;
import com.bjcathay.woqu.model.DistrictListModel;
import com.bjcathay.woqu.model.DistrictModel;
import com.bjcathay.woqu.model.GetCitysModel;
import com.bjcathay.woqu.model.ProvinceListModel;
import com.bjcathay.woqu.model.ProvinceModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.ValidformUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.SelectAddressPopupWindow;
import com.bjcathay.woqu.view.SelectDatePopupWindow;
import com.bjcathay.woqu.view.TopView;
import com.bjcathay.woqu.widget.TosGallery;
import com.bjcathay.woqu.widget.WheelTextAdapter;
import com.bjcathay.woqu.widget.WheelView;
import com.mob.tools.utils.SharePrefrenceHelper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangm on 15-9-24.
 */
public class AddAddressActivity extends Activity implements View.OnClickListener ,SelectAddressPopupWindow.SelectResult{
    private TopView topView;
    private WheelView wvP;
    private WheelView wvC;
    private WheelView wvD;
    private LinearLayout layout;
    private SelectAddressPopupWindow selectAddressPopupWindow;

    private List<ProvinceModel> provinces = new ArrayList<ProvinceModel>();
    private List<GetCitysModel> cities = new ArrayList<GetCitysModel>();
    private List<DistrictModel> districts = new ArrayList<DistrictModel>();

    private List<String> pms = new ArrayList<String>();;
    // = new ArrayList<String>();
    private List<String> cms = new ArrayList<String>();;
    // = new ArrayList<String>();
    private List<String> dms = new ArrayList<String>();;
    // = new ArrayList<String>();

    // private ProvinceModel provinceModel;
    // private GetCitysModel getCitysModel;
    // private DistrictModel districtModel;

    private WApplication wApplication;

    WheelTextAdapter prAdapter;
    WheelTextAdapter ctAdapter;
    WheelTextAdapter diAdapter;
    private long pid=1;
    private long cid=1;
    private long did=1;
    private EditText nameEd;
    private EditText phoneEd;
    private EditText postCodeEd;
    private TextView areaEd;
    // private EditText streetEd;
    private EditText detailAddressEd;
    private LoadingDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("添加收货地址");
        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
    }

    @Override
    public void resultData(long pid, long cid, long did) {
        this.pid=pid;
        this.cid=cid;
        this.did=did;

    }

    public void initView() {
        wvP = (WheelView) findViewById(R.id.id_province);
        wvC = (WheelView) findViewById(R.id.id_city);
        wvD = (WheelView) findViewById(R.id.id_district);
        nameEd = ViewUtil.findViewById(this, R.id.reciver_text);
        phoneEd = ViewUtil.findViewById(this, R.id.phone_text);
        postCodeEd = ViewUtil.findViewById(this, R.id.postalcode_text);
        areaEd = ViewUtil.findViewById(this, R.id.area_text);
        // street = ViewUtil.findViewById(this, R.id.street_text);
        detailAddressEd = ViewUtil.findViewById(this, R.id.detailed_address);
        nameEd.clearFocus();
        phoneEd.clearFocus();
        postCodeEd.clearFocus();
        detailAddressEd.clearFocus();
        progress=new LoadingDialog(this);

    }

    public void initData() {
        //  layout = ViewUtil.findViewById(this, R.id.linear_layout);
        //  layout.setEnabled(false);
        prAdapter = new WheelTextAdapter(this);
        ctAdapter = new WheelTextAdapter(this);
        diAdapter = new WheelTextAdapter(this);
        prAdapter.setData(pms);
        ctAdapter.setData(cms);
        diAdapter.setData(dms);
        String p=PreferencesUtils.getString(AddAddressActivity.this, PreferencesConstant.P_INFO, "");
        if(p!=null&&!p.isEmpty()) {
            ProvinceListModel provinceListModel = JSONUtil.load(ProvinceListModel.class, p);
            provinces = provinceListModel.getProvinces();
            if(provinces==null||provinces.isEmpty()){
                getP();
            }
        }else {
            getP();
        }
        String d=PreferencesUtils.getString(AddAddressActivity.this, PreferencesConstant.D_INFO, "");
        if(d!=null&&!d.isEmpty()) {

            DistrictListModel districtListModel = JSONUtil.load(DistrictListModel.class, d);
            districts = districtListModel.getCounties();
            if(districts==null||districts.isEmpty()){
                getD();
            }

        }else{
            getD();
        }
        String c=PreferencesUtils.getString(AddAddressActivity.this, PreferencesConstant.C_INFO, "");
        if(c!=null&&!c.isEmpty()) {

            CityListModel cityListModel = JSONUtil.load(CityListModel.class, c);
            cities = cityListModel.getCities();
            if(cities==null||cities.isEmpty()){
                getC();
            }



        }else{
            getC();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_layout:
                //showPopupwindow(topView);

                selectAddressPopupWindow=new SelectAddressPopupWindow(AddAddressActivity.this,areaEd,provinces,cities,districts, AddAddressActivity.this);
                if(!provinces.isEmpty()&&!cities.isEmpty()&&!districts.isEmpty()){
                    selectAddressPopupWindow.showAtLocation(AddAddressActivity.this.findViewById(R.id.detailed_address),
                            Gravity.BOTTOM
                                    | Gravity.CENTER_HORIZONTAL,
                            0, 0); // 设置layout在PopupWindow中显示的位置

                }

                break;
            case R.id.sava_bt:
                addAddress();

                break;
            case R.id.title_back_img:
                finish();
                break;

        }
    }

    public void addAddress() {
        String name = nameEd.getText().toString().trim();
        String phone = phoneEd.getText().toString().trim();
        String postCode = postCodeEd.getText().toString().trim();
        String area = areaEd.getText().toString().trim();
        String detailAddress = detailAddressEd.getText().toString().trim();
        if (name == null || name.isEmpty()) {
            DialogUtil.showMessage(getString(R.string.name_empty));
            return;
        } else if (phone == null || phone.isEmpty()) {
            DialogUtil.showMessage(getString( R.string.phone_empty));
            return;
        } else if(!ValidformUtil.isMobileNo(phone)) {
            DialogUtil.showMessage(getString(R.string.phone_check));
            return;
        }else if (postCode == null || postCode.isEmpty()) {
            DialogUtil.showMessage(getString( R.string.postcode_empty));
            return;
        }else if(postCode.length() !=6){
            DialogUtil.showMessage(getString(R.string.postcode_check));
            return;
        } else if (area == null || area.isEmpty()||pid==0||cid==0||did==0) {
            DialogUtil.showMessage(getString(R.string.area_empty));
            return;
        } else if (detailAddress == null || detailAddress.isEmpty()) {
            DialogUtil.showMessage(getString(R.string.detailaddress_empty));
            return;

        }else {
            progress.show();
            ConsigneeModel.newCons(name, phone, pid, cid, did, detailAddress,postCode)
                    .done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            // ConsigneeModel consigneeModel=arguments.get(0);
                            // Intent intent=new
                            // Intent(AddAddressActivity.this,MyAddressActivity.class);
                            progress.dismiss();
                            finish();
                        }
                    }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    String errorMessage = jsonObject.optString("message");
                    progress.dismiss();
                    if(errorMessage!=null){
                        DialogUtil.showMessage(errorMessage);
                    }else{
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                    }


                }
            });
        }

    }

    //获取省份
    public void getP(){
        ProvinceListModel.Province().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                ProvinceListModel provinceListModel = arguments.get(0);
                provinces = provinceListModel.getProvinces();
                PreferencesUtils.putString(AddAddressActivity.this, PreferencesConstant.P_INFO, JSONUtil.dump(provinceListModel));
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });
    }
    //获取区县
    public void getD(){
        DistrictListModel.Districts().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {

                DistrictListModel districtListModel = arguments.get(0);
                PreferencesUtils.putString(AddAddressActivity.this, PreferencesConstant.D_INFO, JSONUtil.dump(districtListModel));
                districts = districtListModel.getCounties();
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });
    }
    //获取市
    public void getC(){
        CityListModel.Cities().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {

                CityListModel cityListModel = arguments.get(0);
                PreferencesUtils.putString(AddAddressActivity.this, PreferencesConstant.C_INFO, JSONUtil.dump(cityListModel));
                cities = cityListModel.getCities();
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("添加收货地址");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("添加收货地址");
        MobclickAgent.onPause(this);
    }
}
