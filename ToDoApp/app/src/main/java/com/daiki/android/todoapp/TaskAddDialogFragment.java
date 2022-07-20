package com.daiki.android.todoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.zip.Inflater;

public class TaskAddDialogFragment extends DialogFragment {

    private TaskAddListener mTaskAddListener = null;

    private EditText mEditText;

    //  新規追加時
    public TaskAddDialogFragment(TaskAddListener listener){
        mTaskAddListener = listener;
    }

    //  既存データの編集時用
    public TaskAddDialogFragment(TaskAddListener listener,String data){
        mTaskAddListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(R.string.dialog_task_add,new OnClickBtnListener());
        builder.setNegativeButton(R.string.dialog_cancel,new OnClickBtnListener());

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.add_dialog_content,null);
        mEditText = view.findViewById(R.id.etTaskName);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    private  class OnClickBtnListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == Dialog.BUTTON_POSITIVE){
                String taskName = mEditText.getText().toString();
                mTaskAddListener.AddTask(taskName);
            }else if(i == Dialog.BUTTON_NEGATIVE){

            }
        }
    }

}
