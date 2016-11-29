package com.bjcathay.woqu.application;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.async.LooperCallbackExecutor;
import com.bjcathay.android.cache.ChainedCache;
import com.bjcathay.android.cache.DiskCache;
import com.bjcathay.android.cache.MemoryCache;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.HttpOption;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.constant.ApiUrl;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.util.PreferencesConstant;
import com.bjcathay.woqu.util.PreferencesUtils;
import com.igexin.sdk.PushManager;

import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by jiangm on 15-10-8.
 */
public class WApplication  extends BaiduMapApplication {
    private static WApplication wApplication;
    private static volatile boolean httpInited;
    private static File baseDir;
    private Context Wcontext;
    private boolean flag = true;
    public static boolean send = false;
    public static boolean send_ = false;
    public  boolean refresh = false;
    private UserModel user;
    private boolean agreeflag=false;
    private PointPraiseModel pointPraiseMode;
    public boolean pointPraiseflag = false;
    private ArrayList<Activity> activityList = new ArrayList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        wApplication=this;
        initHttp(this);
        Logger.setDebug(true);
    }


    public UserModel getUser() {
        if (user == null) {
            String userJson = PreferencesUtils.getString(this, PreferencesConstant.USER_INFO, "");
//            if (StringUtils.isEmpty(userJson)) {
//                return null;
//            }
            user = JSONUtil.load(UserModel.class, userJson);
        }
        return user;
    }

    public boolean isAgreeflag() {
        return agreeflag;
    }

    public void setAgreeflag(boolean agreeflag) {
        this.agreeflag = agreeflag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setUser(UserModel user) {
        this.user = user;
        PreferencesUtils.putString(this, PreferencesConstant.USER_INFO, JSONUtil.dump(user));
    }

    public boolean isLogin() {
        if (getApiToken() == null || getApiToken() == "")
            return false;
        else if (getApiToken().length() > 1)
            return true;
        else
            return false;
    }

    public static WApplication getInstance() {
        PushManager.getInstance().initialize(wApplication.getApplicationContext());
      //  Logger.e("pushid",PushManager.getInstance().getClientid(wApplication.getApplicationContext()));
        return wApplication;
    }
    public void updateApiToken() {
        String token = PreferencesUtils.getString(wApplication, PreferencesConstant.API_TOKEN);
        Http.instance().param("t", token).option(HttpOption.X_Token, token);
    }
    public synchronized void initHttp(Context context) {
        if (httpInited) {
            return;
        }
        this.Wcontext = context;
        // mView = LayoutInflater.from(context).inflate(
        // R.layout.layout_my_dialog, null);
//        mView = new NetAccessView(context);
//        if (dialogReceiver == null) {
//            dialogReceiver = new DialogReceiver();
//        }

        httpInited = true;

        baseDir = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory() : context.getCacheDir();
        baseDir = new File(baseDir, "woqu");
        DiskCache.setBaseDir(baseDir);


        //String token = "a9363cea86a08ef5d68f8fda8b8072469e374bc6";

        String token = getApiToken();
        DiskCache<String, byte[]> apiCache = new DiskCache<String, byte[]>("api", new DiskCache.ByteArraySerialization());
        Http.instance().option(HttpOption.BASE_URL, ApiUrl.HOST_URL).
                option(HttpOption.MIME, "application/json").
                param("t", token).param("v", ApiUrl.VERSION).
                param("os", ApiUrl.OS).
                option(HttpOption.CONNECT_TIMEOUT, 10000).
                option(HttpOption.READ_TIMEOUT, 10000).
                option(HttpOption.X_Token, token).
                option(HttpOption.X_Version, ApiUrl.VERSION).
                option(HttpOption.X_OS, ApiUrl.OS).
                setContentDecoder(new IContentDecoder.JSONDecoder()).
                cache(apiCache).fallbackToCache(true).
                always(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        Object object = arguments.get(0);

                        if (object instanceof Throwable) {
                            Throwable t = (Throwable) object;
                            StringWriter writer = new StringWriter();
                            writer.write(t.getMessage() + "\n");
                            t.printStackTrace(new PrintWriter(writer));
                            String s = writer.toString();
                            System.out.println(s);
                            return;
                        }

                        if (!(object instanceof JSONObject)) {
                            return;
                        }
                        JSONObject json = arguments.get(0);
                        if (!json.optBoolean("success")) {
                            String errorMessage = json.optString("message");
                            int code = json.optInt("code");
                            if (code == 13005) {
                            } else {
                            }
                        }
                    }
                }).start(new ICallback() {
            @Override
            public void call(Arguments arguments) {
//                if (flag == false) {
//                    flag = true;
//                    IntentFilter homeFilter = new IntentFilter(
//                            Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//                    mView.getContext().registerReceiver(dialogReceiver, homeFilter);
//                    if (mView.getParent() == null)
//                        wm.addView(mView, para);
//                }
            }
        }).complete(new ICallback() {
            @Override
            public void call(Arguments arguments) {
//                if (mView.getParent() != null) {
//                    flag = false;
//                    if (dialogReceiver != null) {
//                        mView.getContext().unregisterReceiver(dialogReceiver);
//                    }
//                    wm.removeView(mView);
//                }
            }
        }).
                callbackExecutor(new LooperCallbackExecutor()).
                fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                    }
                });
        ChainedCache<String, byte[]> imageCache = ChainedCache.create(
                800 * 1024 * 1024, new MemoryCache.ByteArraySizer<String>(),
                "images", new DiskCache.ByteArraySerialization<String>()
        );
        Http.imageInstance().cache(imageCache).baseUrl(ApiUrl.HOST_URL).
                aheadReadInCache(true);
    }
    public static File getBaseDir() {
        return baseDir;
    }
    public void startService(String content,String address,ArrayList<String> list,String imageId){
        Intent i = new Intent("com.bjcathay.woqu");
        i.putExtra("content",content);
        i.putExtra("address",address);
        i.putExtra("paths", list);
        i.putExtra("imageId", imageId);
        startService(i);
    }
    public void stopService(){
        Intent i = new Intent("com.bjcathay.woqu");
        stopService(i);
    }
    public String getApiToken() {
        return PreferencesUtils.getString(wApplication, PreferencesConstant.API_TOKEN, "");
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void setPointPraiseMode(PointPraiseModel pointPraiseMode){
        if (pointPraiseflag){
            this.pointPraiseMode = pointPraiseMode;
        }

    }
    public  PointPraiseModel getpointPraiseMode(){
        return pointPraiseMode;
    }
    public void clearPointPraiseModel(){
        pointPraiseMode = null;
    }
    public void exit(){
        for(Activity activity:activityList ){
            activity.finish();
        }
        System. exit(0);
    }
}
