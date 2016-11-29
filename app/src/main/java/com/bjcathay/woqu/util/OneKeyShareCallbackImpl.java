
package com.bjcathay.woqu.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.WishDetailsActivity;
import com.bjcathay.woqu.constant.ApiUrl;
import com.bjcathay.woqu.view.DeleteInfoDialog;
import com.mob.tools.utils.UIHandler;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;

import static com.mob.tools.utils.R.getStringRes;

/**
 * Created by dengt on 15-10-21.
 */
public class OneKeyShareCallbackImpl implements PlatformActionListener, Handler.Callback {
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private Context context;
    private String id;
    private String type;
    private String giftId;
    public OneKeyShareCallbackImpl(Context context,String id,String type,String giftId) {
        this.context = context;
        this.id = id;
        this.type = type;
        this.giftId = giftId;
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        String resjson = JSONUtil.dump(hashMap);
        Logger.e("resjson1", resjson);
        PlatformDb platDB = platform.getDb();
        String platName = platform.getName();
        String platuserid = platDB.getUserId();
        String platuserName = platDB.getUserName();
        String platjson = platDB.exportData();
        String plattoken = platDB.getToken();
        Logger.e("resjson2", "platName:"+platName+"platuserid:"+platuserid+
        "platuserName"+platuserName+"platjson:"+platjson+"plattoken"+plattoken);
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
                break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: {
                        // 成功
                        Platform platform = (Platform) msg.obj;
                        PlatformDb platDB = platform.getDb();
                        String platjson = platDB.exportData();
                        int resId = getStringRes(context, "share_completed");
                        if (resId > 0) {
                            showNotification(context.getString(resId));
                        }
                        DeleteInfoDialog infoDialog = new DeleteInfoDialog(context, R.style.InfoDialog,
                                "分享成功", 0l, new DeleteInfoDialog.DeleteInfoDialogResult() {
                            @Override
                            public void deleteResult(Long targetId, boolean isDelete) {

                            }
                        });
                        infoDialog.f = true;
                        infoDialog.show();
                        if(type.equals("woqu")){
                            Http.instance().post(ApiUrl.getShare(id)).param("data",platjson).
                                    run().
                                    done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            JSONObject js = arguments.get(0);
                                            Logger.i("shares", js.toString());
                                        }
                                    });
                        }else if(type.equals("wish")){
                            Http.instance().post(ApiUrl.getJingXuanShare(id)).param("giftId",giftId).
                                    param("data", platjson).
                                    run().
                                    done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            JSONObject js = arguments.get(0);
                                            Logger.i("shares", js.toString());
                                            WishDetailsActivity.fk = true;
                                            Intent intent = new Intent(WishDetailsActivity.ACTION_NAME);
                                            context.sendBroadcast(intent);
                                        }
                                    });
                        }


                    }
                        break;
                    case 2: {
                        // 失败
                        String expName = msg.obj.getClass().getSimpleName();
                        if ("WechatClientNotExistException".equals(expName)
                                || "WechatTimelineNotSupportedException".equals(expName)
                                || "WechatFavoriteNotSupportedException".equals(expName)) {
                            int resId = getStringRes(context, "wechat_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("GooglePlusClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "google_plus_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("QQClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "qq_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("YixinClientNotExistException".equals(expName)
                                || "YixinTimelineNotSupportedException".equals(expName)) {
                            int resId = getStringRes(context, "yixin_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("KakaoTalkClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "kakaotalk_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("KakaoStoryClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "kakaostory_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("WhatsAppClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "whatsapp_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else {
                            int resId = getStringRes(context, "share_failed");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        }
                    }
                        break;
                    case 3: {
                        // 取消
                        int resId = getStringRes(context, "share_canceled");
                        if (resId > 0) {
                            showNotification(context.getString(resId));
                        }
                    }
                        break;
                }
            }
                break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
                break;
        }
        return false;
    }

    // 在状态栏提示分享操作
    private void showNotification(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
