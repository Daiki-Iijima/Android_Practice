package com.daiki.android.todoapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private List<String> mTaskList = new ArrayList<>();

    private ActivityResultLauncher<Intent> mResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("タイトル");
        bar.setSubtitle("サブタイトル");
        bar.setDisplayShowHomeEnabled(true);

        mListView = findViewById(R.id.lvTodo);
        mListView.setOnItemClickListener(new OnListViewItemClickListener());

        updateListView(mTaskList);

        //  このアクティビティに戻ってきたときの処理を記述
        mResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //  キャンセルされていたら処理を停止
                    if(result.getResultCode() == RESULT_CANCELED) {return;}

                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        String task = intent.getStringExtra("task");
                        //  リストを更新
                        addListView(task);
                    }
                }
        );
    }

    private void addListView(String taskName){
        mTaskList.add(taskName);
        updateListView(mTaskList);
    }

    private void updateListView(List<String> data){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                data);

        mListView.setAdapter(adapter);
    }

    private class OnListViewItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String taskName = (String)adapterView.getItemAtPosition(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  インフレーターを取得する
        MenuInflater inflater = getMenuInflater();

        //  引数でうけとったMenuにレイアウトファイルをインフレート
        inflater.inflate(R.menu.option,menu);

        //  オーバーライドしたらtrueを返す決まり
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //  レイアウトファイルで設定してあるIDを取得できるので、ここで分岐する
        int id = item.getItemId();

        //  タスクの追加
        if(id == R.id.btnAdd){
            TaskAddDialogFragment dialog = new TaskAddDialogFragment(new TaskAddListener() {
                @Override
                public void AddTask(String taskName) {

                    mTaskList.add(taskName);

                    updateListView(mTaskList);
                }
            });

            //  ダイアログ表示
            dialog.show(getSupportFragmentManager(),"識別子");
        }

        if(id == R.id.btnAdd2){
            Intent intent = new Intent(MainActivity.this,DialogActivity.class);

            mResultLauncher.launch(intent);
        }

        return true;
    }
}