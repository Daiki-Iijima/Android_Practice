package com.daiki.android.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.inspector.StaticInspectionCompanionProvider;

import androidx.lifecycle.ViewModel;

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
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class TaskDataViewModel extends ViewModel {
    public List<TaskData> TaskList = new ArrayList<>();

    //  コンストラクタ
    //  saveFileName : データを保存するファイル名
    public TaskDataViewModel(){
        TaskList = new ArrayList<>();
    }

    //  ローカルのデータをクリア
    public void clearCacheData(){
        TaskList = new ArrayList<>();
    }

    //  データのクリア
    public void deleteData(Context context,String filename){
        context.deleteFile("TaskData.json");
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
        for(int i = 0;i < TaskList.size();i++) {
            JSONObject jsonObject = new JSONObject();
            TaskData task = TaskList.get(i);
            try {
                jsonObject.put("ID", task.getId());
                jsonObject.put("Task", task.getTask());
                jsonObject.put("IsCompleted", task.isIsCompleted());
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
                data.setId(jsonObject.get("ID").toString());
                data.setTask(jsonObject.get("Task").toString());
                data.setIsCompleted((boolean)jsonObject.get("IsCompleted"));
                TaskList.add(data);
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
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));

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

            BufferedReader reader= new BufferedReader(new InputStreamReader(in,"UTF-8"));
            while( (lineBuffer = reader.readLine()) != null ){
                Log.d("FileAccess",lineBuffer);
                loadData.append(lineBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return loadData.toString();
    }

}
