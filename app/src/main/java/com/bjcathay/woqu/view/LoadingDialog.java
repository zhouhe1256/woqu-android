
package com.bjcathay.woqu.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.bjcathay.woqu.R;

/**
 * Created by dengt on 15-10-26.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.loadDialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, R.style.loadDialog);
    }

    protected LoadingDialog(Context context, boolean cancelable,
                            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.progress_dialog);
        setCanceledOnTouchOutside(true);
    }
}
