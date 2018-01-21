package com.doooge.timemanager;

/**
 * Created by fredpan on 2018/1/20.
 */

public class Task {
    private String taskName;
    private int id = -1;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
