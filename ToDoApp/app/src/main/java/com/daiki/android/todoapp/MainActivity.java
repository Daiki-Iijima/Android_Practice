package com.daiki.android.todoapp;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private final List<String> mTaskList = new ArrayList<>();

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
        mListView.setOnItemClickListener(new OnItemSelectListener());
        //  コンテキストメニューを出せるように設定
        registerForContextMenu(mListView);

        mTaskList.add("テスト1");
        mTaskList.add("テスト2");
        mTaskList.add("テスト3");

        updateListView(mTaskList);
    }

    private void addListView(String taskName){
        mTaskList.add(taskName);
        updateListView(mTaskList);
    }

    private void updateListView(List<String> data){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );

        mListView.setAdapter(adapter);
    }

    private class OnItemSelectListener implements AdapterView .OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView textView = view.findViewById(android.R.id.text1);
            TextPaint paint = textView.getPaint();
            paint.setFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);

            //  テキストの色を薄くする
            int currentNightMode = MainActivity.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    textView.setTextColor(Color.LTGRAY);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    textView.setTextColor(Color.GRAY);
                    break;
            }

            //  再描画
            //  これを入れないと取り消し線がつかない場合があった
            textView.invalidate();

            //  取り消し線消去
            //paint.setFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context,menu);

        menu.setHeaderTitle("タスクの操作");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        boolean returnVal = true;

            if(item.getItemId() == R.id.uncheck) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                View v = info.targetView;
                TextView tv = v.findViewById(android.R.id.text1);
                TextPaint paint = tv.getPaint();
                //  取り消し線消去
                paint.setFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                //  テキストの色を戻す
                int currentNightMode = MainActivity.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        tv.setTextColor(Color.BLACK);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        tv.setTextColor(Color.WHITE);
                        break;
                }
                //  再描画
                //  これを入れないと取り消し線がつかない場合があった
                tv.invalidate();
            }else if(item.getItemId() == R.id.delete) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mTaskList.remove(info.position);
                updateListView(mTaskList);
            }else {
                returnVal = super.onContextItemSelected(item);
            }

        return returnVal;
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
        if(id == R.id.btAdd){
            TaskAddDialogFragment dialog = new TaskAddDialogFragment(this::addListView);

            //  ダイアログ表示
            dialog.show(getSupportFragmentManager(),"識別子");
        }

        return true;
    }
}