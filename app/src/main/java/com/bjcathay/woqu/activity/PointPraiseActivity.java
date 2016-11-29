
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ApiUrl;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.ShareModel;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.model.WoQuActivitysModel;
import com.bjcathay.woqu.recyle.WoQuTextActivityFragment;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.ShareUtil;
import com.bjcathay.woqu.util.StartTime;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.DeleteInfoDialog;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.NotifyingScrollView;
import com.bjcathay.woqu.view.TimeTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;

public class PointPraiseActivity extends Activity
        implements DeleteInfoDialog.DeleteInfoDialogResult {
    private ImageView backImage;
    private ImageView imageView;
    private ImageView tagImageView;
    private Button shareButton;
    private Button winnerButton;
    private TextView giftTitleText;
    private TextView giftNumberText;
    private TextView giftMoneyrText;
    private TimeTextView giftTimerText;
    private TextView activityRuleText;
    private TextView zanTextView;
    private TextView shoppingTextView;
    private TextView textView;
    private RelativeLayout layoutHead;
    private NotifyingScrollView scrollView;
    protected LoadingDialog progress;
    private String luckyType;
    private RelativeLayout ifStatus;
    private WebView webView;
    private PointPraiseModel pointPraiseModel;
    private String id;
    private ShareModel shareModel;
    private int status;
    private Boolean attend = false;
    private Boolean remind = false;
    private String url;
    private String shareUrl;
    private View view;
    private View view_l;
    private String token;
    private long[] t;
    private String dialogText="你已参与成功,坐等大奖砸中吧";
    private String buttonText="确定";
    private int flag=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_praise);
        initView();
        setListerens();
        if (pointPraiseModel == null) {
            setData();
        }

        layoutHead.getBackground().setAlpha(0);
        view_l.getBackground().setAlpha(0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // if(pointPraiseModel==null){
        setData();
        // }

    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("瀑布流详情页面");
        MobclickAgent.onResume(this);
    }

    private void setData() {
        if (progress == null) {
            progress = new LoadingDialog(this);
        }
        if(flag==1){
            progress.show();
        }

        PointPraiseModel.getActivitys(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                pointPraiseModel = arguments.get(0);
                if (pointPraiseModel.getSuccess()) {
                    setDataMethod();
                } else {
                    DialogUtil.showMessage(pointPraiseModel.getMessage());
                }
                progress.dismiss();
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });
        flag=0;
    }

    private void setDataMethod() {
        giftTitleText.setText(pointPraiseModel.getActivity().getTitle());
        if(pointPraiseModel.getActivity().getNumber()==0){
            giftNumberText.setText("不限");

        }else {
            giftNumberText.setText(
                    pointPraiseModel.getActivity().getNumber() + "");
        }
        giftMoneyrText.setText(pointPraiseModel.getActivity().getPrice() + "0");
        url = pointPraiseModel.getActivity().getGift().getFriendlyLink();
        shareUrl = pointPraiseModel.getActivity().getAttendResultUrl()
                + "&token=" + token;
        luckyType = pointPraiseModel.getActivity().getLuckyType();
        String time = null;
        attend = pointPraiseModel.getActivity().getAttend();
        remind = pointPraiseModel.getActivity().getRemind();
        status = pointPraiseModel.getActivity().getStatus();
        ImageViewAdapter.adapt(tagImageView, pointPraiseModel.getActivity().getTagUrl(), true);
        String imageUrl = pointPraiseModel.getActivity().getImageUrl();
        ImageViewAdapter.adapt(imageView, imageUrl, true);
       if(url!=null&&!url.isEmpty()){
            shoppingTextView.setVisibility(View.VISIBLE);
        }
        if(luckyType.equals("RANDOM")){
            view.setVisibility(View.GONE);
            ifStatus.setVisibility(View.GONE);
            dialogText="你已参与成功,坐等大奖砸中吧";
            buttonText="我知道了";
        }else{
            buttonText="确定";
            dialogText="让小伙伴帮忙去集赞咯";
            view.setVisibility(View.VISIBLE);
            ifStatus.setVisibility(View.VISIBLE);
        }

        if (status == 1) {
            time = StartTime.setStartTime(pointPraiseModel.getActivity().getEndAt(),
                    pointPraiseModel.getActivity().getNow());
            t = StartTime.setTime(pointPraiseModel.getActivity().getEndAt(),
                    pointPraiseModel.getActivity().getNow());
            if (attend && luckyType.equals("RANDOM")) {
                /*view.setVisibility(View.VISIBLE);
                ifStatus.setVisibility(View.VISIBLE);*/
                shareButton.setText("已参与");
                shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));

            } else if (luckyType.equals("SATISFY")) {
                zanTextView.setText("点击查看活动进展");
                if (attend) {
                    ifStatus.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    winnerButton.setVisibility(View.VISIBLE);
                    winnerButton.setBackgroundResource(R.drawable.purple_click);
                    shareButton.setBackgroundResource(R.drawable.red_click);
                    shareButton.setText("再次分享");
                } else {
                    shareButton.setText("我想要");
                    ifStatus.setVisibility(View.GONE);
                }
            } else if (luckyType.equals("BEFORE")) {

                zanTextView.setText("点击查看活动进展");

                if (attend) {
                    ifStatus.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    winnerButton.setVisibility(View.GONE);
                    shareButton.setText("再次分享");
                } else {
                    shareButton.setText("我想要");
                    ifStatus.setVisibility(View.GONE);
                }
            }
        } else if (status == 0) {
            textView.setText("距开始:");
            time = StartTime.setStartTime(pointPraiseModel.getActivity().getStartAt(),
                    pointPraiseModel.getActivity().getNow());
            t = StartTime.setTime(pointPraiseModel.getActivity().getStartAt(),
                    pointPraiseModel.getActivity().getNow());
            ifStatus.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            if (remind) {
                shareButton.setText("已提醒");
                shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));

            } else {
                shareButton.setText("开始提醒");
            }
        }

        if (t != null && t[0] == 0) {
            if (!giftTimerText.isRun()) {
                giftTimerText.setTimes(t);
                giftTimerText.run();
            }
        } else {
            giftTimerText.setText(time);
        }
        activityRuleText.setText(pointPraiseModel.getActivity().getRule());
        webView.loadDataWithBaseURL(null,
                pointPraiseModel.getActivity().getGift().getGiftDetail(),
                "text/html", "utf-8", null);
    }

    private void setListerens() {
        scrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {

                layoutHead.getBackground().setAlpha(t < 255 ? t : 255);
                view_l.getBackground().setAlpha(t < 255 ? t : 255);
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (WApplication.getInstance().isLogin()) {
                    if (status == 0) {
                        if (remind) {
                            /*
                             * DeleteInfoDialog infoDialog = new
                             * DeleteInfoDialog( PointPraiseActivity.this,
                             * R.style.InfoDialog, "已取消提醒", 1l,
                             * PointPraiseActivity.this); infoDialog.f = true;
                             * infoDialog.show();
                             */
                        } else {
                            DeleteInfoDialog infoDialog = new DeleteInfoDialog(
                                    PointPraiseActivity.this, R.style.InfoDialog,
                                    "已提醒", 1l, PointPraiseActivity.this);
                            infoDialog.f = true;
                            infoDialog.show();
                            infoDialog.setDialogConfirmText(buttonText);
                        }

                    } else if (status == 1) {
                        if (attend && luckyType.equals("RANDOM")) {
                            shareButton.setText("已参与");
                            shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));

                        } else {
                            if (attend&&luckyType.equals("SATISFY")||attend&&luckyType.equals("BEFORE")) {
//                                if (progress == null) {
//                                    progress = new LoadingDialog(PointPraiseActivity.this);
//                                }
//                                progress.show();
                                if (luckyType.equals("SATISFY")) {
                                    ShareModel.share(id).done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            shareModel = arguments.get(0);
                                            Boolean b;
                                            b = shareModel.getSuccess();
                                            if (b) {
                                                view.setVisibility(View.VISIBLE);
                                                ifStatus.setVisibility(View.VISIBLE);
                                                winnerButton.setVisibility(View.VISIBLE);
                                                winnerButton.setBackgroundResource(
                                                        R.color.blue_btn_color);
                                                shareButton.setBackgroundResource(
                                                        R.color.woqu_campagin_going);
                                                ShareUtil.showShare(PointPraiseActivity.this,
                                                        shareModel, id, "woqu",
                                                        null);
                                                shareButton.setText("再次分享");

                                                String result = shareModel.getResult();
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    shareUrl = jsonObject.getString("url")
                                                            + "&token=" + token;
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                String message = shareModel.getMessage();
                                                DialogUtil.showMessage(message);

                                            }
                                          //  progress.dismiss();
                                        }

                                    }).fail(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            DialogUtil.showMessage("网络异常");
                                         //   progress.dismiss();
                                        }
                                    });
                                } else if (luckyType.equals("BEFORE")) {
                                    ShareModel.share(id).done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            shareModel = arguments.get(0);
                                            Boolean b;
                                            b = shareModel.getSuccess();
                                            Logger.i("shareSuccess", b + "");
                                            if (b) {
                                                view.setVisibility(View.VISIBLE);
                                                ifStatus.setVisibility(View.VISIBLE);
                                                // winnerButton.setVisibility(View.GONE);
                                                ShareUtil.showShare(PointPraiseActivity.this,
                                                        shareModel, id, "woqu",
                                                        null);
                                                shareButton.setText("再次分享");

                                                String result = shareModel.getResult();
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    shareUrl = jsonObject.getString("url")
                                                            + "&token=" + token;
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                String message = shareModel.getMessage();
                                                DialogUtil.showMessage(message);

                                            }
                                         //   progress.dismiss();
                                        }
                                    }).fail(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            DialogUtil.showMessage("网络异常");
                                         //   progress.dismiss();
                                        }
                                    });
                                }

                            } else {
                                DeleteInfoDialog infoDialog = new DeleteInfoDialog(
                                        PointPraiseActivity.this, R.style.InfoDialog,
                                        dialogText, 0l, PointPraiseActivity.this);
                                infoDialog.f = true;
                                infoDialog.show();
                                infoDialog.setDialogConfirmText(buttonText);
                            }
                        }
                    }
                } else {
                    DialogUtil.showMessage("用户未登录");
                    Intent intent = new Intent(PointPraiseActivity.this, LoginActivity.class);
                    ViewUtil.startActivity(PointPraiseActivity.this, intent);
                }
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        winnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PointPraiseActivity.this, WinnerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("tag",0);
                WApplication.getInstance().pointPraiseflag = true;
                WApplication.getInstance().setPointPraiseMode(pointPraiseModel);
                startActivity(intent);
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        shoppingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PointPraiseActivity.this, ShoppingActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        ifStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         //       if (WApplication.getInstance().isLogin()) {

                if(shareUrl==null||shareUrl.isEmpty()){
                    DialogUtil.showMessage("暂无活动进展情况");
                }else{
                    Intent intent = new Intent(PointPraiseActivity.this,
                            PointPraiseProgressActivity.class);
                    intent.putExtra("url", shareUrl);
                    startActivity(intent);
                }
