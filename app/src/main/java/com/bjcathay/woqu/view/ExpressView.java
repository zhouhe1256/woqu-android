
package com.bjcathay.woqu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-10-23. <com....ExpressView
 * android:layout_marginTop="10dp" android:layout_width="match_parent"
 * android:layout_height="wrap_content" /> 暂不需要其他属性,目前属性已写死,待扩展 代码设置数组
 * view..setList(list);
 */
public class ExpressView extends View {

    private Paint mPaint;
    /**
     * 第一个节点的外半径
     */
    private float timelineHeadRadius;
    /**
     * 第一个节点的颜色值
     */
    private int timelineHeadColor;
    /**
     * 第二个节点的颜色值
     */
    private int timelineOtherColor;
    private int leftCircleOtherColor;
    /**
     * 时间线的节点数
     */
    private int timelineCount;
    /**
     * 时间轴的位置
     */
    private int viewWidth;
    /**
     * 时间轴到距离顶部的距离
     */
    private int marginTop;
    /**
     * 时间轴的节点的半径
     */
    private int timelineRadius;
    /**
     * 时间轴节点之间的距离
     */
    private int timelineRadiusDistance;
    /**
     * 时间轴的宽度
     */
    private int timelineWidth;
    /**
     * 时间轴的高度
     */
    private float timeLineViewHeight;
    private ExpressView expressView;

    public ExpressView(Context context) {
        this(context, null);
    }

