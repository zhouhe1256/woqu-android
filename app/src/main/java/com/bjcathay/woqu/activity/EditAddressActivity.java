
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.PhoneNumberUtil;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ErrorCode;
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
import com.bjcathay.woqu.util.StringUtils;
import com.bjcathay.woqu.util.ValidformUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.DeleteInfoDialog;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.SelectAddressPopupWindow;
import com.bjcathay.woqu.view.SelectDatePopupWindow;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangm on 15-9-24.
 */
public class EditAddressActivity extends Activity implements View.OnClickListener,SelectAddressPopupWindow.SelectResult ,DeleteInfoDialog.DeleteInfoDialogResult{
    private TopView topView;
    private WApplication wApplication;
    private EditText nameEd;
    private EditText phoneEd;
    private EditText postCodeEd;
    private TextView areaEd;
    private EditText street;
    private EditText detailAddressEd;
    private long pid=1;
    private long cid=1;
    private long did=1;
    private List<ProvinceModel> provinces = new ArrayList<ProvinceModel>();
    private List<GetCitysModel> cities = new ArrayList<GetCitysModel>();
    private List<DistrictModel> districts = new ArrayList<DistrictModel>();
    private ConsigneeModel consigneeModel;
    private SelectAddressPopupWindow selectAddressPopupWindow;
    private String pText="";
    private String cText="";
    private String dText="";
    private long DELETE=1l;
    private long id;
    private Button defaultBtn;
    private LoadingDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setSaveBtnVisiable();
        topView.setTitleText("修改收货地址");

