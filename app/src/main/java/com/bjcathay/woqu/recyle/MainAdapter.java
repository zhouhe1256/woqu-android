
package com.bjcathay.woqu.recyle;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.LoginActivity;
import com.bjcathay.woqu.activity.PointPraiseActivity;
import com.bjcathay.woqu.activity.PointPraiseEndActivity;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ApiUrl;
import com.bjcathay.woqu.model.BannerModel;
import com.bjcathay.woqu.model.WoQuActivitysModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.SizeUtil;
import com.bjcathay.woqu.util.StartTime;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TimeTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPER_PAGER = 0;
    private static final int TYPE_ITEM = 1;
    private int p;
    private int p_2;
    private int p_3;
    public Boolean statrt_1 = true;
    public Boolean statrt_2 = true;
    public Boolean statrt_3 = true;
    private int page = 0;
    boolean first = false;
    private Runnable runnable;
    private static final int HEADER_PAGER_LAYOUT = 1;

    private List<BannerModel> mImageResID;
    private Handler handler = new Handler();
    private Context mContext;
    private List<WoQuActivitysModel> activities;

    public MainAdapter(Context context, List<BannerModel> b, List<WoQuActivitysModel> activities) {
        if (b == null) {
            this.mImageResID = new ArrayList<>();
        } else
            this.mImageResID = b;
        this.mContext = context;
        if (activities == null) {
            this.activities = new ArrayList<>();
        } else
            this.activities = activities;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                first = true;
                notifyItemChanged(0);
            }
        }, 5000);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPER_PAGER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_view_pager,
                    parent, false);
            return new HeaderViewHolder(v);

        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.woqu_text_lst_item, null);
            return new RecyclerViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mholder, int pos) {

        if (mholder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) mholder;

            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(mContext, mImageResID);
            headerHolder.viewPager.setAdapter(pagerAdapter);
            headerHolder.mIndicator.setViewPager(headerHolder.viewPager);

            headerHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    page = i;
                    handler.removeCallbacks(runnable);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (page < mImageResID.size() - 1) {
                                headerHolder.viewPager.setCurrentItem(page + 1, true);
                            } else {
                                headerHolder.viewPager.setCurrentItem(0, true);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 5000);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            if (first == true) {
                if (page <= mImageResID.size() - 1) {
                    headerHolder.viewPager.setCurrentItem(page + 1, true);
                } else {
                    headerHolder.viewPager.setCurrentItem(0, true);
                }
                first = false;
            }
        } else if (mholder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder holder = (RecyclerViewHolder) mholder;
            holder.setIsRecyclable(false);
            final int position = pos - 1;
            holder.giftTixingButton.setVisibility(View.GONE);
            holder.giftStatusText.setText(activities.get(position).getStatusLabel());
            holder.giftNameText.setText(activities.get(position).getTitle());
            if(activities.get(position).getNumber()==0){
                holder.giftNumberText.setText("不限");
            }else {
                holder.giftNumberText.setText(activities.get(position).getNumber() + "");
            }
           // holder.giftMoneyText.setText(activities.get(position).getPrice() + "0");

            ImageViewAdapter.adapt(holder.giftImage, activities.get(position).getImageUrl(),true);
           if(activities.get(position).getTagUrl()!=null&&!activities.get(position).getTagUrl().isEmpty()){
               ImageViewAdapter.adapt(holder.giftPbImage, activities.get(position).getTagUrl(),true);
           }

            if (activities.get(position).getAttend()) {
                holder.giftTixingButton.setText("已提醒");
                holder.giftTixingButton.setBackgroundResource(R.drawable.button_click_red_circle);
            } else {
                holder.giftTixingButton.setText("提醒我");
                holder.giftTixingButton.setBackgroundResource(R.drawable.nobt_solid_bg);
            }
            long[] time;
            int status = activities.get(position).getStatus();
            switch (status) {
                case 0:
                    holder.giftNumberImage3.setImageBitmap(
                            BitmapFactory.decodeResource(
                                    mContext.getResources(),R.drawable.money));
                    holder.giftNumberName3.setText("市场价格:");
                    holder.giftMoneyText.setText(activities.get(position).getPrice() + "0");
                    holder.giftNumberText.setVisibility(View.VISIBLE);
                    holder.giftNameText.setVisibility(View.VISIBLE);
                    holder.giftNumberImage.setVisibility(View.VISIBLE);
                    holder.giftNumberName.setVisibility(View.VISIBLE);
                    holder.giftshoppName.setVisibility(View.GONE);
                    if (activities.get(position).getRemind()) {
                        holder.giftTixingButton.setText("已提醒");
                        holder.giftTixingButton.setBackgroundResource(R.drawable.nobt_solid_bg);
                    } else {
                        holder.giftTixingButton.setText("提醒我");
                        holder.giftTixingButton
                                .setBackgroundResource(R.drawable.button_click_red_circle);
                    }

                    holder.relative.setVisibility(View.VISIBLE);
                    holder.giftTixingButton.setVisibility(View.VISIBLE);
                    holder.giftTime.setVisibility(View.VISIBLE);

                    time = StartTime.setTime(activities.get(position).getStartAt(),
                            activities.get(position).getNow());
                    if (time[0] == 0) {
                        if (!holder.giftTimeText.isRun()) {
                            holder.giftTimeText.setTimes(time);
                            holder.giftTimeText.run();
                        }

                    } else {
                        String t = StartTime.setStartTime(activities.get(position).getStartAt(),
                                activities.get(position).getNow());
                        holder.giftTimeText.setText(t);
                    }

                    holder.giftTimeTag.setText("距开始:");
                    if (statrt_3) {
                        holder.giftStatusRelative.setVisibility(View.VISIBLE);
                        holder.view.setVisibility(View.GONE);
                        statrt_3 = false;
                        p_3 = position;
                    } else {
                        if (p_3 != position) {
                            holder.giftStatusRelative.setVisibility(View.GONE);
                            holder.view.setVisibility(View.VISIBLE);
                        } else {
                            holder.giftStatusRelative.setVisibility(View.VISIBLE);
                            holder.view.setVisibility(View.GONE);
                        }
                    }
                    holder.giftTixingButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (WApplication.getInstance().isLogin()) {

                                if (activities.get(position).getRemind()) {
                                } else {
                                    Http.instance()
                                            .post(ApiUrl
                                                    .getRemind(activities.get(position).getId()))
                                            .isCache(false).run().done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            JSONObject j = arguments.get(0);
                                            try {
                                                Boolean f = j.getBoolean("success");
                                                if (f) {
                                                    activities.get(position)
                                                            .setRemind(true);
                                                    holder.giftTixingButton.setText("已提醒");
                                                    holder.giftTixingButton
                                                            .setBackgroundResource(
                                                                    R.drawable.nobt_solid_bg);
                                                } else {
                                                    DialogUtil.showMessage(j
                                                            .getString("message"));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).fail(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            DialogUtil.showMessage("提醒失败,网络异常");
                                        }
                                    });
                                }
                            } else {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                ViewUtil.startActivity(mContext, intent);
                            }
                        }
                    });
                    break;
                case 1:
                    holder.giftNumberImage3.setImageBitmap(
                            BitmapFactory.decodeResource(
                                    mContext.getResources(),R.drawable.money));
                    holder.giftNumberName3.setText("市场价格:");
                    holder.giftNameText.setVisibility(View.VISIBLE);
                    holder.giftNumberImage.setVisibility(View.VISIBLE);
                    holder.giftNumberName.setVisibility(View.VISIBLE);
                    holder.giftNumberText.setVisibility(View.VISIBLE);
                    holder.giftStatusRelative.setVisibility(View.GONE);
                    holder.giftshoppName.setVisibility(View.GONE);
                    holder.relative.setVisibility(View.GONE);
                    holder.giftMoneyText.setText(activities.get(position).getPrice() + "0");
                    time = StartTime.setTime(activities.get(position).getEndAt(),
                            activities.get(position).getNow());
                    if (time[0] == 0) {
                        if (!holder.giftTimeText.isRun()) {
                            holder.giftTimeText.setTimes(time);
                            holder.giftTimeText.run();
                        }

                    } else {
                        String t = StartTime.setStartTime(activities.get(position).getEndAt(),
                                activities.get(position).getNow());
                        holder.giftTimeText.setText(t);
                    }

                    holder.giftTimeTag.setText("距结束:");
                    holder.giftTime.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    holder.giftNameText.setVisibility(View.GONE);
                    holder.giftNumberImage.setVisibility(View.GONE);
                    holder.giftNumberName.setVisibility(View.GONE);
                    holder.giftNumberText.setVisibility(View.GONE);
                    holder.giftshoppName.setText(activities.get(position).getTitle());
                    holder.giftshoppName.setVisibility(View.VISIBLE);
                    holder.giftNumberText.setTextSize(SizeUtil.convertDIP2PX(mContext, 7));
                    holder.relative.setVisibility(View.GONE);
                    holder.giftTime.setVisibility(View.GONE);

                    holder.giftNumberImage3.setImageBitmap(
                            BitmapFactory.decodeResource(
                                    mContext.getResources(),R.drawable.gift));
                    holder.giftNumberName3.setText("礼物数量:");
                    if(activities.get(position).getNumber()==0){
                        holder.giftMoneyText.setText("不限");
                    }else {
                        holder.giftMoneyText.setText(activities.get(position).getNumber() + "");
                    }
                    if (statrt_2) {
                        holder.giftStatusRelative.setVisibility(View.VISIBLE);
                        holder.view.setVisibility(View.GONE);
                        statrt_2 = false;
                        p_2 = position;
                    } else {
                        if (p_2 != position) {
                            holder.giftStatusRelative.setVisibility(View.GONE);
                            holder.view.setVisibility(View.VISIBLE);
                        } else {
                            holder.giftStatusRelative.setVisibility(View.VISIBLE);
                            holder.view.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    int status = activities.get(position).getStatus();
                    String id1 = activities.get(position).getId();
                    if (status == 0) {
                        intent = new Intent(mContext, PointPraiseActivity.class);

                    } else if (status == 1) {
                        intent = new Intent(mContext, PointPraiseActivity.class);
                    } else {
                        intent = new Intent(mContext, PointPraiseEndActivity.class);
                    }
                    intent.putExtra("id", id1);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPER_PAGER;
        }
        return TYPE_ITEM;
    }

    public boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return activities.size() + HEADER_PAGER_LAYOUT;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        CirclePageIndicator mIndicator;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.viewPager = (ViewPager) itemView.findViewById(R.id.pager);
            this.mIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView giftPbImage;
        public TextView giftNumberText;
        public TimeTextView giftTimeText;
        public TextView giftMoneyText;
        public TextView giftStatusText;
        public TextView giftNameText;
        public TextView giftTimeTag;
        public LinearLayout giftTime;
        public LinearLayout giftStatusRelative;
        public Button giftTixingButton;
        public ImageView giftImage;
        public RelativeLayout linear;
        public RelativeLayout relative;
        public ImageView giftNumberImage;
        public TextView giftNumberName;
        public TextView giftshoppName;
        public ImageView giftNumberImage3;
        public TextView giftNumberName3;
        public View view;

        public RecyclerViewHolder(View view) {
            super(view);
            this.giftPbImage = (ImageView) view.findViewById(R.id.woqu_gift_pb);
            this.giftMoneyText = (TextView) view.findViewById(R.id.woqu_gift_money);
            this.giftNameText = (TextView) view.findViewById(R.id.woqu_gift_name);
            this.giftNumberText = (TextView) view.findViewById(R.id.woqu_gift_number_m);
            this.giftTimeText = (TimeTextView) view.findViewById(R.id.woqu_gift_time);
            this.giftStatusText = (TextView) view.findViewById(R.id.woqu_gift_status_text);
            this.giftStatusRelative = (LinearLayout) view.findViewById(R.id.woqu_gift_staus);
            this.giftTixingButton = (Button) view.findViewById(R.id.woqu_gift_tixing);
            this.giftTimeTag = (TextView) view.findViewById(R.id.woqu_gift_time_tag);
            this.giftTime = (LinearLayout) view.findViewById(R.id.woqu_gift_time_line);
            this.giftImage = (ImageView) view.findViewById(R.id.woqu_gift_image);
            this.linear = (RelativeLayout) view.findViewById(R.id.dianji_list);
            this.relative = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            this.view = view.findViewById(R.id.view_line);
            this.giftNumberImage = (ImageView) view.findViewById(R.id.gift_number_image);
            this.giftNumberName = (TextView) view.findViewById(R.id.gift_number_text);
            this.giftshoppName = (TextView) view.findViewById(R.id.gift_shopp_name);
            this.giftNumberImage3 = (ImageView) view.findViewById(R.id.gift_image_number_3);
            this.giftNumberName3 = (TextView) view.findViewById(R.id.gift_text_number_3);
            // giftTixingButton.setOnClickListener(this);

        }
    }

}
