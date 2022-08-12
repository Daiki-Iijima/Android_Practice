package com.daiki.android.todoapp.viewmodel;

import com.daiki.android.todoapp.model.GroupData;

import java.util.List;

public interface GroupDataViewModelListener {
    void updateValue(List<GroupData> updateList);
}
