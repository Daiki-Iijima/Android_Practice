package com.daiki.android.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private final List<String> mTaskList = new ArrayList<>();

    private ActivityResultLauncher<Intent> mResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        if(bar == null) { return ;}
        bar.setTitle("タイトル");
        bar.setSubtitle("サブタイトル");
        bar.setDisplayShowHomeEnabled(true);

        mListView = findViewById(R.id.lvTodo);

        updateListView(mTaskList);

        //  このアクティビティに戻ってきたときの処理を記述
        mResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //  キャンセルされていたら処理を停止
                    if(result.getResultCode() == RESULT_CANCELED) {return;}

                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        if(intent == null){return;}
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
        List<Map<String,Object>> d = new ArrayList<>();

        for(int i = 0; i< data.size();i++) {
            Map<String, Object> dd = new HashMap<>();
            dd.put("title", data.get(i));
            dd.put("flag", false);
            d.add(dd);
        }

        String[] from = new String[]{"title","flag"};
        int[] to = new int[]{R.id.tvTitle,R.id.cbComp};

        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
                d,
                R.layout.todo_cell,
                from,
                to);

        adapter.setViewBinder(new CustomViewBinder());

        mListView.setAdapter(adapter);
    }

    private static class CustomViewBinder implements SimpleAdapter.ViewBinder{
        @Override
        public boolean setViewValue(View view, Object o, String s) {
            //  チェックボックスを取得
            if(view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setOnCheckedChangeListener((compoundButton, flag) -> {
                    if(flag){
                        //  親のViewを取得
                        ViewGroup vg = (ViewGroup) checkBox.getParent();
                        for(int i = 0;i < vg.getChildCount();i++) {
                            View v = vg.getChildAt(i);
                            //  TextViewすべてを取得
                            if( v instanceof TextView){
                                TextView tv= (TextView)v;
                                //  IDで更に分岐
                                if(tv.getId() == R.id.tvTitle) {
                                    tv.setText("終了");
                                }
                            }
                        }
                    }
                });
            }

            //  タイトル用のテキストを取得
            if(view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setText(s);
            }

            return false;
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
            TaskAddDialogFragment dialog = new TaskAddDialogFragment(taskName -> {

                mTaskList.add(taskName);

                updateListView(mTaskList);
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