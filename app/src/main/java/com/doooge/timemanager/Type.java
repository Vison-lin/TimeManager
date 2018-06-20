package com.doooge.timemanager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fredpan on 2018/1/20.
 */

public class Type implements Serializable {

    private int id = -999;
    private String name;
    private String color;
    private ArrayList<Task> tasks;

    public Type(String name, String color) {
        this.color = color;
        this.name = name;
        tasks = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public boolean deleteTask(Task task) {
        boolean result = tasks.remove(task);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {

        return name;
    }

    @Override
    public boolean equals(Object o) {
        boolean result =
                (this.id == ((Type) o).getId());
        return result;

    }
}
