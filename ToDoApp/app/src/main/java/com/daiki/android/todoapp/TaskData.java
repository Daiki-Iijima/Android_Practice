package com.daiki.android.todoapp;

import java.util.UUID;

public class TaskData {

    private String mId;
    private String mTask;
    private boolean mIsCompleted;

    public TaskData(){
    }

    public TaskData(String taskName){
        //  UUIDを生成
        setId(UUID.randomUUID().toString());

        setTask(taskName);
        setIsCompleted(false);
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTask() {
        return mTask;
    }

    public void setTask(String mTask) {
        this.mTask = mTask;
    }

    public boolean isCompleted() {
        return mIsCompleted;
    }

    public void setIsCompleted(boolean mIsCompleted) {
        this.mIsCompleted = mIsCompleted;
    }
}
