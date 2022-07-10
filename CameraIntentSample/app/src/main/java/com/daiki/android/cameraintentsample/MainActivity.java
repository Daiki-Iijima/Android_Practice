package com.daiki.android.cameraintentsample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.ivCamera);
        imageView.setOnClickListener(new OnCameraImage());
    }

    //  startActivityForResultメソッドで起動したActivityの結果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //  親クラスのメソッド呼び出し
        super.onActivityResult(requestCode, resultCode, data);

        //  requestCode : startActivityForResultで引数に指定した数値
        //  resultCode : RESULT_OK = 処理成功,RESULT_CANCELED = 処理キャンセル
        if( requestCode == 200 && resultCode == RESULT_OK){

            //  撮影された画像のビットマップデータを取得
            Bitmap bitmap = data.getParcelableExtra("data");

            //  取得した画像の設定
            //  この方法だと、サムネイルサイズの低画質の画像しか取得できない
            //  高画質画像を取得するには、ストレージを経由する必要がある
            ImageView ivCamera = findViewById(R.id.ivCamera);
            ivCamera.setImageBitmap(bitmap);
        }
    }

    //  ImageViewがクリックされたとき
    //  CameraImageはxmlの方でカメラの画像をImageViewに設定しているので
    private class OnCameraImage implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //  アクティビティを起動
            //  startActivityForResult : 別のActivityを起動するが、戻ってくる場合に使用する
            //  onActivityResultメソッドで結果を確認できる
            startActivityForResult(intent,200);
        }
    }
}