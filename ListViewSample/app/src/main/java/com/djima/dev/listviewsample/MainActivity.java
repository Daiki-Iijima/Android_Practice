package com.djima.dev.listviewsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//  静的なリストを扱うサンプル
//  string.xmlに定義している値をlayoutファイルで指定して扱う方法
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  idからListViewを取得
        ListView listView = findViewById(R.id.lvMenu);

        //  リスナークラスを生成しながらListViewに設定
        listView.setOnItemClickListener(new ListItemClickListener());
    }

    //  リストビューのイベントリスナークラス
    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            //  選択されたアイテムを引数のpositionを使って取得
            String getItem = (String)parent.getItemAtPosition(position);

            //  この方法でも取得できる
            //  リストが複雑な場合は、こちらの方法で取得するのがいいかもしれない
            //TextView textView = (TextView)view;
            //String getItem = textView.getText();

            //  トーストで表示するテキストの生成
            String showText = "["+ getItem + "]が選択されました。";

            //  トーストを表示
            Toast.makeText(MainActivity.this, showText, Toast.LENGTH_SHORT).show();
        }
    }
}