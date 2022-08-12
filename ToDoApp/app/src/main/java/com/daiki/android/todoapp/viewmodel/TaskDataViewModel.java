package com.daiki.android.todoapp.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.daiki.android.todoapp.model.TaskData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TaskDataViewModel extends ViewModel {
    private List<TaskData> mTaskList;

    public List<TaskData> getTaskList() {
        return mTaskList;
    }

    public void setTaskList(List<TaskData> taskList) {
        this.mTaskList = taskList;
    }

    private final static String ID = "ID";
    private final static String TASK = "Task";
    private final static String GROUP = "Group";
    private final static String IS_COMPLETED = "IsCompleted";

    //  タスクの追加
    public void addTask(String str,String groupName){
        mTaskList.add(new TaskData(str,groupName));
    }

    //  タスクの追加
    public void addTask(TaskData data){
        mTaskList.add(data);
    }

    //  コンストラクタ
    //  saveFileName : データを保存するファイル名
    public TaskDataViewModel(){
        mTaskList = new ArrayList<>();
    }

    //  ローカルのデータをクリア
    public void clearCacheData(){
        mTaskList = new ArrayList<>();
    }

    //  データのクリア
    public void deleteData(Context context,String filename){
        context.deleteFile(filename);
        clearCacheData();
    }

    //  データの読み込み
    public void loadData(Context context,String fileName){
        String json = loadFile(context,fileName);
        loadJson(json);
    }

    //  データの保存
    public void saveData(Context context, String fileName){
        String json = createJson();
        saveFile(context,fileName,json);
    }

    //  Jsonデータの生成
    private String createJson(){
        //  Jsonテスト
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < mTaskList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            TaskData task = mTaskList.get(i);
            try {
                jsonObject.put(ID, task.getId());
                jsonObject.put(TASK, task.getTask());
                jsonObject.put(GROUP, task.getTaskGroup());
                jsonObject.put(IS_COMPLETED, task.isCompleted());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }

        return jsonArray.toString();
    }

    //  Jsonデータの読み込み
    private void loadJson(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TaskData data = new TaskData();
                data.setId(jsonObject.get(ID).toString());
                data.setTask(jsonObject.get(TASK).toString());
                data.setTaskGroup(jsonObject.get(GROUP).toString());
                data.setIsCompleted((boolean)jsonObject.get(IS_COMPLETED));
                mTaskList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //  ファイルに保存
    private void saveFile(Context context,String fileName,String data){
        OutputStream out;
        try {
            out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

            writer.append(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  ファイルから読み込み
    private String loadFile(Context context, String fileName){
        InputStream in;
        String lineBuffer;

        StringBuilder loadData = new StringBuilder();

        try {
            in = context.openFileInput(fileName);

            BufferedReader reader= new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8));
            while( (lineBuffer = reader.readLine()) != null ){
                loadData.append(lineBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return loadData.toString();
    }

}