        wApplication = WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
    }

    public void initView() {
        nameEd = ViewUtil.findViewById(EditAddressActivity.this, R.id.reciver_text);
        phoneEd = ViewUtil.findViewById(EditAddressActivity.this, R.id.phone_text);
        postCodeEd = ViewUtil.findViewById(EditAddressActivity.this, R.id.postalcode_text);
        // area = ViewUtil.findViewById(EditAddressActivity.this, R.id.area_text);
        //   street = ViewUtil.findViewById(EditAddressActivity.this, R.id.street_text);
        detailAddressEd = ViewUtil.findViewById(EditAddressActivity.this, R.id.detailed_address);
        areaEd=ViewUtil.findViewById(EditAddressActivity.this,R.id.area_text);
        defaultBtn=ViewUtil.findViewById(this,R.id.set_default);
        progress=new LoadingDialog(this);
    }

    public void initData() {
        Intent intent = getIntent();
        consigneeModel = (ConsigneeModel) intent.getSerializableExtra("consignee");

        nameEd.setText(consigneeModel.getName().toString());
        phoneEd.setText(consigneeModel.getContactWay()==null?"":consigneeModel.getContactWay());
        detailAddressEd.setText(consigneeModel.getDetailAddress());
        postCodeEd.setText(consigneeModel.getZipCode());
        // postCode.setText(consigneeModel.get);
        pid=consigneeModel.getLuProvinceId();
        cid=consigneeModel.getLuCityId();
        did=consigneeModel.getLuCountyId();
        id=consigneeModel.getId();
        String p= PreferencesUtils.getString(EditAddressActivity.this, PreferencesConstant.P_INFO, "");
        if(p!=null&&!p.isEmpty()){
            ProvinceListModel provinceListModel= JSONUtil.load(ProvinceListModel.class, p);
            provinces=provinceListModel.getProvinces();
            if(provinces!=null){
                for (int i=0;i<provinces.size();i++){
                    if(provinces.get(i).getId()==pid){
                        pText=provinces.get(i).getName();
                    }
                }
                areaEd.setText(pText+cText+dText);
            }else{
                getP();
            }



        }else{
            getP();
        }
        String d=PreferencesUtils.getString(EditAddressActivity.this, PreferencesConstant.D_INFO, "");
        if(d!=null&&!d.isEmpty()){
            DistrictListModel districtListModel=JSONUtil.load(DistrictListModel.class,d);
            districts=districtListModel.getCounties();
            if(districts!=null||!districts.isEmpty()){
                for (int i=0;i<districts.size();i++){
                    if(districts.get(i).getId()==did){
                        dText=districts.get(i).getName();

                    }
                }
                areaEd.setText(pText+cText+dText);
            }else{
                getD();
            }


        }else{
            getD();
        }

        String c=PreferencesUtils.getString(EditAddressActivity.this, PreferencesConstant.C_INFO, "");
        if(c!=null&&!c.isEmpty()){
            CityListModel cityListModel=JSONUtil.load(CityListModel.class,c);
            cities=cityListModel.getCities();
            if(cities!=null||!cities.isEmpty()){

                for (int i=0;i<cities.size();i++){
                    if(cities.get(i).getId()==cid){
                        cText=cities.get(i).getName();
                    }
                }
                areaEd.setText(pText+cText+dText);
            }else{
                getC();
            }
        }else{
            getC();
        }

    }

    @Override
    public void resultData(long pid, long cid, long did) {
        this.pid=pid;
        this.cid=cid;
        this.did=did;
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if(isDelete){
            delete();
            finish();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.delete_layout:

                DeleteInfoDialog infoDialog = new DeleteInfoDialog(EditAddressActivity.this,
                        R.style.InfoDialog, getString(R.string.delete_address_text), DELETE,
                        EditAddressActivity.this);
                infoDialog.show();
                break;

            case R.id.set_default:
                setDefault();
                defaultBtn.setClickable(false);
                break;
            case R.id.title_back_img:

                finish();
                break;
            case R.id.title_save:
                changeAddress();
                break;
            case R.id.address_layout:

                selectAddressPopupWindow=new SelectAddressPopupWindow(EditAddressActivity.this,areaEd,provinces,cities,districts, EditAddressActivity.this);
                if(!provinces.isEmpty()&&!cities.isEmpty()&&!districts.isEmpty()){
                    progress.dismiss();
                    selectAddressPopupWindow.showAtLocation(EditAddressActivity.this.findViewById(R.id.detailed_address),
                            Gravity.BOTTOM
                                    | Gravity.CENTER_HORIZONTAL,
                            0, 0); // 设置layout在PopupWindow中显示的位置
                }

                break;

        }
    }

    private void delete() {
        progress.show();
        ConsigneeModel.deleteCons(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                progress.dismiss();
                boolean flag = jsonObject.optBoolean("success");
                if (flag) {
                    DialogUtil.showMessage("删除地址成功");
                } else {
                    DialogUtil.showMessage("删除地址失败");
                }

            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                String errorMessage = jsonObject.optString("message");
                progress.dismiss();
                DialogUtil.showMessage(errorMessage);


            }});
    }

    private void setDefault() {
        progress.show();
        ConsigneeModel.defaultCons(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                progress.dismiss();
                boolean flag = jsonObject.optBoolean("success");
                if (flag) {
                    DialogUtil.showMessage("设置默认地址成功");
                    finish();
                } else {
                    DialogUtil.showMessage("设置默认地址失败");
                }
                defaultBtn.setClickable(true);
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
                DialogUtil.showMessage(getString(R.string.empty_net_text));
                defaultBtn.setClickable(true);
            }
        });
    }
    private void changeAddress(){

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
        } else if (area == null || area.isEmpty()||pid==0||cid==0||did==0) {
            DialogUtil.showMessage(getString(R.string.area_empty));
            return;
        } else if (detailAddress == null || detailAddress.isEmpty()) {
            DialogUtil.showMessage(getString(R.string.detailaddress_empty));

            return;

        } else{
            progress.show();
            ConsigneeModel.modifyCons(id,name,phone,pid,cid,did,detailAddress,postCode).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    progress.dismiss();
                    try {
                        if (jsonObject.getBoolean("success") == true) {
                            DialogUtil.showMessage("修改成功");
                            finish();

                        } else {
                            DialogUtil.showMessage("修改失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    DialogUtil.showMessage(getString(R.string.empty_net_text));
                    progress.dismiss();
                }
            });
        }}

    //获取省份
    public void getP(){
        ProvinceListModel.Province().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                ProvinceListModel provinceListModel = arguments.get(0);
                provinces = provinceListModel.getProvinces();
                PreferencesUtils.putString(EditAddressActivity.this, PreferencesConstant.P_INFO, JSONUtil.dump(provinceListModel));
                for (int i=0;i<provinces.size();i++){
                    if(provinces.get(i).getId()==pid){
                        pText=provinces.get(i).getName();
                    }
                }
                areaEd.setText(pText+cText+dText);
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
                PreferencesUtils.putString(EditAddressActivity.this, PreferencesConstant.D_INFO, JSONUtil.dump(districtListModel));
                districts = districtListModel.getCounties();
                for (int i = 0; i < districts.size(); i++) {
                    if (districts.get(i).getId() == did) {
                        dText = districts.get(i).getName();

                    }
                }
                areaEd.setText(pText + cText + dText);
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
                PreferencesUtils.putString(EditAddressActivity.this, PreferencesConstant.C_INFO, JSONUtil.dump(cityListModel));
                cities = cityListModel.getCities();
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getId() == cid) {
                        cText = cities.get(i).getName();
                    }
                }
                areaEd.setText(pText + cText + dText);
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
        MobclickAgent.onPageStart("修改收货地址");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改收货地址");
        MobclickAgent.onPause(this);
    }
}
