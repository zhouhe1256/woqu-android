
package com.bjcathay.woqu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-11-17.
 */
public class ExpressComposeView extends LinearLayout {
    private Context context;

    public ExpressComposeView(Context context) {
        super(context);
        this.context = context;
        setOrientation(VERTICAL);
        initView(context);
    }

    public ExpressComposeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);
        initView(context);
    }

    public ExpressComposeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(VERTICAL);
        initView(context);
    }

    public void setList(List<DataModel> list) {
        this.list = list;
        initView(context);
    }

    List<DataModel> list = new ArrayList<DataModel>();

    private void initView(Context context) {
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                View view;
                if (i == 0) {
                    view = LayoutInflater.from(context).inflate(R.layout.express_head, null);
                } else if (i == list.size() - 1) {
                    view = LayoutInflater.from(context).inflate(R.layout.express_bottom, null);
                } else {
                    view = LayoutInflater.from(context).inflate(R.layout.express_middle, null);
                }
                TextView titleText = (TextView) view.findViewById(R.id.name);
                TextView timeText = (TextView)view.findViewById(R.id.time);
                timeText.setText(list.get(i).getTime());
                titleText.setText(list.get(i).getContext());
                this.addView(view);
            }
        }

    }
}
