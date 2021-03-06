

package com.bjcathay.woqu.recyle;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.StringDef;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.Locale;

/**
 * Class that implements a simple timer.
 * <p/>
 * You can give it a start time in the {@link android.os.SystemClock#elapsedRealtime} timebase,
 * and it counts up from that, or if you don't give it a base time, it will use the
 * time at which you call {@link #start}.  By default it will display the current
 * timer value in the form "MM:SS" or "H:MM:SS", or you can use {@link #setFormat}
 * to format the timer value into an arbitrary string.
 *
 * @attr ref android.R.styleable#Chronometer_format
 */
@RemoteView
public class CountDownChronometer extends TextView {
    private static final String TAG = "Chronometer";

    /**
     * A callback that notifies when the chronometer has incremented on its own.
     */
    public interface OnChronometerTickListener {

        /**
         * Notification that the chronometer has changed.
         */
        void onChronometerTick(CountDownChronometer chronometer);

        void onChronometerFinish(CountDownChronometer chronometer);

    }

    private long mBase;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private boolean mLogged;
    private String mFormat;
    private Formatter mFormatter;
    private Locale mFormatterLocale;
    private Object[] mFormatterArgs = new Object[1];
    private StringBuilder mFormatBuilder;
    private OnChronometerTickListener mOnChronometerTickListener;
    private StringBuilder mRecycle = new StringBuilder(8);

    private static final int TICK_WHAT = 2;

    public static final String FLAG_TIME_FORMAT_DEFAULT = "default";
    public static final String FLAG_TIME_FORMAT_CUSTOM = "custom";

    private String timeFormatFlag = FLAG_TIME_FORMAT_DEFAULT;

    /**
     * Initialize this Chronometer object.
     * Sets the base to the current time.
     */
    public CountDownChronometer(Context context) {
        this(context, null, 0);
    }

    /**
     * Initialize with standard view layout information.
     * Sets the base to the current time.
     */
    public CountDownChronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Initialize with standard view layout information and style.
     * Sets the base to the current time.
     */
    public CountDownChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

//        init();
    }

    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    private int time = 0;

    /**
     * set the time of chronometer to count down
     *
     * @param time number of seconds
     */
    public void setTime(int time) {
        this.time = time;
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    /**
     * set the time of chronometer to count down from {@code base}
     *
     * @param seconds   number of seconds
     * @param startTime the time  milliseconds of the chronometer started
     */
    public void setTime(int seconds, long startTime, long currentTime) {
        time = seconds;
        mBase = startTime;
        dispatchChronometerTick();
        updateText(currentTime);
    }

    /**
     * get the total time of chronometer to count down
     *
     * @return time of seconds
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the time that the count-up timer is in reference to.
     *
     * @param base Use the {@link android.os.SystemClock#elapsedRealtime} time base.
     */
    private void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    /**
     * Return the base time as set through {@link #setBase}.
     */
    private long getBase() {
        return mBase;
    }

    /**
     * Sets the format string used for display.  The Chronometer will display
     * this string, with the first "%s" replaced by the current timer value in
     * "MM:SS" or "H:MM:SS" form.
     * <p/>
     * If the format string is null, or if you never call setFormat(), the
     * Chronometer will simply display the timer value in "MM:SS" or "H:MM:SS"
     * form.
     *
     * @param format the format string.
     */
    public void setFormat(String format) {
        mFormat = format;
        if (format != null && mFormatBuilder == null) {
            mFormatBuilder = new StringBuilder(format.length() * 2);
        }
    }

    @StringDef(
            {
                    FLAG_TIME_FORMAT_DEFAULT,
                    FLAG_TIME_FORMAT_CUSTOM
            }
    )
    @Retention(RetentionPolicy.SOURCE)
    @interface Flag {
    }

    public void setFormat(String format, @Flag String flag) {
        mFormat = format;
        timeFormatFlag = flag;

        switch (timeFormatFlag) {
            case FLAG_TIME_FORMAT_CUSTOM:
                break;
            default:
                setFormat(mFormat);
        }
    }

    private String formatText(long seconds) {
        String text = "";
        switch (timeFormatFlag) {
            case FLAG_TIME_FORMAT_CUSTOM:
                text = new Formatter(mFormatBuilder, Locale.getDefault()).format(mFormat, seconds).toString();
                break;
            default:
                text = DateUtils.formatElapsedTime(mRecycle, seconds);
                if (mFormat != null) {
                    Locale loc = Locale.getDefault();
                    if (mFormatter == null || !loc.equals(mFormatterLocale)) {
                        mFormatterLocale = loc;
                        mFormatter = new Formatter(mFormatBuilder, loc);
                    }
                    mFormatBuilder.setLength(0);
                    mFormatterArgs[0] = text;
                    try {
                        mFormatter.format(mFormat, mFormatterArgs);
                        text = mFormatBuilder.toString();
                    } catch (IllegalFormatException ex) {
                        if (!mLogged) {
                            Log.w(TAG, "Illegal format string: " + mFormat);
                            mLogged = true;
                        }
                    }
                }
        }
        return text;
    }

    /**
     * Returns the current format string as set through {@link #setFormat}.
     */
    public String getFormat() {
        return mFormat;
    }

    /**
     * Sets the listener to be called when the chronometer changes.
     *
     * @param listener The listener.
     */
    public void setOnChronometerTickListener(OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    /**
     * @return The listener (may be null) that is listening for chronometer change
     * events.
     */
    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    /**
     * Start counting up.
     * <p/>
     * Chronometer works by regularly scheduling messages to the handler, even when the
     * Widget is not visible.  To make sure resource leaks do not occur, the user should
     * make sure that each start() call has a reciprocal call to {@link #stop}.
     */
    public void start() {
        mStarted = true;
        updateRunning();
//        setBase(SystemClock.elapsedRealtime());
    }

    /**
     * Stop counting up.  This does not affect the base as set from {@link #setBase}, just
     * the view display.
     * <p/>
     * This stops the messages to the handler, effectively releasing resources that would
     * be held as the chronometer is running, via {@link #start}.
     */
    public void stop() {
        mStarted = false;
        updateRunning();
    }

    public void goOn() {
        long seconds = (SystemClock.elapsedRealtime() - mBase);
        seconds /= 1000;
        seconds = time - seconds;
        if (seconds < 0) {
            setText(formatText(0));
            return;
        }
        mStarted = true;
        updateRunning();
    }

    /**
     * The same as calling {@link #start} or {@link #stop}.
     *
     * @hide pending API council approval
     */
    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    private synchronized void updateText(long now) {

        long seconds = (now - mBase);
        seconds /= 1000;
        seconds = time - seconds;
        if (seconds < 0) {
            setText(formatText(0));
            return;
        }
        setText(formatText(seconds));
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(System.currentTimeMillis());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 1000);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(System.currentTimeMillis());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
            }
        }
    };

    void dispatchChronometerFinish() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerFinish(this);
        }
    }

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }
}
