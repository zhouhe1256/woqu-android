
package com.bjcathay.woqu.receiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.activity.LoginActivity;
import com.bjcathay.woqu.activity.MyNewsActivity;
import com.bjcathay.woqu.activity.PointPraiseActivity;
import com.bjcathay.woqu.activity.PointPraiseEndActivity;
import com.bjcathay.woqu.activity.ShoppingActivity;
import com.bjcathay.woqu.activity.WelcomeActivity;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.model.PointPraiseModel;
import com.bjcathay.woqu.model.PushModel;
import com.bjcathay.woqu.model.UserModel;
import com.bjcathay.woqu.view.PushInfoDialog;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import java.util.List;

/**
 * Created by dengt on 15-5-22.
 */
public class MessageReceiver extends BroadcastReceiver implements PushInfoDialog.PushResult {

    private static int ids = 0;
    public static Activity baseActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid,
                        messageid, 90001);
                if (payload != null) {
                    String data = new String(payload);
                   // Logger.d("GetuiSdkDemo", "Got Payload:" + data);// {"type":"ORDER","target":33}
                    // DialogUtil.showMessage(data);
                    Log.i("GetuiSdkDemo","Got Payload:" + data);
                    try {
                        PushModel pushModel = JSONUtil.load(PushModel.class, data);
                        if (pushModel != null) {
                            handlePush(context, pushModel);
                        }
                    } catch (IllegalArgumentException e) {
                    }

                }
                break;
            case PushConsts.GET_CLIENTID:
//                UserModel.updateUser("pushClientId", PushManager.getInstance().getClientid(context)).done(new ICallback() {
//                    @Override
//                    public void call(Arguments arguments) {
//
//                    }
//                });
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;
            default:
                break;
        }
    }

    private void handlePush(final Context context, final PushModel pushModel) {

        Intent intent;
        if (isRunning(context)) {
            if (!isRunningForeground(context)) {
                if (baseActivity != null)
                    if ("REMIND".equals(pushModel.getT()))
                        PushInfoDialog.getInstance(baseActivity,
                                R.style.InfoDialog, pushModel.getM(), "确认", pushModel, this).show();
                    else {

                    }
            } else {
                // REMIND|SYSTEM|LUCK|AD|ACTIVITY
                // 消息分为五种类型，提醒，系统，获奖，广告，活动
                // 提醒:应用内弹窗提醒，应用外通知栏提醒。
                // 其他:均以通知栏提醒。
                if ("REMIND".equals(pushModel.getT())) {
                    PointPraiseModel.getActivitys(pushModel.getId()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            Intent intent = null;
                            PointPraiseModel pointPraiseMode = arguments.get(0);
                            int status = pointPraiseMode.getActivity().getStatus();
                            if (status == 0) {
                                intent = new Intent(context, PointPraiseActivity.class);

                            } else if (status == 1) {
                                intent = new Intent(context, PointPraiseActivity.class);
                            } else {
                                intent = new Intent(context, PointPraiseEndActivity.class);
                            }
                            intent.putExtra("id", pushModel.getId());
                            sendNotice(context, intent, pushModel);
                        }
                    });
                } else if ("SYSTEM_MESSAGE".equals(pushModel.getT())) {
                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setClass(context, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);// 关键的一步，设置启动模式
                    sendNotice(context, intent, pushModel);
                } else if ("LUCK".equals(pushModel.getT())) {
                    if (WApplication.getInstance().isLogin()) {
                        intent = new Intent(context, MyNewsActivity.class);
                        intent.putExtra("id", Long.parseLong(pushModel.getId()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }
                    sendNotice(context, intent, pushModel);
                } else if ("AD".equals(pushModel.getT())) {
                    intent = new Intent(context, ShoppingActivity.class);
                    intent.putExtra("url", String.valueOf(pushModel.getId()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    sendNotice(context, intent, pushModel);
                } else if ("ACTIVITY".equals(pushModel.getT())) {
                    PointPraiseModel.getActivitys(pushModel.getId()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            Intent intent = null;
                            PointPraiseModel pointPraiseMode = arguments.get(0);
                            int status = pointPraiseMode.getActivity().getStatus();
                            if (status == 0) {
                                intent = new Intent(context, PointPraiseActivity.class);

                            } else if (status == 1) {
                                intent = new Intent(context, PointPraiseActivity.class);
                            } else {
                                intent = new Intent(context, PointPraiseEndActivity.class);
                            }
                            intent.putExtra("id", pushModel.getId());
                            sendNotice(context, intent, pushModel);
                        }
                    });
                }
            }
        } else {
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("push", pushModel);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClass(context, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent).setTicker(pushModel.getM()).
                    setContentTitle("喔去").
                    setContentText(pushModel.getM()).
                    setSmallIcon(R.drawable.ic_launcher).
                    setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND);
            Notification notification;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                notification = new Notification();
                notification.contentIntent = contentIntent;
                notification.tickerText = pushModel.getM();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.defaults = Notification.DEFAULT_SOUND;
                notification.icon = R.drawable.ic_launcher;

            } else
                notification = builder.
                        build();
            mNotificationManager.notify(ids++, notification);
        }

    }

    public void sendNotice(Context context, Intent intent, PushModel pushModel) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        // intent = new Intent(Intent.ACTION_MAIN);
        // intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // intent.setClass(context, WelcomeActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
        // Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        // | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);// 关键的一步，设置启动模式
        PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent).setTicker(pushModel.getM()).
                setContentTitle("喔去").
                setContentText(pushModel.getM()).
                setSmallIcon(R.drawable.ic_launcher).
                setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);
        Notification notification;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification();
            notification.contentIntent = contentIntent;
            notification.tickerText = pushModel.getM();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND;
            notification.icon = R.drawable.ic_launcher;
        } else
            notification = builder.
                    build();
        mNotificationManager.notify(ids++, notification);
    }

    public boolean isRunningForeground(Context context) {
        String packageName = getPackageName(context);
        String topActivityClassName = getTopActivityName(context);
        System.out.println("packageName=" + packageName + ",topActivityClassName="
                + topActivityClassName);
        if (packageName != null && topActivityClassName != null
                && topActivityClassName.startsWith(packageName)) {
            System.out.println("---> isRunningForeGround");
            return true;
        } else {
            System.out.println("---> isRunningBackGround");
            return false;
        }
    }

    private boolean isRunning(Context context) {
        // 判断应用是否在运行
        ActivityManager am = (ActivityManager) WApplication.getInstance().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = getPackageName(context);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
                    || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                Logger.i("isRunning",
                        info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="
                                + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }

    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context
                        .getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    public String getPackageName(Context context) {
        String packageName = context.getPackageName();
        return packageName;
    }

    @Override
    public void pushResult(PushModel pushModel, boolean isDelete, Context context) {

    }
}
