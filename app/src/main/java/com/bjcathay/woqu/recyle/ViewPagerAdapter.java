package com.bjcathay.woqu.recyle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.remote.HttpClient;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.PointPraiseActivity;
import com.bjcathay.woqu.activity.PointPraiseEndActivity;
import com.bjcathay.woqu.activity.ShoppingActivity;
import com.bjcathay.woqu.activity.WishDetailsActivity;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.BannerModel;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {
        private PointPraiseModel pointPraiseMode;
        private List<BannerModel> b;
        public static Context mContext;
        private LoadingDialog progress;
    private  BannerModel bannerModel;
    public ViewPagerAdapter(Context context, List<BannerModel> b) {

            this.b = b;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return b.size();
        }



    @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container,  int position) {
           bannerModel = b.get(position);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.viewpager_item, container, false);
            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            ImageViewAdapter.adapt(iconView, bannerModel.getImageUrl(), R.drawable.exchange_default,true);

            container.addView(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String target = bannerModel.getType();
                    //      Logger.i("target",target);
                    Intent intent;
                    if ("URL".equals(target)) {
                        intent = new Intent(mContext, ShoppingActivity.class);
                        intent.putExtra("url", bannerModel.getTarget());
                        ViewUtil.startActivity(mContext, intent);
                    } else if ("CAMPAIGN".equals(target)) {
                        intent = new Intent(mContext,
                                WishDetailsActivity.class);
                        // intent.putExtra("imageurl", bannerModel.getImageUrl());
                        intent.putExtra("id",
                                bannerModel.getTarget());
                        ViewUtil.startActivity(mContext, intent);
                    } else if ("ACTIVITY".equals(target)) {
                        if (progress == null) {
                            progress = new LoadingDialog(mContext);
                        }
                        progress.show();
                        Logger.e("bannerModel",bannerModel.getTarget()+"");
                        String id=bannerModel.getTarget();
                        PointPraiseModel.getActivitys(bannerModel.getTarget()).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                pointPraiseMode = arguments.get(0);
                                if (pointPraiseMode.getSuccess()) {
                                    Intent intent;
                                    Logger.i("zhl",pointPraiseMode.getActivity().getStatus()+"6");
                                    if (pointPraiseMode.getActivity().getStatus() == 2) {
                                         intent = new Intent(mContext, PointPraiseEndActivity.class);
                                        intent.putExtra("id",bannerModel.getTarget());
                                        Logger.i("zhl","2");
                                    } else {
                                         intent = new Intent(mContext, PointPraiseActivity.class);
                                        intent.putExtra("id",bannerModel.getTarget());
                                        Logger.i("zhl","0");
                                    }
                                    WApplication.getInstance().pointPraiseflag = true;
                                    WApplication.getInstance().setPointPraiseMode(pointPraiseMode);
                                    mContext.startActivity(intent);
                                } else {
                                    DialogUtil.showMessage(pointPraiseMode.getMessage());
                                }
                                progress.dismiss();
                            }
                        }).fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage("网络异常");
                                progress.dismiss();
                            }
                        });
                    }
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }




}