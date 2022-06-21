package com.daiki.android.intentsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvMenu = findViewById(R.id.lvMenu);

        //  SimpleAdapterでデータを表示する場合、"List<Map<String,>>"が必要
        List<Map<String,String>> menuList = new ArrayList<>();

        //  データを一時的に保持するMap
        //  データを追加するたびにnewして上書きしていく
        Map<String,String> tmpMenu = new HashMap<>();

        //  要素を生成
        tmpMenu.put("name","唐揚げ定食");
        tmpMenu.put("price","800円");
        menuList.add(tmpMenu);  //  追加

        tmpMenu = new HashMap<>();  //  追加してあるので前のデータを破棄
        tmpMenu.put("name","ハンバーグ定食");
        tmpMenu.put("price","900円");
        menuList.add(tmpMenu);  //  追加

        //  記述するデータの順番で対応するUIと値が変わる
        //  データを割り当てるMapのキー名
        String[] from = {"name","price"};
        //  データを設定するUIのID
        int[] to = {android.R.id.text1,android.R.id.text2};

        //  アダプタークラスを生成
        SimpleAdapter adapter = new SimpleAdapter(
                MainActivity.this,
                menuList,
                android.R.layout.simple_list_item_2,
                from,
                to);

        //  ListViewにデータを設定
        lvMenu.setAdapter(adapter);

        //  リストタップ時のリスナクラスを登録
        lvMenu.setOnItemClickListener(new ListItemClickListener());
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            //  このViewはSimpleAdapterを使っているので、Map<String,?>型で取得する
            Map<String,String> item = (Map<String,String>)parent.getItemAtPosition(position);

            //  Mapからデータを取得
            String menuName = item.get("name");
            String menuPrice = item.get("price");

            //  Intentオブジェクトを生成
            Intent intent = new Intent(MainActivity.this,MenuTanksActivity.class);

            //  次の画面に送るデータを格納
            intent.putExtra("menuName",menuName);
            intent.putExtra("menuPrice",menuPrice);

            //  次の画面へ遷移
            startActivity(intent);
        }
    }
}