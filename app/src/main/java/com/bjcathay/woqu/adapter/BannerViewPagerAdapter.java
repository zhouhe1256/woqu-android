
package com.bjcathay.woqu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.PointPraiseActivity;
import com.bjcathay.woqu.activity.ShoppingActivity;
import com.bjcathay.woqu.activity.WishDetailsActivity;
import com.bjcathay.woqu.model.BannerModel;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.JazzyViewPager;
import com.bjcathay.woqu.view.OutlineContainer;


import org.json.JSONObject;

import java.util.List;

/**
 * Created by dengt on 15-4-24.
 */
public class BannerViewPagerAdapter extends PagerAdapter {
    private Activity context;
    private JazzyViewPager bannerViewPager;
    private List<BannerModel> items;


    public BannerViewPagerAdapter(Activity context, JazzyViewPager bannerViewPager,
                                  List<BannerModel> recommendations) {
        this.context = context;
        this.bannerViewPager = bannerViewPager;
        this.items = recommendations;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(bannerViewPager.findViewFromObject(position));
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_home_banner,
                container, false);

        final BannerModel bannerModel = items.get(position);
        ImageView bgView = ViewUtil.findViewById(convertView, R.id.bg);
        ImageViewAdapter.adapt(bgView, bannerModel.getImageUrl(), R.drawable.exchange_default,true);
        container.addView(convertView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bannerViewPager.setObjectForPosition(convertView, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String target = bannerModel.getType();
          //      Logger.i("target",target);
                Intent intent;
                if ("URL".equals(target)) {
                    intent = new Intent(context, ShoppingActivity.class);
                    intent.putExtra("url", bannerModel.getTarget());
                    ViewUtil.startActivity(context, intent);
                } else if ("CAMPAIGN".equals(target)) {
                     intent = new Intent(context,
                     WishDetailsActivity.class);
                    // intent.putExtra("imageurl", bannerModel.getImageUrl());
                    intent.putExtra("id",
                    bannerModel.getTarget());
                    ViewUtil.startActivity(context, intent);
                } else if ("ACTIVITY".equals(target)) {
                    intent = new Intent(context, PointPraiseActivity.class);
                    intent.putExtra("id", bannerModel.getTarget());
                    ViewUtil.startActivity(context, intent);
                }
            }
        });
        return convertView;
    }
}
