
package com.bjcathay.woqu.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dengt on 15-10-19.
 */
public class PriceTextView extends TextView {

    public PriceTextView(Context context) {
        super(context);
    }

    public PriceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PriceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
        text=text+"0";
        int position = text.indexOf(".");
        int end = text.length();
        Spannable WordtoSpan = new SpannableString(text);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(21), 0, position,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(18), position, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(WordtoSpan);
    }
}
