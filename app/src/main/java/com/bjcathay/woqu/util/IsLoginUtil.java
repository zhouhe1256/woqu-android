
package com.bjcathay.woqu.util;

import android.content.Context;
import android.content.Intent;


import com.bjcathay.woqu.activity.LoginActivity;
import com.bjcathay.woqu.application.WApplication;

/**
 * Created by jiangm on 15-10-12.
 */
public class IsLoginUtil {
    public static void isLogin(Context context, Intent intent) {
        if (WApplication.getInstance().isLogin()) {
            ViewUtil.startActivity(context, intent);

        } else {
            intent = new Intent(context, LoginActivity.class);
            ViewUtil.startActivity(context, intent);
        }
    }
}
