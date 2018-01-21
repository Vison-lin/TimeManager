package com.doooge.timemanager;

import java.util.Calendar;

/**
 * Created by fredpan on 2018/1/20.
 */

public class SpecificTask extends Task {

    private int isCompleted;
    private String startTime;
    private String endTime;


    public SpecificTask(String taskName, Calendar startTime, Calendar endTime) {
        super(taskName);
        setEndTime(startTime);
        setEndTime(endTime);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = CalendarHelper.convertCal2UTC(endTime);
    }

    public int isCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = CalendarHelper.convertCal2UTC(startTime);
    }
}
