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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private TaskDataViewModel mTaskList;

    private final static String FILE_NAME = "TaskData.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskList = new ViewModelProvider(MainActivity.this).get(TaskDataViewModel.class);
        mTaskList.clearCacheData();

        mTaskList.loadData(MainActivity.this,FILE_NAME);

        ActionBar bar = getSupportActionBar();
        if(bar == null) { return ;}
        bar.setTitle(R.string.app_name);
        bar.setDisplayShowHomeEnabled(true);

        mListView = findViewById(R.id.lvTodo);
        mListView.setOnItemClickListener(new OnItemSelectListener());
        //  コンテキストメニューを出せるように設定
        registerForContextMenu(mListView);

        //  リストビューを更新
        updateListView(mTaskList.TaskList);
    }

    private void addListView(String taskName){

        TaskData setData = new TaskData(taskName);

        mTaskList.TaskList.add(setData);
        updateListView(mTaskList.TaskList);
    }

    private void updateListView(List<TaskData> data){

        List<Map<String,String>> setData = new ArrayList<>();

        String[] from = new String[]{"title", "id", "isCompleted"};
        int[] to = new int[]{R.id.tvTitle, R.id.tvId, R.id.cbIsCompleted};

        if(data != null){
            for (int i = 0; i < data.size(); i++) {
                HashMap<String, String> tmpData = new HashMap<>();
                tmpData.put("title", data.get(i).getId());   //  バインドするときに指標にするために
                tmpData.put("id", data.get(i).getId());   //  バインドするときに指標にするために
                tmpData.put("isCompleted", (String.format("%s", data.get(i).isIsCompleted())));   //  バインドするときに指標にするために
                setData.add(tmpData);
            }
        }else{
            mTaskList.deleteData(MainActivity.this,FILE_NAME);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                MainActivity.this,
                setData,
                R.layout.list_cell,
                from,
                to
        );

        simpleAdapter.setViewBinder(new OnBindSimpleAdapter());

        mListView.setAdapter(simpleAdapter);

        mTaskList.saveData(MainActivity.this,FILE_NAME);
    }

    private class OnBindSimpleAdapter implements SimpleAdapter.ViewBinder{
        @Override
        public boolean setViewValue(View view, Object o, String s) {
            if(view instanceof TextView) {
                TextView textView = (TextView) view;
                int id = textView.getId();
                if (id == R.id.tvTitle) {
                    TaskData data = (TaskData) mTaskList.TaskList.stream().filter(f -> f.getId() == o).toArray()[0];
                    textView.setText(data.getTask());
                } else if (id == R.id.tvId) {
                    textView.setText(s);
                }
            }

            if(view instanceof CheckBox) {
                CheckBox cb = (CheckBox)view;

                if (cb.getId() == R.id.cbIsCompleted) {
                    ViewGroup vg = (ViewGroup)cb.getParent();
                    boolean isCompleted =false;
                    for(int i = 0;i < vg.getChildCount();i++) {
                        View v = vg.getChildAt(i);
                        if(v.getId() == R.id.tvId){
                            TextView tvId = (TextView)v;
                            String id= tvId.getText().toString();
                            TaskData data = (TaskData) mTaskList.TaskList.stream().filter(f -> f.getId().equals(id)).toArray()[0];
                            isCompleted = data.isIsCompleted();
                        }
                    }

                    cb.setChecked(isCompleted);
                    if(isCompleted){
                        for(int i = 0;i < vg.getChildCount();i++) {
                            View v = vg.getChildAt(i);
                            if (v instanceof TextView) {
                                if (v.getId() == R.id.tvTitle) {
                                    TextView textView = (TextView) v;
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
                                }
                            }
                        }
                    }else {
                        for (int i = 0; i < vg.getChildCount(); i++) {
                            View v = vg.getChildAt(i);

                            if (v instanceof TextView) {
                                if (v.getId() == R.id.tvTitle) {
                                    TextView tv = v.findViewById(R.id.tvTitle);
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
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    private class OnItemSelectListener implements AdapterView .OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView textView = view.findViewById(R.id.tvTitle);
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

            CheckBox cb = view.findViewById(R.id.cbIsCompleted);
            cb.setChecked(true);

            mTaskList.TaskList.get(i).setIsCompleted(true);

            //  保存
            mTaskList.saveData(MainActivity.this,FILE_NAME);
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
            TextView tv = v.findViewById(R.id.tvTitle);
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

            CheckBox cb = v.findViewById(R.id.cbIsCompleted);
            cb.setChecked(false);

            TextView idTextView = v.findViewById(R.id.tvId);
            String id = idTextView.getText().toString();
            List<TaskData> tmpData = new ArrayList<>();
            for(int i =0;i < mTaskList.TaskList.size();i++){
                TaskData data = mTaskList.TaskList.get(i);
                if(data.getId().equals(id)){
                    data.setIsCompleted(false);
                }
                tmpData.add(data);
            }

            mTaskList.TaskList = tmpData;
            updateListView(mTaskList.TaskList);

        }else if(item.getItemId() == R.id.delete) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            mTaskList.TaskList.remove(info.position);
            updateListView(mTaskList.TaskList);
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

        if(id == R.id.btLoopAdd){
            for(int i = 1;i < 711;i++) {
                addListView(String.format("%d",i));
            }
        }

        if(id == R.id.btAllDelete){

            updateListView(null);

        }

        return true;
    }
}