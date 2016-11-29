
package com.bjcathay.woqu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.WindowManager;

import com.bjcathay.woqu.application.WApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SystemUtil {
    /**
     * 获取操作系统版本
     */
    public static String getVersion() {
        String[] version = {
                "null", "null", "null", "null"
        };
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];// KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2] = Build.MODEL;// model
        version[3] = Build.DISPLAY;// system version
        return "Android" + version[1];
    }

    /**
     * 获取当前版本号
     *
     * @param mContext
     * @return
     */
    public static String getCurrentVersionName(Context mContext) {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }
public static int getScriptWidth(){
    WindowManager wm = (WindowManager) WApplication.getInstance()
            .getSystemService(Context.WINDOW_SERVICE);

    return  wm.getDefaultDisplay().getWidth();
}
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }
  /*  public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }*/

}
