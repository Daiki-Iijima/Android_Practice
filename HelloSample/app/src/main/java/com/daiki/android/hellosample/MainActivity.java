package com.daiki.android.hellosample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Button btn = findViewById(R.id.btClick);
         Button btClear = findViewById(R.id.btClear);

         // リスナのインスタンスを生成
         //TestListener longClickListener = new TestListener();
         HelloListener listener = new HelloListener();

         // リスナを設定
         //btn.setOnLongClickListener(longClickListener);
         btn.setOnClickListener(listener);
         btClear.setOnClickListener(listener);
    }

    private class HelloListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            //  名前入力欄用のEditTextウィジェットをidから取得
            EditText input = findViewById(R.id.etName);
            //  結果表示用のTextViewウィジェットをidから取得
            TextView output = findViewById(R.id.tvOutput);

            //  タップされた画面部品のidのR値を取得
            int id = view.getId();

            //  idによって処理を分岐
            switch (id){
                case R.id.btClick:
                    //  入力欄に入力された文字列を取得
                    String inputStr = input.getText().toString();
                    //  結果を表示
                    output.setText(inputStr + "さん、こんいちは！");
                    break;
                case R.id.btClear:
                    //  入力欄を空にする
                    input.setText("");
                    //  結果を空にする
                    output.setText("");
                    break;
            }

        }
    }

    //private class TestListener implements View.OnLongClickListener{
    //    @Override
    //    public boolean onLongClick(View view) {
    //        Log.d("Test","ロングクリックされた");

    //        //  結果表示用のTextViewウィジェットをidから取得
    //        TextView output = findViewById(R.id.tvOutput);

    //        output.setText("ボタンを長押しするとこちらが発火します");
    //        //  true : OnClickのイベントは発火しない
    //        //  false : このメソッドのあとに、OnClickのイベントも発火する
    //        return true;
    //    }
    //}
}