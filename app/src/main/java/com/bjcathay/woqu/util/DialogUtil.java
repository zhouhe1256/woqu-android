
package com.bjcathay.woqu.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;

/**
 * Created by dengt on 15-4-21.
 */
public class DialogUtil {
    private static Toast toast;
    public static int gravity = Gravity.CENTER;

    public static void showMessage(String message) {
        if (toast == null) {
            toast = Toast.makeText(WApplication.getInstance(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.setGravity(gravity, 0, 0);
        toast.show();

    }

    public static void showDialog(Activity context, int resource) {
        LayoutInflater inflater = context.getLayoutInflater();
        ViewGroup rootView = (ViewGroup) inflater.inflate(resource, null);
        Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        dialog.setContentView(rootView);
        // dialog.create();
        // dialog.setContentView(rootView);
        dialog.show();
    }
}
