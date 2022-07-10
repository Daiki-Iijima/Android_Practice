package com.daiki.android.cameraintentsample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

        //  UIからImageViewを取得
        ImageView ivCamera = findViewById(R.id.ivCamera);

        //  実行結果の処理を格納したランチャーを生成
        //  requestCodeが必要なくなった
        //  onCreateで作成しておく必要があるっぽい
        ActivityResultLauncher<Intent> activityResultLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> { //  結果

                    //  キャンセルされていたら処理を停止
                    if(result.getResultCode() == RESULT_CANCELED) {return;}

                    //  データを取得してnullチェック
                    Intent resultDataIntent = result.getData();
                    if(resultDataIntent == null){ return; }

                    //  撮影された画像のビットマップデータを取得
                    Bitmap bitmap = resultDataIntent.getParcelableExtra("data");

                    //  取得した画像の設定
                    //  この方法だと、サムネイルサイズの低画質の画像しか取得できない
                    //  高画質画像を取得するには、ストレージを経由する必要がある
                    ivCamera.setImageBitmap(bitmap);
                }
        );

        //  クリック時のイベントを設定
        ivCamera.setOnClickListener(new OnCameraImage(activityResultLauncher));
    }

    //  ImageViewがクリックされたとき
    //  CameraImageはxmlの方でカメラの画像をImageViewに設定しているので
    private static class OnCameraImage implements View.OnClickListener{

        ActivityResultLauncher<Intent> mLauncher;

        public OnCameraImage(ActivityResultLauncher<Intent> launcher){
            mLauncher = launcher;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //  アクティビティを起動
            mLauncher.launch(intent);
        }
    }
}