package com.daiki.android.todoapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daiki.android.todoapp.R;

public class TaskDialogFragment extends DialogFragment{

    private TaskDialogListener mTaskDialogListener;

    private String mTitle;
    private String mData;
    private String mGroupName;

    public TaskDialogFragment(TaskDialogListener listener, String title) {
        mTaskDialogListener = listener;
        mTitle = title;
    }

    public TaskDialogFragment(TaskDialogListener listener, String data,String groupName, String title) {
        mTaskDialogListener = listener;
        mTitle = title;
        mData = data;
        mGroupName = groupName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_task_dialog,null);

        TextView tvTitle = view.findViewById(R.id.tvTaskDialogTitle);
        EditText etTaskName = view.findViewById(R.id.etTaskName);
        EditText etGroupName = view.findViewById(R.id.etTaskGroupName);

        tvTitle.setText(mTitle);
        etTaskName.setText(mData);
        etGroupName.setText(mGroupName);

        builder.setPositiveButton(R.string.dialog_task_positive_btn,(inter,i)->{
            mTaskDialogListener.onTaskAdd(
                    etTaskName.getText().toString(),
                    etGroupName.getText().toString());
        });
        builder.setNegativeButton(R.string.dialog_task_negative_btn,(inter,i)->{
        });

        builder.setView(view);
        return builder.create();
    }
}