package com.gemptc.wd.bean;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by C5-0 on 2016/5/24.
 */
public class TaskBean implements Serializable{
    private int taskId;
    private int uId;
    private String taskName;
    private String taskDescription;
    private String taskTime;
    private int taskState;
    public TaskBean() {
    }

    public TaskBean(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }
}
