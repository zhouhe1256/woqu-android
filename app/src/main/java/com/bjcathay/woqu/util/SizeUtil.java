
package com.bjcathay.woqu.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class SizeUtil {

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context
                .getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float pixelToDp(Context context, float val) {
        float density = context.getResources().getDisplayMetrics().density;
        return val * density;
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context
                .getResources().getDisplayMetrics());
    }
    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
