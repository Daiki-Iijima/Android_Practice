package com.daiki.android.todoapp.model;

public class GroupData {
    private String GroupName;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public GroupData(String groupName){
        setGroupName(groupName);
    }

    public boolean checkEqual(GroupData groupData){
        return getGroupName().equals(groupData.getGroupName());
    }
}
