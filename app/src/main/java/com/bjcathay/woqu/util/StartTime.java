package com.bjcathay.woqu.util;

import com.bjcathay.android.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhouh on 15-10-30.
 */
public class StartTime {
    public static String setStartTime(long startAt,long now){
        Date startdate = new Date(startAt);
        Date nowdate = new Date(now);
        long betweenTime = startdate.getTime() - nowdate.getTime();
        long days = betweenTime / (1000 * 60 * 60 * 24);
        long hours = (betweenTime-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (betweenTime-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        long msecond = (betweenTime-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000 * 60))/(1000);
        if(days==0){
            return (String.valueOf(hours).length()==1?"0"+
                    hours:hours)+
                    ":"+
                    (String.valueOf(minutes).length()==1?"0"+
                    minutes:minutes)+":"+
                    (String.valueOf(msecond).length()==1?"0"+
                            msecond:msecond);
        }else if((days>0)){
            if(hours==0){
                return days+"天";
            }else{
                return days+"天"+hours+"小时";
            }
        }else if((days<0)){
            return "不限时";
        }
        return null;
    }
    public static long[] setTime(long startAt,long now){
        Date startdate = new Date(startAt);
        Date nowdate = new Date(now);
        long betweenTime = startdate.getTime() - nowdate.getTime();
        long days = betweenTime / (1000 * 60 * 60 * 24);
        long hours = (betweenTime-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (betweenTime-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        long msecond = (betweenTime-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000 * 60))/(1000);
        long[] time = {days,hours,minutes,msecond};

        return  time;
    }


}
