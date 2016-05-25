package com.gemptc.wd.bean;

import java.io.Serializable;

/**
 * Created by C5-0 on 2016/5/24.
 */
public class TaskBean implements Serializable {
    private int taskId;
    private int userId;
    private String taskName;
    private String taskDescription;
    private Long taskTime;
    private int taskState;
    private boolean checked = false;
    public TaskBean(int taskId, int userId, String taskName, String taskDescription, Long taskTime, int taskState) {
        this.taskId = taskId;
        this.userId = userId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskTime = taskTime;
        this.taskState = taskState;
    }
    public TaskBean() {
    }

    public TaskBean(String taskName) {
        this.taskName = taskName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Long taskTime) {
        this.taskTime = taskTime;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }
}
