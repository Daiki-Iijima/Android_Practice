package com.daiki.android.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DialogActivity extends AppCompatActivity {

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        mEditText = findViewById(R.id.etTask);
        Button btOk = findViewById(R.id.btAdd);
        btOk.setOnClickListener(new OnClickButtonListener());
        Button btCancel = findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new OnClickButtonListener());


    }

    private class OnClickButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if(id == R.id.btAdd){
                Intent intent = new Intent();
                String taskName = mEditText.getText().toString();
                intent.putExtra("task",taskName);
                setResult(RESULT_OK,intent);
            }else if(id == R.id.btCancel){
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
            }else{
                return;
            }

            finish();
        }
    }
}