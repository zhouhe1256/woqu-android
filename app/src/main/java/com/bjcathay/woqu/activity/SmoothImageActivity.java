package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.util.Bimp;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class SmoothImageActivity extends Activity {
    private ArrayList<View> listViews = null;
    private ViewPager pager;
    private MyPageAdapter adapter;
    private RelativeLayout back;
    private int count;

    public ArrayList<String> bmp = new ArrayList<String>();
    public List<String> drr = new ArrayList<String>();
    public List<String> del = new ArrayList<String>();
    public int max;
    ImageView img;
    RelativeLayout photo_relativeLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        back = (RelativeLayout) findViewById(R.id.relativeLayout);
        photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        photo_relativeLayout.setVisibility(View.GONE);
        Bundle bundleObject = getIntent().getExtras();
        ArrayList<String> t = bundleObject.getStringArrayList("t");
        max = Bimp.max;
        pager = (ViewPager) findViewById(R.id.viewpager);
       // pager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < t.size(); i++) {
            initListViews(t.get(i));//
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        pager.setAdapter(adapter);// 设置适配器
        int id = bundleObject.getInt("ID");
        pager.setCurrentItem(id);



    }

    private void initListViews(String t) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        img = new ImageView(this);// 构造textView对象
        img.setBackgroundColor(0xff000000);
        ImageViewAdapter.adapt(img, t,true);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        listViews.add(img);// 添加view
    }



    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;// content

        private int size;// 页数

        public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {// 返回数量
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {// 返回view对象
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            listViews.get(arg1 % size).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SmoothImageActivity.this.finish();
                }
            });
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("浏览图片页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("浏览图片页面");
        MobclickAgent.onPause(this);
    }
}