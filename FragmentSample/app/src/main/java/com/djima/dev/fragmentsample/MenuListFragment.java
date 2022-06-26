package com.djima.dev.fragmentsample;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  生成時はいろいろな記述があったが、消しても問題ないらしいので、いいたん消去してる
public class MenuListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  このフラグメントがどのアクティビティオブジェクト上で表示されているわからないので、アクティビティを動的に取得
        Activity parentActivity = getActivity();

        //  使用する.xml(レイアウトファイル)をインフレート
        //  最後のフラグは、親子関係にするかのフラグ
        View view = inflater.inflate(R.layout.fragment_menu_list,container,false);

        //  インフレートしたレイアウトファイルのviewから探す
        ListView lvView = view.findViewById(R.id.lvMenu);

        //  SimpleAdapter用のデータを生成
        List<Map<String,String>> menuList = new ArrayList<>();
        menuList.add(new HashMap<String,String>(){{
            put("name","日替わり定食");
            put("price","850");
            put("desc","シェフの気まぐれメニューになります。");
        }});
        menuList.add(new HashMap<String,String>(){{
            put("name","焼肉定食");
            put("price","900");
            put("desc","豚肉を使っています。");
        }});

        SimpleAdapter adapter = new SimpleAdapter(
                parentActivity,
                menuList,
                android.R.layout.simple_list_item_2,
                new String[]{"name","price"},
                new int[]{android.R.id.text1,android.R.id.text2}
        );

        lvView.setAdapter(adapter);

        return view;
    }
}