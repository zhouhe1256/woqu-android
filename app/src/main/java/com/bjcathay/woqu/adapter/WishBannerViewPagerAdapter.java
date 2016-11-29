
package com.bjcathay.woqu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.ShoppingActivity;
import com.bjcathay.woqu.model.BannerModel;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.JazzyViewPager;
import com.bjcathay.woqu.view.OutlineContainer;

import java.util.List;

/**
 * Created by dengt on 15-4-24.
 */
public class WishBannerViewPagerAdapter extends PagerAdapter {
    private Activity context;
    private JazzyViewPager bannerViewPager;
    private List<BannerModel> items;

    public WishBannerViewPagerAdapter(Activity context, JazzyViewPager bannerViewPager,
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

            }
        });
        return convertView;
    }
}