    public ExpressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        init(context, attrs, defStyle);
    }

    public List<DataModel> getList() {
        return list;
    }

    public void setList(List<DataModel> list) {
        this.list = list;
        invalidate();
        forceLayout();
        requestLayout();
    }

    ViewTreeObserver vto = getViewTreeObserver();

    List<DataModel> list = new ArrayList<DataModel>();
    private Context context;

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
        expressView = this;
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TimeLineView, defStyle, 0);
        timelineRadiusDistance = (int) a.getDimension(
                R.styleable.TimeLineView_timelineRadiusDistance, convertDIP2PX(context, 20));
        timelineHeadRadius = a.getDimension(R.styleable.TimeLineView_timelineHeadRadius,
                convertDIP2PX(context, 5));
        timelineRadius = (int) a.getDimension(R.styleable.TimeLineView_timelineRadius,
                convertDIP2PX(context, 3));
        timelineHeadColor = a.getColor(R.styleable.TimeLineView_timelineHeadColor,
                Color.parseColor("#fc4a5b"));
        timelineOtherColor = a.getColor(R.styleable.TimeLineView_timelineOtherColor,
                Color.parseColor("#969696"));
        leftCircleOtherColor = a.getColor(R.styleable.TimeLineView_leftCircleOtherColor,
                Color.parseColor("#c8c8c8"));
        timelineCount = a.getInteger(R.styleable.TimeLineView_timelineCount, 0);
        timelineWidth = (int) a.getDimension(R.styleable.TimeLineView_timelineWidth,
                convertDIP2PX(context, 1));
        marginTop = (int) a.getDimension(R.styleable.TimeLineView_timelineMarginTop,
                convertDIP2PX(context, 20));
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setColor(Color.parseColor("#fc4a5b"));
        textPaint.setTextSize(convertDIP2PX(context, 13));
        textPaint.setAntiAlias(true);
        topmaginH = convertDIP2PX(context, 10);
        midmaginH = convertDIP2PX(context, 5);
        botmaginH = convertDIP2PX(context, 5);
        tmH = convertDIP2PX(context, 30);
        leftW = convertDIP2PX(context, 10);
        midW = convertDIP2PX(context, 20);
        tmW = convertDIP2PX(context, 31);
        viewWidth = convertDIP2PX(context, 20);
        mPaint.setColor(timelineHeadColor);
    }

    TextPaint textPaint;
    int totalHight;
    int currentHight;
    int topmaginH;
    int midmaginH;
    int botmaginH;
    int tmH;
    int leftW;
    int midW;
    int tmW;
    int rightW;
    int lay1H;
    int lay2H;
    int hh;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        if (list != null && !list.isEmpty())
            if (hh != 0) {
            } else
                for (int j = 0; j <= list.size() - 1; j++) {
                    if (j == 0) {

                        StaticLayout layout = new StaticLayout(list.get(j).getContext(),
                                textPaint, getMeasuredWidth() - tmW,
                                Layout.Alignment.ALIGN_NORMAL,
                                1.3F, 0.0F, false);
                        lay1H = layout.getHeight();
                        StaticLayout layout_ = new StaticLayout(list.get(j).getTime(),
                                textPaint, getMeasuredWidth() - tmW,
                                Layout.Alignment.ALIGN_NORMAL,
                                1.3F, 0.0F, false);
                        lay2H = layout_.getHeight();
                        height += topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                        continue;
                    }
                    if (j == list.size() - 1) {
                        // 最后一个
                        StaticLayout layout = new StaticLayout(list.get(j).getContext(),
                                textPaint, getMeasuredWidth() - tmW,
                                Layout.Alignment.ALIGN_NORMAL,
                                1.3F, 0.0F, false);
                        lay1H = layout.getHeight();
                        textPaint.setTextSize(convertDIP2PX(context, 11));
                        StaticLayout layout_ = new StaticLayout(list.get(j).getTime(),
                                textPaint, getMeasuredWidth() - tmW,
                                Layout.Alignment.ALIGN_NORMAL,
                                1.3F, 0.0F, false);
                        lay2H = layout_.getHeight();
                        mPaint.setColor(timelineOtherColor);
                        height += topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;

                        continue;
                    }
                    StaticLayout layout = new StaticLayout(list.get(j).getContext(),
                            textPaint, getMeasuredWidth() - tmW,
                            Layout.Alignment.ALIGN_NORMAL,
                            1.3F, 0.0F, false);
                    lay1H = layout.getHeight();
                    StaticLayout layout_ = new StaticLayout(list.get(j).getTime(),
                            textPaint, getMeasuredWidth() - tmW,
                            Layout.Alignment.ALIGN_NORMAL,
                            1.3F, 0.0F, false);
                    lay2H = layout_.getHeight();
                    height += topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                    height = height - 4;
                }
        if (height != 0) {
            hh = height;
        }
        setMeasuredDimension(widthSize, hh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (list != null && !list.isEmpty())
            for (int j = 0; j <= list.size() - 1; j++) {
                if (j == 0) {
                    mPaint.setColor(Color.parseColor("#88fc4a5b"));
                    canvas.drawCircle(leftW, timelineHeadRadius + topmaginH,
                            timelineHeadRadius + 2,
                            mPaint);
                    mPaint.setColor(Color.parseColor("#fc4a5b"));
                    canvas.drawCircle(leftW, timelineHeadRadius + topmaginH, timelineHeadRadius,
                            mPaint);
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.save();
                    textPaint.setTextSize(convertDIP2PX(context, 13));
                    StaticLayout layout = new StaticLayout(list.get(j).getContext(),
                            textPaint, getMeasuredWidth() - tmW,
                            Layout.Alignment.ALIGN_NORMAL,
                            1.3F, 0.0F, false);
                    canvas.translate(tmW, topmaginH);
                    layout.draw(canvas);
                    lay1H = layout.getHeight();
                    textPaint.setTextSize(convertDIP2PX(context, 11));
                    StaticLayout layout_ = new StaticLayout(list.get(j).getTime(),
                            textPaint, getMeasuredWidth() - tmW,
                            Layout.Alignment.ALIGN_NORMAL,
                            1.3F, 0.0F, false);
                    canvas.translate(0, lay1H + midmaginH);
                    layout_.draw(canvas);
                    lay2H = layout_.getHeight();
                    canvas.translate(0, lay2H + midmaginH);
                    mPaint.setColor(leftCircleOtherColor);
                    canvas.drawLine(0, 0, getMeasuredWidth() - tmW, 0,
                            mPaint);
                    totalHight += topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                    currentHight = topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                    canvas.translate(-tmW + leftW, -currentHight + topmaginH);
                    mPaint.setColor(timelineHeadColor);
                    canvas.drawLine(0, (int) (0 + 0.5 * timelineRadius), 0, currentHight
                            - topmaginH, mPaint);
                    canvas.restore();
                    mPaint.setColor(timelineOtherColor);
                    continue;
                }
                textPaint.setColor(Color.GRAY);
                if (j == list.size() - 1) {
                    // 最后一个
                    canvas.save();
                    textPaint.setTextSize(convertDIP2PX(context, 13));
                    StaticLayout layout = new StaticLayout(list.get(j).getContext(),
                            textPaint, getMeasuredWidth() - tmW,
                            Layout.Alignment.ALIGN_NORMAL,
                            1.3F, 0.0F, false);
                    canvas.translate(tmW, totalHight + topmaginH);
                    layout.draw(canvas);
                    lay1H = layout.getHeight();
                    textPaint.setTextSize(convertDIP2PX(context, 11));
                    StaticLayout layout_ = new StaticLayout(list.get(j).getTime(),
                            textPaint, getMeasuredWidth() - tmW,
                            Layout.Alignment.ALIGN_NORMAL,
                            1.3F, 0.0F, false);
                    canvas.translate(0, lay1H + midmaginH);
                    layout_.draw(canvas);
                    lay2H = layout_.getHeight();
                    canvas.translate(0, lay2H + midmaginH);
                    mPaint.setColor(timelineOtherColor);
                    totalHight += topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                    currentHight = topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                    canvas.translate(-tmW + leftW, -currentHight + topmaginH - timelineHeadRadius);
                    mPaint.setColor(timelineHeadColor);
                    canvas.drawLine(0, 0 - timelineRadius - 4, 0, currentHight / 2, mPaint);
                    mPaint.setColor(leftCircleOtherColor);
                    canvas.drawCircle((float) 0.5, currentHight / 2, timelineRadius, mPaint);
                    mPaint.setColor(timelineHeadColor);
                    canvas.restore();
                   // onMeasure(MeasureSpec.getSize(widthMeasureSpec));
                    continue;
                }
                canvas.save();
                textPaint.setTextSize(convertDIP2PX(context, 13));
                StaticLayout layout = new StaticLayout(list.get(j).getContext(),
                        textPaint, getMeasuredWidth() - tmW,
                        Layout.Alignment.ALIGN_NORMAL,
                        1.3F, 0.0F, false);
                canvas.translate(tmW, totalHight + topmaginH);
                layout.draw(canvas);
                lay1H = layout.getHeight();
                textPaint.setTextSize(convertDIP2PX(context, 11));
                StaticLayout layout_ = new StaticLayout(list.get(j).getTime(),
                        textPaint, getMeasuredWidth() - tmW,
                        Layout.Alignment.ALIGN_NORMAL,
                        1.3F, 0.0F, false);
                canvas.translate(0, lay1H + midmaginH);
                layout_.draw(canvas);
                lay2H = layout_.getHeight();
                canvas.translate(0, lay2H + midmaginH);
                mPaint.setColor(leftCircleOtherColor);
                canvas.drawLine(0, 0, getMeasuredWidth() - tmW, 0,
                        mPaint);
                totalHight += topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                currentHight = topmaginH + midmaginH + botmaginH + lay1H + lay2H + 1;
                canvas.translate(-tmW + leftW, -currentHight + topmaginH - timelineHeadRadius);
                mPaint.setColor(timelineHeadColor);
                canvas.drawLine(0, 0 - topmaginH + timelineRadius, 0, currentHight - timelineRadius
                        - 4, mPaint);
                mPaint.setColor(leftCircleOtherColor);
                canvas.drawCircle((float) 0.5, currentHight / 2, timelineRadius, mPaint);
                mPaint.setColor(timelineOtherColor);
                canvas.restore();
            }
        measure(getMeasuredWidth(),totalHight);
        //super.onDraw(canvas);
    }

    public float getTimelineHeadRadius() {
        return timelineHeadRadius;
    }

    public void setTimelineHeadRadius(float timelineHeadRadius) {

        this.timelineHeadRadius = timelineHeadRadius;
        invalidate();
    }

    public int getTimelineHeadColor() {
        return timelineHeadColor;
    }

    public void setTimelineHeadColor(int timelineHeadColor) {

        this.timelineHeadColor = timelineHeadColor;
        invalidate();
    }

    public int getTimelineOtherColor() {
        return timelineOtherColor;
    }

    public void setTimelineOtherColor(int timelineOtherColor) {
        this.timelineOtherColor = timelineOtherColor;
        invalidate();
    }

    public int getTimelineCount() {
        return timelineCount;
    }

    public void setTimelineCount(int timelineCount) {
        this.timelineCount = timelineCount;
        invalidate();
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {

        this.marginTop = marginTop;
        invalidate();
    }

    public int getTimelineRadius() {
        return timelineRadius;
    }

    public void setTimelineRadius(int timelineRadius) {

        this.timelineRadius = timelineRadius;
        invalidate();
    }

    public int getTimelineRadiusDistance() {
        return timelineRadiusDistance;
    }

    public void setTimelineRadiusDistance(int timelineRadiusDistance) {

        this.timelineRadiusDistance = timelineRadiusDistance;
        invalidate();
    }

    public int getTimelineWidth() {
        return timelineWidth;
    }

    public void setTimelineWidth(int timelineWidth) {
        this.timelineWidth = timelineWidth;
    }

    public float getTimeLineViewHeight() {
        this.timeLineViewHeight = getMarginTop() + getTimelineCount()
                * (2 * getTimelineRadius() + getTimelineRadiusDistance());
        return timeLineViewHeight;
    }

    public void setTimeLineViewHeight(float timeLineViewHeight) {
        this.timeLineViewHeight = timeLineViewHeight;
        invalidate();

    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
        invalidate();
    }

    /**
     * 转换dip为px
     */
    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
