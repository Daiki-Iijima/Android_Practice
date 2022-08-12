package com.daiki.android.todoapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.daiki.android.todoapp.R;

public class InputDialogFragment extends DialogFragment {

    private final InputDialogListener mInputDialogListener;

    private final String mTitleText;
    private EditText mEditText;
    private String mSetText;

    //  新規追加時
    public InputDialogFragment(InputDialogListener listener, String title){
        mInputDialogListener = listener;
        mTitleText = title;
    }

    //  既存データの編集時用
    public InputDialogFragment(InputDialogListener listener, String data, String title){
        mInputDialogListener = listener;
        mSetText = data;
        mTitleText = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(R.string.dialog_positive_btn,new OnClickBtnListener());
        builder.setNegativeButton(R.string.dialog_negative_btn,new OnClickBtnListener());

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_input_dialog,null);
        mEditText = view.findViewById(R.id.etDialogInput);

        mEditText.setText(mSetText);

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        tvTitle.setText(mTitleText);

        builder.setView(view);

        return builder.create();
    }

    private  class OnClickBtnListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == Dialog.BUTTON_POSITIVE){
                String inputStr = mEditText.getText().toString();
                mInputDialogListener.onInput(inputStr);
            }
        }
    }

}
