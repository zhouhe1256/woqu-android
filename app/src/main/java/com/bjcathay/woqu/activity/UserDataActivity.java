
package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;

import com.bjcathay.woqu.util.FileUtils;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.SelectDatePopupWindow;
import com.bjcathay.woqu.view.SelectPicPopupWindow;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jiangm on 15-9-24.
 */
public class UserDataActivity extends Activity
        implements View.OnClickListener, SelectPicPopupWindow.SelectResult,SelectDatePopupWindow.SelectResult  {
    TopView topView;
    SelectPicPopupWindow menuWindow;
    SelectDatePopupWindow dateWindow;
    private ImageView imageInfo;
    private int selectCode = 1;
    private int requestCropIcon = 2;
    private int resultPictureCode = 3;
    private UserModel userModel;
    private TextView dateText;
    //  private DatePickerDialog datePickerDialog;
    private String SEX="sex";
    private String MALE="MALE";
    private String FEMALE="FEMALE";
    private CheckBox checkBox;
    private String BIRTHDAY="birthday";
    //MALE|FEMALE
    WApplication wApplication;
    private TextView userName;
    protected LoadingDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        topView = ViewUtil.findViewById(this, R.id.top_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("个人资料");
        wApplication=WApplication.getInstance();
        WApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }

    public void initEvent(){

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeSex(checkBox.isChecked());
            }
        });
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                changeSex(isChecked);
//            }
//        });
    }
    public void initData() {
        // Intent intent = getIntent();
        // userModel = (UserModel) intent.getSerializableExtra("user");
            if (progress == null)
                progress = new LoadingDialog(this);
        progress.show();
        dateText.setText(PreferencesUtils.getString(wApplication, PreferencesConstant.USER_BIRTHDAY));
        judgeSex( PreferencesUtils.getString(wApplication, PreferencesConstant.USER_SEX)==null?MALE:PreferencesUtils.getString(wApplication, PreferencesConstant.USER_SEX));
        userName.setText(PreferencesUtils.getString(wApplication, PreferencesConstant.NICK_NAME));
        UserModel.get().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
                userModel=arguments.get(0);
                wApplication.setUser(userModel);
                ImageViewAdapter.adapt(imageInfo, userModel.getImageUrl(), R.drawable.ic_default_avatar,true);
                userName.setText(userModel.getNickname());
                dateText.setText(userModel.getBirthday());
                PreferencesUtils.putString(wApplication, PreferencesConstant.USER_BIRTHDAY, userModel.getBirthday());
                PreferencesUtils.putString(wApplication, PreferencesConstant.NICK_NAME, userModel.getNickname());
                PreferencesUtils.putString(wApplication, PreferencesConstant.USER_SEX, userModel.getSex());
                if(!userModel.getSex().isEmpty()&&userModel.getSex()!=null) {

                    judgeSex(userModel.getSex());
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
            }
        });



    }

    public void initView() {
        imageInfo = ViewUtil.findViewById(this, R.id.image_info);
        dateText = ViewUtil.findViewById(this, R.id.birthday_text);
        checkBox=ViewUtil.findViewById(this,R.id.set_switch);
        userName=ViewUtil.findViewById(this,R.id.user_name);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.photo_layout:
                menuWindow = new SelectPicPopupWindow(UserDataActivity.this, this);
                menuWindow.showAtLocation(UserDataActivity.this.findViewById(R.id.image_info),
                        Gravity.BOTTOM
                                | Gravity.CENTER_HORIZONTAL,
                        0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.select_date:
                //show();
                // datePickerDialog.show();
                dateWindow=new SelectDatePopupWindow(UserDataActivity.this,dateText, this);
                dateWindow.showAtLocation(UserDataActivity.this.findViewById(R.id.image_info),
                        Gravity.BOTTOM
                                | Gravity.CENTER_HORIZONTAL,
                        0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.nick_name:
                Intent intent= new Intent(UserDataActivity.this,EditUserNnameActivity.class);
                ViewUtil.startActivity(UserDataActivity.this,intent);
                break;
            case R.id.title_back_img:
                finish();
                break;

        }
    }
    String query;
    @Override
    public void resultData() {


        UserModel.updateUser(BIRTHDAY,dateText.getText().toString()).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                try {
                    if (jsonObject.getBoolean("success") == true) {
                        DialogUtil.showMessage("生日修改成功");
                        //finish();
                    } else {
                        DialogUtil.showMessage(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });

    }

    @Override
    public void resultPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, selectCode);
    }

    @Override
    public void resultCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, resultPictureCode);
    }

    Uri uri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (selectCode == requestCode) {
            uri = data.getData();
            Intent intent = new Intent("com.android.camera.action.CROP");

            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 60);
            intent.putExtra("outputY", 60);
            intent.putExtra("scale", true);//黑边
            intent.putExtra("scaleUpIfNeeded", true);//黑边
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            File file=new File(WApplication.getBaseDir()+"/camera.jpg");
            uri=Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);// 设置为不返回数据
            startActivityForResult(intent, requestCropIcon);
        } else if (requestCropIcon == requestCode) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (uri == null) {
                    return;
                }
                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                    imageInfo.setImageBitmap(photo);
                    UserModel.setAvatar(FileUtils.Bitmap2Bytes(photo)).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            UserModel user = arguments.get(0);
                            // initUserData(user, false);
                        }
                    }).fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            DialogUtil.showMessage(getString(R.string.empty_net_text));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (resultPictureCode == requestCode) {
            Bundle extras = data.getExtras();
            data.getData();

            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                imageInfo.setImageBitmap(photo);
                UserModel.setAvatar(FileUtils.Bitmap2Bytes(photo)).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        UserModel user = arguments.get(0);
                        // initUserData(user, false);

                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                    }
                });
            }
        }
    }


    String sex;
    public void  changeSex(boolean flag){


        if(flag){
            sex=MALE;
        }else{
            sex=FEMALE;
        }
        UserModel.updateUser(SEX,sex).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                Logger.e("jsonObject",jsonObject.toString());
                try {
                    if (jsonObject.getBoolean("success") == true) {
                        DialogUtil.showMessage("性别修改成功");

                    } else {
                        DialogUtil.showMessage("性别修改失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));

            }
        });
    }
    public void judgeSex(String sex){
        if (sex.equals(MALE)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        userName.setText(PreferencesUtils.getString(wApplication,PreferencesConstant.NICK_NAME));
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("用户信息界面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("用户信息界面");
        MobclickAgent.onPause(this);
    }
}
