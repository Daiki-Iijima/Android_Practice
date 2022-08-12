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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.daiki.android.todoapp.dialog.InputDialogFragment;
import com.daiki.android.todoapp.dialog.TaskDialogFragment;
import com.daiki.android.todoapp.model.GroupData;
import com.daiki.android.todoapp.model.TaskData;
import com.daiki.android.todoapp.viewmodel.GroupDataViewModel;
import com.daiki.android.todoapp.viewmodel.GroupDataViewModelListener;
import com.daiki.android.todoapp.viewmodel.TaskDataViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView mResultTextView;
    private CheckBox mIsShowCompletedCheckBox;

    private TaskDataViewModel mTaskDataViewModel;
    private GroupDataViewModel mGroupDataViewModel;

    private final static String TASK_DATA_JSON = "TaskData.json";
    private final static String GROUP_DATA_JSON = "GroupData.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = findViewById(R.id.tvResult);
        mIsShowCompletedCheckBox = findViewById(R.id.cbIsShowCompleted);
        mIsShowCompletedCheckBox.setOnCheckedChangeListener(new OnChangeCheck());

        //  ViewModelのインスタンスを取得
        mTaskDataViewModel = new ViewModelProvider(MainActivity.this).get(TaskDataViewModel.class);
        mTaskDataViewModel.clearCacheData();
        mTaskDataViewModel.loadData(MainActivity.this, TASK_DATA_JSON);

        mGroupDataViewModel = new ViewModelProvider(MainActivity.this).get(GroupDataViewModel.class);
        mGroupDataViewModel.clearCacheData();
        mGroupDataViewModel.loadData(MainActivity.this,GROUP_DATA_JSON);

        ActionBar bar = getSupportActionBar();
        if(bar == null) { return ;}
        bar.setTitle(R.string.app_name);
        bar.setDisplayShowHomeEnabled(true);

        mListView = findViewById(R.id.lvTodo);
        mListView.setOnItemClickListener(new OnItemSelectListener());
        //  コンテキストメニューを出せるように設定
        registerForContextMenu(mListView);

        //  リストビューを更新
        if(mGroupDataViewModel.getSelectedGroup() != null) {
            Stream<TaskData> stream = mTaskDataViewModel.getTaskList().stream();
            TaskData[] data = stream.filter(taskData -> taskData.getTaskGroup().equals(mGroupDataViewModel.getSelectedGroup().getGroupName())).toArray(TaskData[]::new);
            updateListView(Arrays.asList(data));
        }else{
            updateListView(mTaskDataViewModel.getTaskList());
        }

        ViewGroup lvGroup = findViewById(R.id.lvGroup);

        //  デフォルトのグループデータをビューに追加
        Button btn = new Button(MainActivity.this);
        btn.setText("グループの追加");
        btn.setOnClickListener(view -> {
            //  ダイアログを生成
            InputDialogFragment dialog = new InputDialogFragment(taskName ->{
                Button addBtn = new Button(MainActivity.this);
                addBtn.setText(taskName);
                addBtn.setOnClickListener(new OnClickGroupBtn());
                lvGroup.addView(addBtn);
            },"グループ追加");
            dialog.show(getSupportFragmentManager(),"add_group");
        });
        lvGroup.addView(btn);

        //  すべてのグループを表示できるるグループを設定
        Button defaultBtn = new Button(MainActivity.this);
        defaultBtn.setText("All");
        defaultBtn.setOnClickListener(new OnClickGroupBtn());
        lvGroup.addView(defaultBtn);

        //  グループビューのデータを反映
        for(GroupData data : mGroupDataViewModel.getGroupList()){
            Button addBtn = new Button(MainActivity.this);
            addBtn.setText(data.getGroupName());
            addBtn.setOnClickListener(new OnClickGroupBtn());
            lvGroup.addView(addBtn);
        }

        //  データの更新リスナーを設定
        mGroupDataViewModel.setUpdateHandler(new GroupDataViewModelListener() {
            @Override
            public void updateValue(List<GroupData> updateList) {
                //  グループビューのデータを反映
                for(GroupData data : updateList){
                    Button addBtn = new Button(MainActivity.this);
                    addBtn.setText(data.getGroupName());
                    addBtn.setOnClickListener(new OnClickGroupBtn());
                    lvGroup.addView(addBtn);
                }
            }
        });

    }

    private class OnClickGroupBtn implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Button b = (Button)view;
            String groupName= b.getText().toString();
            GroupData selectGroup = mGroupDataViewModel.getGroupData(groupName);
            mGroupDataViewModel.setSelectedGroup(selectGroup);
            Stream<TaskData> stream = mTaskDataViewModel.getTaskList().stream();
            TaskData[] data = stream.filter(taskData -> taskData.getTaskGroup().equals(groupName)).toArray(TaskData[]::new);
            updateListView(Arrays.asList(data));
        }
    }

    private void addListView(String taskName,String groupName){
        mTaskDataViewModel.addTask(taskName,groupName);
        updateListView(mTaskDataViewModel.getTaskList());
    }

    private void updateListView(List<TaskData> data){

        List<Map<String,String>> setData = new ArrayList<>();

        String[] from = new String[]{"id"};
        int[] to = new int[]{R.id.tvId};

        for (int i = 0; i < data.size(); i++) {
            HashMap<String, String> tmpData = new HashMap<>();
            TaskData task = data.get(i);
            tmpData.put("id", task.getId());   //  バインドするときに指標にするために
            setData.add(tmpData);
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

        mTaskDataViewModel.saveData(MainActivity.this, TASK_DATA_JSON);

        long completedCount = mTaskDataViewModel.getTaskList().stream().filter(TaskData::isCompleted).count();

        mResultTextView.setText(String.format(Locale.getDefault(),"完了タスク : %d / %d",completedCount, (long) mTaskDataViewModel.getTaskList().size()));
    }

    private class OnChangeCheck implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int uiId = compoundButton.getId();
            if(uiId == R.id.cbIsShowCompleted){
                TaskData[] data;
                if(b){
                    data = mTaskDataViewModel.getTaskList().stream().filter(taskData -> !taskData.isCompleted()).toArray(TaskData[]::new);
                }else{
                    data = mTaskDataViewModel.getTaskList().stream().filter(taskData -> taskData.isCompleted()).toArray(TaskData[]::new);
                }
                updateListView(Arrays.asList(data));
            }
        }
    }

    //  タスクが終了したかによってUIを変更する
    //  vg : リストビューのセルのルートオブジェクト
    private void updateCell(String taskId, ViewGroup vg){

        TextView tvTitle;
        TextView tvId;
        CheckBox cbCompleted;

        //  セル内のUIパーツを取得
        int[] ids = {R.id.tvId,R.id.tvTitle,R.id.cbIsCompleted};
        List<View> getViewList = findViewGroupInView(vg,ids);

        if(getViewList.size() == 0){
            return;
        }

        tvId = (TextView)getViewList.stream().filter(v -> v.getId() == R.id.tvId).toArray()[0];
        tvTitle = (TextView)getViewList.stream().filter(v -> v.getId() == R.id.tvTitle).toArray()[0];
        cbCompleted = (CheckBox) getViewList.stream().filter(v -> v.getId() == R.id.cbIsCompleted).toArray()[0];

        TaskData data = (TaskData) mTaskDataViewModel.getTaskList().stream().filter(f -> f.getId().equals(taskId)).toArray()[0];

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

    //  ViewGroupから指定したIDのViewを取得する
    //  見つからない場合、nullが戻る
    public List<View> findViewGroupInView(ViewGroup vg, int[] ids) {
        List<View> retView = new ArrayList<>();
        //  セルのUIを取得
        for (int i = 0; i < vg.getChildCount(); i++) {
            View view = vg.getChildAt(i);
            int viewId = view.getId();

            for (int id : ids) {
                if (viewId == id) {
                    retView.add(view);
                }
            }
        }
        return retView;
    }

    private class OnBindSimpleAdapter implements SimpleAdapter.ViewBinder {
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
            TaskData taskData = mTaskDataViewModel.getTaskList().get(i);
            taskData.setIsCompleted(true);

            updateCell(taskData.getId(),vg);

            //  保存
            mTaskDataViewModel.saveData(MainActivity.this, TASK_DATA_JSON);
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

        ViewGroup vg = (ViewGroup)info.targetView;
        int[] ids = {R.id.tvId,R.id.tvTitle,R.id.cbIsCompleted};
        List<View> views= findViewGroupInView(vg,ids);

        if(views.size() == 0){
            return false;
        }

        TextView idView = (TextView) views.stream().filter(v -> v.getId() == R.id.tvId).toArray()[0];
        String taskId = idView.getText().toString();

        if(uiId == R.id.uncheck) {
            TaskData data = (TaskData) mTaskDataViewModel.getTaskList().stream().filter(f -> f.getId().equals(taskId)).toArray()[0];
            data.setIsCompleted(false);

            updateCell(taskId,vg);

            //  保存
            mTaskDataViewModel.saveData(MainActivity.this, TASK_DATA_JSON);

        }else if(uiId == R.id.delete) {
            mTaskDataViewModel.getTaskList().remove(info.position);
            updateListView(mTaskDataViewModel.getTaskList());
        }else if(uiId == R.id.rename){
            TaskData data = (TaskData) mTaskDataViewModel.getTaskList().stream().filter(f -> f.getId().equals(taskId)).toArray()[0];

            InputDialogFragment dialog = new InputDialogFragment(name->{
                data.setTask(name);
                updateCell(taskId,vg);

                //  保存
                mTaskDataViewModel.saveData(MainActivity.this, TASK_DATA_JSON);
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
            //  ダイアログ生成
            TaskDialogFragment dialog = new TaskDialogFragment((taskName, taskGroup) ->{
                addListView(taskName,taskGroup);
                mGroupDataViewModel.addGroup(new GroupData(taskGroup));
                mGroupDataViewModel.saveData(MainActivity.this,GROUP_DATA_JSON);
            },"タスク追加");

            //  ダイアログ表示
            dialog.show(getSupportFragmentManager(),"add_task");
        }

        //  タスクを一気に追加(いずれ消す)
        if(id == R.id.btLoopAdd){
            for(int i = 1;i < 711;i++) {
                addListView(String.format(Locale.getDefault(),"%d",i),"All");
            }
        }

        //  すべてのタスクを消す
        if(id == R.id.btAllDelete){
            mTaskDataViewModel.deleteData(MainActivity.this, TASK_DATA_JSON);
            updateListView(mTaskDataViewModel.getTaskList());
        }

        return true;
    }

}