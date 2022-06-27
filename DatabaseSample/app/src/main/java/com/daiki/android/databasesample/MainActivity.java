package com.daiki.android.databasesample;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

    //  DBへルーパーオブジェクト
    private DatabaseHelper m_DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  クリック時のイベント
        ListView lvCocktail = findViewById(R.id.lvCocktail);
        lvCocktail.setOnItemClickListener(new onItemClickListener());

        //  DBヘルパーオブジェクトの生成
        m_DbHelper = new DatabaseHelper(MainActivity.this);
    }

    //  保存ボタンクリック時イベント
    //  xmlの方で紐づけ済み
    //  xmlで紐づける場合、publicの必要がある
    public void onSaveButtonClick(View view){
        //  感想欄に入力された要素を消去
        EditText etNote = findViewById(R.id.etNote);
        //  入力された情報を保持
        String note = etNote.getText().toString();
        etNote.setText("");

        //  DBヘルパーオブジェクトから、データベース接続オブジェクトを取得
        //  重い処理なので、公式では、非同期処理を勧めている
        SQLiteDatabase db = m_DbHelper.getWritableDatabase();
        //  DBでSQL文を実行するためのインターフェイスを定義
        SQLiteStatement stmt;

        //  すでに情報が存在する可能性があるので、一旦消去してから、インサートを実行
        //  消去
        String sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?";
        stmt = db.compileStatement(sqlDelete);
        //  SQL文の?部分にデータを埋め込む
        stmt.bindLong(1,m_CocktailId);
        //  消去SQLの実行
        stmt.executeUpdateDelete();

        //  インサート
        String sqlInsert = "INSERT INTO cocktailmemos (_id,name,note) VALUES (?,?,?)";
        stmt = db.compileStatement(sqlInsert);
        //  SQL文の?部分にデータを埋め込む
        stmt.bindLong(1,m_CocktailId);
        stmt.bindString(2,m_CocktailName);
        stmt.bindString(3,note);
        //  インサートSQLの実行
        stmt.executeInsert();

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

            //  データベースからデータを取得
            SQLiteDatabase db = m_DbHelper.getWritableDatabase();
            //  検索用SQL
            //  SELECTではbindは使いづらいので、文字列結合を使用する
            String sql = "SELECT * FROM cocktailmemos WHERE _id = " + m_CocktailId;
            //  SQL実行
            //  Cursorオブジェクト : SQLの実行結果がまるごと格納されている
            Cursor cursor = db.rawQuery(sql,null);

            //  実行結果をループで目的のnoteカラムを取得
            String note = "";
            while(cursor.moveToNext()){
                //  カラムのインデックス番号はSQL文によって変わるので、動的に取得する必要がある
                int idxNote = cursor.getColumnIndex("note");
                //  インデックス番号から、データを取得
                note = cursor.getString(idxNote);
            }

            EditText etNote = findViewById(R.id.etNote);
            etNote.setText(note);

        }
    }

    @Override
    protected void onDestroy() {
        //  DBへルーパーオブジェクトを開放
        m_DbHelper.close();
        super.onDestroy();
    }
}