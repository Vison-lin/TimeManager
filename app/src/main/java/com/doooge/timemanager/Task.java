package com.doooge.timemanager;

import java.io.Serializable;

/**
 * Created by fredpan on 2018/1/20.
 */

public class Task implements Serializable {
    private String taskName;
    private int id;
    private Type type;

    public Task(String taskName) {
        this.taskName = taskName;
        type = new Type("11","11");
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