//                } else {
//                    Intent intent1 = new Intent(PointPraiseActivity.this, LoginActivity.class);
//                    ViewUtil.startActivity(PointPraiseActivity.this, intent1);
//                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WApplication.getInstance().clearPointPraiseModel();
    }

    private void initView() {
        Logger.i("zhl","ff");
        view = findViewById(R.id.view_line);
        view_l = findViewById(R.id.view_l);
        layoutHead = (RelativeLayout) findViewById(R.id.head_f);
        scrollView = (NotifyingScrollView) findViewById(R.id.scrollView);
        shareButton = (Button) findViewById(R.id.woqu_gift_share);
        backImage = (ImageView) findViewById(R.id.title_back_img);
        winnerButton = (Button) findViewById(R.id.woqu_gift_button);
        giftTitleText = (TextView) findViewById(R.id.woqu_gift_name);
        giftNumberText = (TextView) findViewById(R.id.woqu_gift_number);
        giftMoneyrText = (TextView) findViewById(R.id.woqu_gift_money);
        giftTimerText = (TimeTextView) findViewById(R.id.woqu_gift_time);
        activityRuleText = (TextView) findViewById(R.id.woqu_gift_rule_count);
        webView = (WebView) findViewById(R.id.woqu_gift_webview);
        ifStatus = (RelativeLayout) findViewById(R.id.woqu_ifstart);
        ifStatus.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        imageView = (ImageView) findViewById(R.id.woqu_gift_image);
        tagImageView = (ImageView) findViewById(R.id.woqu_gift_pb);
        zanTextView = (TextView) findViewById(R.id.zantext);
        zanTextView.setText("点击查看活动进展");
        shoppingTextView = (TextView) findViewById(R.id.shopping);
        textView=(TextView)findViewById(R.id.text_view);
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        }

        if (getIntent().getStringExtra("id")== null) {
            pointPraiseModel = WApplication.getInstance().getpointPraiseMode();
            id = pointPraiseModel.getActivity().getId();
            setDataMethod();
        }
        token = WApplication.getInstance().getApiToken();
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDisplayZoomControls(false);


    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (progress == null) {
            progress = new LoadingDialog(this);
        }
       // progress.show();
        if (targetId == 0l) {
            if (!attend && luckyType.equals("RANDOM")) {
                ShareModel.participate(id).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        ShareModel participate = arguments.get(0);
                        if (participate != null) {
                            if (participate.getSuccess()) {
                                attend = true;
                                view.setVisibility(View.VISIBLE);
                                ifStatus.setVisibility(View.VISIBLE);
                                shareButton.setText("已参与");
                                shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));

                                String result = participate.getResult();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    shareUrl = jsonObject.getString("url") + "&token=" + token;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                DialogUtil.showMessage(participate.getMessage());
                            }
                        }
                     //   progress.dismiss();
                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                    //    progress.dismiss();
                    }
                });


            } else if (luckyType.equals("SATISFY")) {
                ShareModel.share(id).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        shareModel = arguments.get(0);
                        Boolean b;
                        b = shareModel.getSuccess();
                        if (b) {
                            view.setVisibility(View.VISIBLE);
                            ifStatus.setVisibility(View.VISIBLE);
                            winnerButton.setVisibility(View.VISIBLE);
                            winnerButton.setBackgroundResource(R.color.blue_btn_color);
                            shareButton.setBackgroundResource(R.color.woqu_campagin_going);
                            ShareUtil.showShare(PointPraiseActivity.this, shareModel, id, "woqu",
                                    null);
                            shareButton.setText("再次分享");

                            String result = shareModel.getResult();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                shareUrl = jsonObject.getString("url") + "&token=" + token;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            String message = shareModel.getMessage();
                            DialogUtil.showMessage(message);

                        }
                      //  progress.dismiss();
                    }

                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                      //  progress.dismiss();
                    }
                });
            } else if (luckyType.equals("BEFORE")) {
                ShareModel.share(id).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        shareModel = arguments.get(0);
                      //  progress.dismiss();
                        Boolean b;
                        b = shareModel.getSuccess();
                        Logger.i("shareSuccess", b + "");
                        if (b) {
                            view.setVisibility(View.VISIBLE);
                            ifStatus.setVisibility(View.VISIBLE);
                            // winnerButton.setVisibility(View.GONE);
                            ShareUtil.showShare(PointPraiseActivity.this, shareModel, id, "woqu",
                                    null);
                            shareButton.setText("再次分享");

                            String result = shareModel.getResult();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                shareUrl = jsonObject.getString("url") + "&token=" + token;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            String message = shareModel.getMessage();
                            DialogUtil.showMessage(message);

                        }

                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                      //  progress.dismiss();
                        DialogUtil.showMessage(getString(R.string.empty_net_text));

                    }
                });
            }

        }
        if (targetId == 1l) {
            if (progress == null) {
                progress = new LoadingDialog(this);
            }
            progress.show();
            if (remind) {
                /*
                 * Http.instance(). post(ApiUrl. getRemind(id)).
                 * param("_method","DELETE"). run(). done(new ICallback() {
                 * @Override public void call(Arguments arguments) { JSONObject
                 * j = arguments.get(0); try { Boolean f =
                 * j.getBoolean("success"); if (f) { remind = false;
                 * shareButton.setText("开始提醒");
                 * shareButton.setBackgroundResource(R.color.woqu_campagin_going
                 * ); } else { DialogUtil.showMessage(j.getString("message")); }
                 * } catch (JSONException e) { e.printStackTrace(); } }
                 * }).fail(new ICallback() {
                 * @Override public void call(Arguments arguments) {
                 * DialogUtil.showMessage("取消提醒失败,网络异常"); } });
                 */
            } else {
                Http.instance().post(ApiUrl.getRemind(id)).isCache(false).run()
                        .done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject j = arguments.get(0);
                                try {
                                    Boolean f = j.getBoolean("success");
                                    if (f) {
                                        remind = true;
                                        shareButton.setText("已提醒");
                                        shareButton.setBackgroundColor(Color.parseColor("#C8C8C8"));
                                        int position = getIntent().getIntExtra("position", 0);
                                        Intent intent = new Intent(
                                                WoQuTextActivityFragment.ACTION_NAME);
                                        intent.putExtra("position", position);
                                        sendBroadcast(intent);
                                    } else {
                                        DialogUtil.showMessage(j.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progress.dismiss();

                            }
                        }).fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage("提醒失败,网络异常");
                                progress.dismiss();
                            }
                        });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("瀑布流详情页面");
        MobclickAgent.onPause(this);
    }

}
