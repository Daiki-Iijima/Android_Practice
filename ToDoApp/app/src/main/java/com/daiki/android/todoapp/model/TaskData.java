package com.daiki.android.todoapp.model;

import java.util.UUID;

public class TaskData {

    private String mId;
    private String mTask;
    private boolean mIsCompleted;
    private String mTaskGroup;

    public TaskData(){
    }

    public TaskData(String taskName,String groupName){
        //  UUIDを生成
        setId(UUID.randomUUID().toString());

        setTaskGroup(groupName);
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

    public String getTaskGroup() {
        if(mTaskGroup == null){
            return "All";
        }else {
            return mTaskGroup;
        }
    }

    public void setTaskGroup(String mTaskGroup) {
        this.mTaskGroup = mTaskGroup;
    }
}
