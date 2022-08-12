package com.daiki.android.todoapp.viewmodel;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.daiki.android.todoapp.model.GroupData;

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

public class GroupDataViewModel extends ViewModel {

    private GroupDataViewModelListener mListener;

    private List<GroupData> mGroupList;

    private final static String GROUP_NAME = "GroupName";

    public GroupDataViewModel(){
        mGroupList = new ArrayList<>();
    }

    public List<GroupData> getGroupList() {
        return mGroupList;
    }


    private GroupData selectedGroup;

    public GroupData getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(GroupData selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void setGroupList(List<GroupData> mGroupList) {
        this.mGroupList = mGroupList;
    }

    @Nullable
    public GroupData getGroupData(String key){
        for(GroupData data : getGroupList()){
            if(data.getGroupName().equals(key)){
                return data;
            }
        }

        return null;
    }

    public void addGroup(GroupData addGroup){
        boolean isUpdate = false;
        if(mGroupList.size() == 0){
            mGroupList.add(addGroup);
            isUpdate = true;
        }else {
            List<GroupData> tmp = new ArrayList<>(mGroupList);
            for (GroupData group : tmp) {
                //  重複していない場合は、追加
                if (!group.checkEqual(addGroup)) {
                    mGroupList.add(addGroup);
                    isUpdate = true;
                }
            }
        }

        if(isUpdate){
            if(mListener != null){
                mListener.updateValue(getGroupList());
            }
        }
    }

    public void setUpdateHandler(GroupDataViewModelListener listener){
        mListener = listener;
    }

    //  ローカルのデータをクリア
    public void clearCacheData(){
        mGroupList = new ArrayList<>();
    }

    //  データのクリア
    public void deleteData(Context context,String filename){
        context.deleteFile(filename);
        clearCacheData();
    }

    //  データの読み込み
    public void loadData(Context context, String fileName){
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
        for(int i = 0; i < mGroupList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            GroupData groupData = mGroupList.get(i);
            try {
                jsonObject.put(GROUP_NAME, groupData.getGroupName());
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
                GroupData data = new GroupData(jsonObject.get(GROUP_NAME).toString());
                mGroupList.add(data);
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
