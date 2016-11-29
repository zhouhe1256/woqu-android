
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.SunAwardDetailsImageAdapter;
import com.bjcathay.woqu.adapter.SunAwardDetailsListAdapter;
import com.bjcathay.woqu.adapter.SunAwardDetailsZanImageAdapter;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.constant.ApiUrl;
import com.bjcathay.woqu.fragment.SunAwardFragment;
import com.bjcathay.woqu.model.CommentModel;
import com.bjcathay.woqu.model.LikesModel;
import com.bjcathay.woqu.model.SunAwardDetailsTalkModel;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.SystemUtil;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.GridViewForScrollView;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.RoundCornerImageView;
import com.bjcathay.woqu.view.ScrollViewWithListView;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SunAwardDetailsActivity extends Activity implements View.OnClickListener {
    private GridViewForScrollView imageGridView;
    private TextView nameTextView;
    private RoundCornerImageView headImageView;
    private GridViewForScrollView zanGridView;
    private ScrollViewWithListView sunDetailsListView;
    private LinearLayout informationImage;
    private TextView contentTextView;
    protected LoadingDialog progress;
    private TextView timeTextView;
    private TextView deviceNameTextView;
    private LinearLayout zanImage;
    private LinearLayout sendMessageLinear;
    private LinearLayout messageLinear;
    private LinearLayout addressLinear;
    private TextView zanNumberTextView;
    private EditText contentEdit;
    private ImageView zanImageView;
    private ImageView xiaoxiImage;
    private TextView addressTextView;
    private Button sendButton;
    private ScrollView rela;
    private List<String> contentLilst = new ArrayList<String>();
    private SunAwardDetailsImageAdapter adapter;
    private SunAwardDetailsListAdapter contentAdapter;
    private CommentModel commentModel;
    private SunAwardDetailsTalkModel sunAwardDetailsTalkModel;
    private SunAwardDetailsZanImageAdapter zanAdapter;
    private TextView xiaoxiTextView;
    private TopView topView;
    private Boolean click = true;
    private Boolean ifZan = false;
    private String id;
    private List<LikesModel> likes = new ArrayList<LikesModel>();
    private List<LikesModel> likesMore = new ArrayList<LikesModel>();
    private int width;
    private int position;
    private int zanNumber;
    private String xiaoxiNumber;
    private View viewline;
    private RelativeLayout likelayout;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout.LayoutParams layoutParams2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                setData();
                if (commentModel != null && contentAdapter != null) {
                    contentAdapter.notifyDataSetChanged();

                }
            }
            if (msg.what == 2) {

                setData();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun_award_details);
        width = SystemUtil.getScriptWidth() - 30;
        layoutParams = new LinearLayout.LayoutParams(width / 3 * 2 + 25,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();
        setData();
        setListeners();
    }

    private void setListeners() {

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if(WApplication.getInstance().isLogin()){
                String contents = contentEdit.getText().toString().trim();
                if (contents.equals("") || contents.equals(null)) {
                    DialogUtil.showMessage("内容不能为空");
                } else {
                    messageLinear.setVisibility(View.VISIBLE);
                    sendMessageLinear.setVisibility(View.GONE);
                    Http.instance().post(ApiUrl.talksComment(id)).param("content", contents).isCache(false).run()
                            .done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    JSONObject j = arguments.get(0);
                                    Boolean f = false;
                                    try {
                                        f = j.getBoolean("success");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (f) {

                                        contentEdit.setText(null);
                                        Message msg = handler.obtainMessage();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }else{
                                        try {
                                            String message = j.getString("message");
                                            DialogUtil.showMessage(message);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }).fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            DialogUtil.showMessage("发送失败网络异常");
                            progress.dismiss();
                        }
                    });
                }
                // }else{
                // Intent intent = new Intent(SunAwardDetailsActivity.this,
                // LoginActivity.class);
                // ViewUtil.startActivity(SunAwardDetailsActivity.this, intent);
                // }
            }
        });

        informationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WApplication.getInstance().isLogin()) {
                    sendMessageLinear.setVisibility(View.VISIBLE);
                    messageLinear.setVisibility(View.GONE);
                    contentEdit.requestFocus();
                    InputMethodManager imm = (InputMethodManager) contentEdit.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                } else {
                    Intent intent = new Intent(SunAwardDetailsActivity.this, LoginActivity.class);
                    ViewUtil.startActivity(SunAwardDetailsActivity.this, intent);
                }
            }
        });
        zanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WApplication.getInstance().isLogin()) {
                    if (click) {
                        click = false;
                        if (ifZan) {
                            ifZan = false;
                            zanImageView.setBackgroundResource(R.drawable.zan);
                            --zanNumber;
                            Http.instance().post(ApiUrl.getTalksLike(id)).isCache(false).param("_method", "DELETE")
                                    .run().done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    JSONObject jsonObject = arguments.get(0);
                                    Boolean j = false;
                                    click = true;
                                    try {
                                        j = jsonObject.getBoolean("success");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (j) {
                                        Message msg = handler.obtainMessage();
                                        msg.what = 2;
                                        handler.sendMessage(msg);
                                    }

                                }
                            }).fail(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    DialogUtil.showMessage("取消赞失败,当前网络异常");
                                    zanImageView.setBackgroundResource(R.drawable.zan_is);
                                    --zanNumber;
                                    ifZan = true;
                                    progress.dismiss();
                                }
                            });
                        } else {
                            click = false;
                            ifZan = true;
                            zanNumber = zanNumber + 1;
                            Logger.i("ceshi", "p" + zanNumber);

                            zanImageView.setBackgroundResource(R.drawable.zan_is);
                            Http.instance().post(ApiUrl.getTalksLike(id)).isCache(false).run()
                                    .done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            JSONObject jsonObject = arguments.get(0);
                                            click = true;

                                            Message msg = handler.obtainMessage();
                                            msg.what = 2;
                                            handler.sendMessage(msg);

                                        }
                                    }).fail(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    DialogUtil.showMessage("点赞失败,当前网络异常");
                                    ifZan = false;

                                    zanImageView.setBackgroundResource(R.drawable.zan);
                                    --zanNumber;
                                    progress.dismiss();
                                }
                            });
                        }
                    }
                } else {
                    Intent intent = new Intent(SunAwardDetailsActivity.this, LoginActivity.class);
                    ViewUtil.startActivity(SunAwardDetailsActivity.this, intent);
                }

            }

        });
        zanGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (zanAdapter.getCount() - 1 == position) {
                    if (zanAdapter.getCount() == 14) {
                        likesMore.add(null);
                        zanAdapter = new SunAwardDetailsZanImageAdapter(
                                SunAwardDetailsActivity.this, likesMore);
                        zanGridView.setAdapter(zanAdapter);
                    } else if (zanAdapter.getCount() > 14) {
                        zanAdapter = new SunAwardDetailsZanImageAdapter(
                                SunAwardDetailsActivity.this, likes);
                        zanGridView.setAdapter(zanAdapter);
                    }

                }
            }
        });

        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(SunAwardDetailsActivity.this, SmoothImageActivity.class);
                Bundle bundleObject = new Bundle();
                bundleObject.putStringArrayList("t",
                        (ArrayList<String>) sunAwardDetailsTalkModel.getTalk().getImageUrls());
                bundleObject.putInt("ID", position);
                intent.putExtras(bundleObject);
                startActivity(intent);
            }
        });
    }

    private void setData() {

        if (progress == null) {
            progress = new LoadingDialog(this);
        }
        progress.show();
        CommentModel.getComment(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                commentModel = arguments.get(0);
                for (int i = 0; i < commentModel.getComments().size(); i++) {
                    contentLilst.add(commentModel.getComments().get(i).getContent());
                    contentAdapter = new SunAwardDetailsListAdapter(SunAwardDetailsActivity.this,
                            commentModel);
                    sunDetailsListView.setAdapter(contentAdapter);
                }
            }
        });
        SunAwardDetailsTalkModel.talks(id).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                sunAwardDetailsTalkModel = arguments.get(0);
                likes.clear();
                likesMore.clear();
                xiaoxiNumber = sunAwardDetailsTalkModel.getTalk().getCommentNumber();
                if (sunAwardDetailsTalkModel.getTalk().getLocation() != null) {
                    addressLinear.setVisibility(View.VISIBLE);
                    addressTextView.setText(sunAwardDetailsTalkModel.getTalk().getLocation());
                } else {
                    addressLinear.setVisibility(View.GONE);
                }
                nameTextView.setText(sunAwardDetailsTalkModel.getTalk().getUser().getNickname());
                ImageViewAdapter.adapt(headImageView,
                        sunAwardDetailsTalkModel.getTalk().getUser().getImageUrl(),
                        R.drawable.ic_default_avatar, true);
                if (sunAwardDetailsTalkModel.getTalk().getImageUrls() != null) {
                    if (sunAwardDetailsTalkModel.getTalk().getImageUrls().size() <= 4) {
                        imageGridView.setNumColumns(2);
                        imageGridView.setLayoutParams(layoutParams);
                    } else {
                        imageGridView.setNumColumns(3);
                        imageGridView.setLayoutParams(layoutParams2);
                    }
                    adapter = new SunAwardDetailsImageAdapter(SunAwardDetailsActivity.this,
                            sunAwardDetailsTalkModel.getTalk().getImageUrls());
                    imageGridView.setAdapter(adapter);
                }

                if (sunAwardDetailsTalkModel.getTalk().getIsLike()) {
                    xiaoxiImage.setBackgroundResource(R.drawable.zan);
                } else {
                    xiaoxiImage.setBackgroundResource(R.drawable.zan);
                }
                if (sunAwardDetailsTalkModel.getTalk().getLikes().size() == 0) {
                    likelayout.setVisibility(View.GONE);
                    viewline.setVisibility(View.GONE);
                } else {
                    likelayout.setVisibility(View.VISIBLE);
                    viewline.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < sunAwardDetailsTalkModel.getTalk().getLikes().size(); i++) {
                    likesMore.add(sunAwardDetailsTalkModel.getTalk().getLikes().get(i));
                    if (i < 14) {
                        likes.add(sunAwardDetailsTalkModel.getTalk().getLikes().get(i));
                    }
                }
                zanAdapter = new SunAwardDetailsZanImageAdapter(SunAwardDetailsActivity.this,
                        likes);
                zanGridView.setAdapter(zanAdapter);
                contentTextView.setText(sunAwardDetailsTalkModel.getTalk().getContent());
                timeTextView.setText(sunAwardDetailsTalkModel.getTalk().getHumanDate());
                deviceNameTextView
                        .setText("来自:" + sunAwardDetailsTalkModel.getTalk().getDeviceName());
                ifZan = sunAwardDetailsTalkModel.getTalk().getIsLike();
                zanNumber = sunAwardDetailsTalkModel.getTalk().getLikeNumber();
                if (sunAwardDetailsTalkModel.getTalk().getIsLike()) {
                    zanImageView.setBackgroundResource(R.drawable.zan_is);
                } else {
                    zanImageView.setBackgroundResource(R.drawable.zan);
                }
                progress.dismiss();
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
                DialogUtil.showMessage("网络异常");
            }
        });

    }

    private void initView() {
        nameTextView = (TextView) findViewById(R.id.sunaward_fragment_name);
        headImageView = (RoundCornerImageView) findViewById(R.id.sunaward_details_image);
        imageGridView = (GridViewForScrollView) findViewById(R.id.sunaward_details_gridview);
        zanGridView = (GridViewForScrollView) findViewById(R.id.sunaward_details_zan_gridview);
        sunDetailsListView = (ScrollViewWithListView) findViewById(R.id.sunaward_details_list);
        contentEdit = (EditText) findViewById(R.id.sunaward_details_edit_content);
        sendButton = (Button) findViewById(R.id.sunaward_details_button_send);
        rela = (ScrollView) findViewById(R.id.scroll);
        contentTextView = (TextView) findViewById(R.id.sunaward_details_content);
        timeTextView = (TextView) findViewById(R.id.sunaward_fragment_time);
        deviceNameTextView = (TextView) findViewById(R.id.sunaward_fragment_from);
        topView = (TopView) findViewById(R.id.top_placelist_layout);
        informationImage = (LinearLayout) findViewById(R.id.sunaward_fragment_image_information);
        zanImage = (LinearLayout) findViewById(R.id.sunaward_fragment_image_zan);
        sendMessageLinear = (LinearLayout) findViewById(R.id.send_message);
        messageLinear = (LinearLayout) findViewById(R.id.message);
        zanImageView = (ImageView) findViewById(R.id.zan_image);
        zanNumberTextView = (TextView) findViewById(R.id.sunaward_fragment_zan);
        xiaoxiImage = (ImageView) findViewById(R.id.xiaoxi);
        addressLinear = (LinearLayout) findViewById(R.id.sunaward_details_address_linear);
        addressTextView = (TextView) findViewById(R.id.sunaward_details_address);
        xiaoxiTextView = (TextView) findViewById(R.id.sunaward_fragment_information);
        viewline=findViewById(R.id.view_line);
        likelayout=(RelativeLayout)findViewById(R.id.like_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("晒单详情");
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        }
        position = getIntent().getIntExtra("position",0);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(rela.getWindowToken(), 0);
                    rela.requestFocus();

                    /*sendMessageLinear.setVisibility(View.GONE);
                    messageLinear.setVisibility(View.VISIBLE);*/

                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {
                    0, 0
            };
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back_img:


                Intent intent = new Intent(SunAwardFragment.ACTION_NAME);
                intent.putExtra("tag",0);
                intent.putExtra("ifzan",ifZan);
                intent.putExtra("xiaoxi",xiaoxiNumber);
                intent.putExtra("position",position);
                intent.putExtra("zanNumber",zanNumber);
                sendBroadcast(intent);
                progress.dismiss();
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SunAwardFragment.ACTION_NAME);
        intent.putExtra("tag",0);
        intent.putExtra("ifzan",ifZan);
        intent.putExtra("xiaoxi",xiaoxiNumber);
        intent.putExtra("position",position);
        intent.putExtra("zanNumber",zanNumber);
        sendBroadcast(intent);
        progress.dismiss();
        finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("晒详情界面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("晒详情界面");
        MobclickAgent.onPause(this);
    }
}
