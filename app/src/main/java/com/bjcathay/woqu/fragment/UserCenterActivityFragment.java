package com.bjcathay.woqu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.LoginActivity;
import com.bjcathay.woqu.activity.MyAddressActivity;
import com.bjcathay.woqu.activity.MyInvolvedActivity;
import com.bjcathay.woqu.activity.MyNewsActivity;
import com.bjcathay.woqu.activity.MyOrderActivity;
import com.bjcathay.woqu.activity.MyWarehouseActivity;
import com.bjcathay.woqu.activity.RegisterActivity;
import com.bjcathay.woqu.activity.SettingActivity;
import com.bjcathay.woqu.activity.UserDataActivity;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.IsLoginUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.CircleImageView;

import java.lang.reflect.Field;

/**
 * Created by jiangm on 15-9-24.
 */

public class UserCenterActivityFragment extends Fragment {
    private CircleImageView userImg;
    private UserModel userModel;
    private WApplication wApplication;
    private LinearLayout notlogin;
    private LinearLayout login;
    private TextView userName;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_center, container,
                false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wApplication=WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initData();
    }

    public void initView(View view){
        userImg=ViewUtil.findViewById(view,R.id.photo_image);
        notlogin=ViewUtil.findViewById(view,R.id.notlogin_layout);
        login=ViewUtil.findViewById(view,R.id.login_layout);
        userName=ViewUtil.findViewById(view,R.id.user_name);
    }
    public void initData(){
        if(wApplication.isLogin()){
            login.setVisibility(View.VISIBLE);
            notlogin.setVisibility(View.GONE);
            userName.setText(PreferencesUtils.getString(wApplication, PreferencesConstant.NICK_NAME));
            ImageViewAdapter.adapt(userImg, wApplication.getUser().getImageUrl(), R.drawable.ic_default_avatar,true);
//            UserModel.get().done(new ICallback() {
//                @Override
//                public void call(Arguments arguments) {
//                    userModel = arguments.get(0);
//                    wApplication.setUser(userModel);
////                PreferencesUtils.putInt(wApplication, PreferencesConstant.VALIDATED_USER,
////                        userModel.getInviteAmount());
////                PreferencesUtils.putString(wApplication, PreferencesConstant.INVITE_CODE,
////                        userModel.getInviteCode());
//                    ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_default_user);
//                }
//            });
            //    userName.setText(wApplication.getUser());
        }else{
            login.setVisibility(View.GONE);
            notlogin.setVisibility(View.VISIBLE);
        }

    }

    public void doClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.setting_bt:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                ViewUtil.startActivity(getActivity(), intent);
                break;
            case R.id.photo_image:
                Intent intent1 = new Intent(getActivity(), UserDataActivity.class);
                IsLoginUtil.isLogin(getActivity(),intent1);
                break;
            case R.id.my_store:
                Intent intent2 = new Intent(getActivity(), MyWarehouseActivity.class);
                IsLoginUtil.isLogin(getActivity(),intent2);
                break;
            case R.id.my_news:
                Intent intent3 = new Intent(getActivity(), MyNewsActivity.class);
                IsLoginUtil.isLogin(getActivity(), intent3);
                break;
            case R.id.my_address:
                Intent intent4 = new Intent(getActivity(), MyAddressActivity.class);
                IsLoginUtil.isLogin(getActivity(),intent4);
                break;
            case R.id.my_join:
                Intent intent5 = new Intent(getActivity(), MyInvolvedActivity.class);
                IsLoginUtil.isLogin(getActivity(),intent5);
                break;
            case R.id.my_order:
                Intent intent6 = new Intent(getActivity(), MyOrderActivity.class);
                IsLoginUtil.isLogin(getActivity(),intent6);
                break;
            case R.id.register_bt:
                Intent intent7= new Intent(getActivity(),RegisterActivity.class);
                ViewUtil.startActivity(getActivity(), intent7);
                break;
            case R.id.login_bt:
                Intent intent8= new Intent(getActivity(),LoginActivity.class);
                ViewUtil.startActivity(getActivity(), intent8);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(wApplication.isLogin()){
            login.setVisibility(View.VISIBLE);
            notlogin.setVisibility(View.GONE);
            UserModel.get().done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    userModel = arguments.get(0);
                    wApplication.setUser(userModel);
                    PreferencesUtils.putString(wApplication, PreferencesConstant.USER_BIRTHDAY, userModel.getBirthday());
                    PreferencesUtils.putString(wApplication, PreferencesConstant.NICK_NAME, userModel.getNickname());
                    PreferencesUtils.putString(wApplication, PreferencesConstant.USER_SEX, userModel.getSex());
                    ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_default_avatar,true);
                }
            });
            userName.setText(PreferencesUtils.getString(wApplication, PreferencesConstant.NICK_NAME));
        }else{
            login.setVisibility(View.GONE);
            notlogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
      //  super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
     //   cancelHomeDataTask();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
