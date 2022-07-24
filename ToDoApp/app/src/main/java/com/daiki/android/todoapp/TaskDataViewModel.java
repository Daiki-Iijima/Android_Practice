package com.daiki.android.todoapp;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskDataViewModel extends ViewModel {
    public List<TaskData> TaskList = new ArrayList<>();
}
