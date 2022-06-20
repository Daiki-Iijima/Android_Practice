package com.djima.dev.listviewsample2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

//  ListViewに表示する内容を動的に生成するサンプル
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  ListViewオブジェクトを取得
        ListView listView = findViewById(R.id.lvMenu);

        //  リストビューに生成するデータの生成
        List<String> menuList = new ArrayList<>();

        //  データの生成
        menuList.add("から揚げ定食");
        menuList.add("ハンバーグ定食");
        menuList.add("生姜焼き定食");
        menuList.add("ステーキ定食");
        menuList.add("野菜炒め定食");
        menuList.add("とんかつ定食");
        menuList.add("ミンチかつ定食");
        menuList.add("チキンカツ定食");
        menuList.add("コロッケ定食");
        menuList.add("回鍋肉定食");
        menuList.add("麻婆豆腐定食");
        menuList.add("青椒肉絲定食");
        menuList.add("焼き魚定食");
        menuList.add("焼肉定食");

        //  アダプタオブジェクトの生成
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,    //  フルパスで指定するほうが安全,独自のRクラスが読み込めなくなる可能性あり
                menuList);

        //  リストビューにアダプタオブジェクトを設定
        listView.setAdapter(adapter);

        //  クリック時に表示するダイアログクラスを生成
        //  自前クラス
        OrderConfirmDialogFragment dialog = new OrderConfirmDialogFragment();

        //  ListViewにitemクリック時のリスナーを登録
        listView.setOnItemClickListener(new ListItemClickListener(dialog));
    }

    //  リストがタップされたときのリスナクラスの生成
    private class ListItemClickListener implements AdapterView.OnItemClickListener{

        //  クリック時に表示するダイアログクラス
        private DialogFragment m_dialog;

        //  コンストラクタ
        public ListItemClickListener(DialogFragment dialog){
            m_dialog = dialog;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id){
            if(m_dialog == null){
                return;
            }

            //  第２引数の値は識別するための文字列なので、任意の文字列でいい
            m_dialog.show(getSupportFragmentManager(),"OrderConfirmDialogFragment");
        }
    }
}