package com.bjcathay.woqu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bjcathay.woqu.R;

/**
 * Created by zhouh on 15-11-3.
 */
public class TimeTextView extends TextView implements Runnable{

    Paint mPaint; //画笔,包含了画几何图形、文本等的样式和颜色信息

    private long[] times;

    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒

    private boolean run=false; //是否启动了

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle(); //一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint=new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);

        array.recycle(); //一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public TimeTextView(Context context) {
        super(context);
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];

    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 59;
                    mday--;

                }
            }

        }

    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        //标示已经启动
        run=true;

        ComputeTime();
        String strTime="";
        if(mday>0){
            strTime = mday+"天"+mhour+"小时";
        }else{
            String m;
            if(mmin<10){
                 m = "0"+mmin;
            }else{
                 m = mmin+"";
            }
            String s;
            if(msecond<10){
                s = "0"+msecond;
            }else{
                s = msecond+"";
            }
            strTime = mhour+":"+m+":"+s;

        }

        this.setText(Html.fromHtml(strTime));

        postDelayed(this, 1000);

    }

}