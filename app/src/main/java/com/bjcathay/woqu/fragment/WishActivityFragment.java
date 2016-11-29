
package com.bjcathay.woqu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.LoginActivity;
import com.bjcathay.woqu.activity.PublishedActivity;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.util.ViewUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class WishActivityFragment extends Fragment {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RelativeLayout wishButton;
    private View view_1;
    private View view_2;
    private WishFragment wishFragment;
    private SunAwardFragment sunAwardFragment;
    private ArrayList<Fragment> fragmentList;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wish, container,
                false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WApplication.getInstance().addActivity(getActivity());
        initView();
        setData();
        settListeners();
    }

    private void settListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiaobutton_1:
                        viewPager.setCurrentItem(0);
                        view_1.setBackgroundResource(R.color.woqu_campagin_going);
                        view_2.setBackgroundResource(R.color.viewline);
                        wishButton.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radiaobutton_2:
                        viewPager.setCurrentItem(1);
                        view_2.setBackgroundResource(R.color.woqu_campagin_going);
                        view_1.setBackgroundResource(R.color.viewline);
                        wishButton.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioButton1.setChecked(true);
                        view_1.setBackgroundResource(R.color.woqu_campagin_going);
                        view_2.setBackgroundResource(R.color.viewline);
                        wishButton.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        radioButton2.setChecked(true);
                        view_2.setBackgroundResource(R.color.woqu_campagin_going);
                        view_1.setBackgroundResource(R.color.viewline);
                        wishButton.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        wishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WApplication.getInstance().isLogin()) {
                    Intent intent = new Intent(getActivity(), PublishedActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    ViewUtil.startActivity(getActivity(), intent);
                }

            }
        });
    }

    private void setData() {
        fragmentList = new ArrayList<Fragment>();
        wishFragment = new WishFragment();
        sunAwardFragment = new SunAwardFragment();
        Intent intent = getActivity().getIntent();
        String flase = intent.getStringExtra("flase");
        String page = intent.getStringExtra("page");
        if (page != null && page.equals("2")) {
            viewPager.setCurrentItem(1);
            radioButton2.setChecked(true);
            view_1.setBackgroundResource(R.color.viewline);
            view_2.setBackgroundResource(R.color.woqu_campagin_going);
            wishButton.setVisibility(View.VISIBLE);
            // sunAwardFragment = new SunAwardFragment(talk);
        } else if (page != null && page.equals("1")) {
            viewPager.setCurrentItem(0);
            radioButton1.setChecked(true);
            view_1.setBackgroundResource(R.color.woqu_campagin_going);
            view_2.setBackgroundResource(R.color.viewline);
        }
        fragmentList.add(wishFragment);
        fragmentList.add(sunAwardFragment);
        viewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
        if (page != null && page.equals("2")) {
            viewPager.setCurrentItem(1);
            radioButton2.setChecked(true);
        }
    }

    private void initView() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        radioButton1 = (RadioButton) view.findViewById(R.id.radiaobutton_1);
        radioButton2 = (RadioButton) view.findViewById(R.id.radiaobutton_2);
        wishButton = (RelativeLayout) view.findViewById(R.id.wish_button);
        view_1 = view.findViewById(R.id.view_1);
        view_2 = view.findViewById(R.id.view_2);
        wishButton.setVisibility(View.INVISIBLE);
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // cancelHomeDataTask();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }
}
