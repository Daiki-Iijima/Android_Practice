package com.daiki.android.databasesample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //  選択されたカクテルの主キーID
    //  -1 : 未選択
    private int m_CocktailId = -1;

    //  選択されたカクテル名
    private String m_CocktailName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  クリック時のイベント
        ListView lvCocktail = findViewById(R.id.lvCocktail);
        lvCocktail.setOnItemClickListener(new onItemClickListener());
    }

    //  保存ボタンクリック時イベント
    //  xmlの方で紐づけ済み
    //  xmlで紐づける場合、publicの必要がある
    public void onSaveButtonClick(View view){
        //  感想欄に入力された要素を消去
        EditText etNote = findViewById(R.id.etNote);
        etNote.setText("");

        //  カクテルを未選択に変更
        TextView tvCocktailName = findViewById(R.id.tvCocktailName);
        tvCocktailName.setText(getString(R.string.tv_lb_name));

        //  選択されていない状態になるので、保存ボタンをタップできないようにする
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
    }

    //  リスト要素クリック時のイベント
    private class onItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //  タップされたコンテンツの行番号をIDとして保存
            m_CocktailId = i;

            //  タップされたコンテンツ名を取得
            m_CocktailName = (String)adapterView.getItemAtPosition(i);

            //  選択された項目を下部のUIに表示
            TextView tvCocktailName = findViewById(R.id.tvCocktailName);
            tvCocktailName.setText(m_CocktailName);

            //  選択されたので、保存ボタンを有効化
            Button btnSave = findViewById(R.id.btnSave);
            btnSave.setEnabled(true);
        }
    }
}