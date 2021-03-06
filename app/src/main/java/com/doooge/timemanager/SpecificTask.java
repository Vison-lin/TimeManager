package com.doooge.timemanager;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by fredpan on 2018/1/20.
 */

public class SpecificTask extends Task implements Serializable {

    private int isCompleted;
    private String startTime;
    private String endTime;
    private long compareTime;


    public SpecificTask(String taskName, Calendar startTime, Calendar endTime) {
        super(taskName);
        setStartTime(startTime);
        setEndTime(endTime);
        compareTime = startTime.getTimeInMillis();
    }


    public Calendar getEndTime() {
        return CalendarHelper.convertUTC2Cal(endTime);
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = CalendarHelper.convertCal2UTC(endTime);
    }

    /**
     * @return 0 represent false while 1 represent true.
     */
    public int isCompleted() {
        return isCompleted;
    }

    /**
     * @param completed 0 represent false while 1 represent true.
     */
    public void setCompleted(int completed) {
        isCompleted = completed;
    }

    public boolean isCompletedInBoolean() {
        return isCompleted == 1;
    }

    public Calendar getStartTime() {
        return CalendarHelper.convertUTC2Cal(startTime);
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = CalendarHelper.convertCal2UTC(startTime);

    }

    public long getCompareTime() {
        return compareTime;
    }

}
