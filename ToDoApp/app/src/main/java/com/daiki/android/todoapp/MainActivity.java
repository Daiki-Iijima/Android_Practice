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
import android.widget.CompoundButton;
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
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView mResultTextView;
    private CheckBox mIsShowCompletedCheckBox;

    private TaskDataViewModel mTaskList;

    private final static String FILE_NAME = "TaskData.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = findViewById(R.id.tvResult);
        mIsShowCompletedCheckBox = findViewById(R.id.cbIsShowCompleted);
        mIsShowCompletedCheckBox.setOnCheckedChangeListener(new OnChangeCheck());

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
        updateListView(mTaskList.getTaskList());
    }

    private void addListView(String taskName){
        mTaskList.addTask(taskName);
        updateListView(mTaskList.getTaskList());
    }

    private void updateListView(List<TaskData> data){

        List<Map<String,String>> setData = new ArrayList<>();

        String[] from = new String[]{"id"};
        int[] to = new int[]{R.id.tvId};

        for (int i = 0; i < data.size(); i++) {
            HashMap<String, String> tmpData = new HashMap<>();
            TaskData task = data.get(i);
            tmpData.put("id", task.getId());   //  バインドするときに指標にするために
            if(mIsShowCompletedCheckBox.isChecked()) {
                if(!task.isCompleted()){
                    setData.add(tmpData);
                }
            }
            else{
                setData.add(tmpData);
            }
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

        long completedCount = mTaskList.getTaskList().stream().filter(TaskData::isCompleted).count();

        mResultTextView.setText(String.format(Locale.getDefault(),"完了タスク : %d / %d",completedCount, (long) mTaskList.getTaskList().size()));
    }

    private class OnChangeCheck implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int uiId = compoundButton.getId();

            if(uiId == R.id.cbIsShowCompleted){
                updateListView(mTaskList.getTaskList());
            }
        }
    }

    //  タスクが終了したかによってUIを変更する
    //  vg : リストビューのセルのルートオブジェクト
    private void updateCell(String taskId, ViewGroup vg){

        TextView tvTitle = null;
        TextView tvId = null;
        CheckBox cbCompleted = null;

        //  セルのUIを取得
        for(int i = 0;i < vg.getChildCount();i++) {
            View view = (View)vg.getChildAt(i);
            int uiId = view.getId();

            if(uiId == R.id.tvId){
                tvId = (TextView) view;
            } else if(uiId == R.id.tvTitle){
                tvTitle = (TextView) view;
            }else if(uiId == R.id.cbIsCompleted){
                cbCompleted = (CheckBox) view;
            }
        }

        //  セルのTaskDataを取得
        //  IDが入力されていない場合、UIからIDを取得
        if(taskId.isEmpty()){
            taskId = findTaskId(vg);
        }
        String finalTaskId = taskId;
        TaskData data = (TaskData) mTaskList.getTaskList().stream().filter(f -> f.getId().equals(finalTaskId)).toArray()[0];

        //  現在の状態を反映
        if(tvId != null)
            tvId.setText(data.getId());
        if(tvTitle != null)
            tvTitle.setText(data.getTask());
        if(cbCompleted != null)
            cbCompleted.setChecked(data.isCompleted());

        TextPaint paint = null;
        //  ペイントクラスを取得
        if(tvTitle != null)
            paint = tvTitle.getPaint();

        if(data.isCompleted()){
            if(paint != null) {
                paint.setFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                paint.setAntiAlias(true);

                //  テキストの色を薄くする
                int currentNightMode = MainActivity.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        tvTitle.setTextColor(Color.LTGRAY);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        tvTitle.setTextColor(Color.GRAY);
                        break;
                }
            }
        }else{
            if(paint != null) {
                //  取り消し線消去
                paint.setFlags(tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                //  テキストの色を戻す
                int currentNightMode = MainActivity.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        tvTitle.setTextColor(Color.BLACK);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        tvTitle.setTextColor(Color.WHITE);
                        break;
                }
            }
        }

        //  再描画
        //  これを入れないと取り消し線がつかない場合があった
        if(tvTitle != null)
            tvTitle.invalidate();
    }

    //  ViewGroupからTaskIdを取得する
    private String findTaskId(ViewGroup vg){
        String retValue = "";
        //  セルのUIを取得
        for(int i = 0;i < vg.getChildCount();i++) {
            View view = (View)vg.getChildAt(i);
            int uiId = view.getId();

            if(uiId == R.id.tvId){
                TextView tvId = (TextView) view;
                retValue = tvId.getText().toString();
            }
        }

        return retValue;
    }

    private class OnBindSimpleAdapter implements SimpleAdapter.ViewBinder{
        @Override
        public boolean setViewValue(View view, Object o, String s) {
            ViewGroup vg = (ViewGroup)view.getParent();
            updateCell(s,vg);
            return true;
        }
    }

    private class OnItemSelectListener implements AdapterView .OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ViewGroup vg = (ViewGroup)view;
            TaskData taskData = mTaskList.getTaskList().get(i);
            taskData.setIsCompleted(true);

            updateCell(taskData.getId(),vg);

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

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int uiId = item.getItemId();

        if(uiId == R.id.uncheck) {
            ViewGroup vg = (ViewGroup)info.targetView;
            String taskId = findTaskId(vg);
            TaskData data = (TaskData) mTaskList.getTaskList().stream().filter(f -> f.getId().equals(taskId)).toArray()[0];
            data.setIsCompleted(false);

            updateCell(taskId,vg);

            //  保存
            mTaskList.saveData(MainActivity.this,FILE_NAME);

        }else if(uiId == R.id.delete) {
            mTaskList.getTaskList().remove(info.position);
            updateListView(mTaskList.getTaskList());
        }else if(uiId == R.id.rename){
            ViewGroup vg = (ViewGroup)info.targetView;
            String taskId = findTaskId(vg);
            TaskData data = (TaskData) mTaskList.getTaskList().stream().filter(f -> f.getId().equals(taskId)).toArray()[0];

            TaskAddDialogFragment dialog = new TaskAddDialogFragment(name->{
                data.setTask(name);
                updateCell(taskId,vg);

                //  保存
                mTaskList.saveData(MainActivity.this,FILE_NAME);
            },data.getTask());
            //  ダイアログ表示
            dialog.show(getSupportFragmentManager(),"rename_task");
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
            dialog.show(getSupportFragmentManager(),"add_task");
        }

        //  タスクを一気に追加(いずれ消す)
        if(id == R.id.btLoopAdd){
            for(int i = 1;i < 711;i++) {
                addListView(String.format(Locale.getDefault(),"%d",i));
            }
        }

        //  すべてのタスクを消す
        if(id == R.id.btAllDelete){
            mTaskList.deleteData(MainActivity.this,FILE_NAME);
            updateListView(mTaskList.getTaskList());
        }

        return true;
    }
}