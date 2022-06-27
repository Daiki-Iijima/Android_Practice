package com.daiki.android.databasesample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    //  データベースファイル名の定数フィールド
    private static final String DATABASE_NAME = "cocktailmemo.db";

    //  バージョン情報の定数フィールド
    private static final int DATABASE_VERSION = 1;

    //  コンストラクタ
    //  必須
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //  SQLiteOpenHelperコンストラクタの第2引数のデータベース名が存在していない場合に、1回だけ動く
    //  必須
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //  このメソッドが呼ばれるということはテーブルが存在していないので、テーブルを作成する
        //  テーブル作成のSQL文字列の作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE cocktailmemos(");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("name TEXT,");
        sb.append("note TEXT");
        sb.append(");");
        String sql = sb.toString();

        //  生成した文字列の表示
        Log.i("テーブル作成SQL",sql);

        //  作成したSQL文を実行
        sqLiteDatabase.execSQL(sql);
    }

    //  SQLiteOpenHelperのコンストラクタの第４引数に渡すバージョン情報が内部のバージョンよりも大きい場合、呼ばれるメソッド
    //  必須
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
