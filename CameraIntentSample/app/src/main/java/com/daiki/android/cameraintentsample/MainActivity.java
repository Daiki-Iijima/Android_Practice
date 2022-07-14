package com.daiki.android.cameraintentsample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //  ストレージに保存した画像のURI
    private Uri mImageUri;

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

                    //  取得した画像の設定
                    //  この方法だと、サムネイルサイズの低画質の画像しか取得できない
                    //  高画質画像を取得するには、ストレージを経由する必要がある
                    ivCamera.setImageURI(mImageUri);
                }
        );

        //  クリック時のイベントを設定
        ivCamera.setOnClickListener(new OnCameraImage(activityResultLauncher));
    }

    //  ImageViewがクリックされたとき
    //  CameraImageはxmlの方でカメラの画像をImageViewに設定しているので
    private class OnCameraImage implements View.OnClickListener{

        ActivityResultLauncher<Intent> mLauncher;

        public OnCameraImage(ActivityResultLauncher<Intent> launcher){
            mLauncher = launcher;
        }

        @Override
        public void onClick(View view) {
            //  日時データ用のフォーマッタを作成
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            //  現在日時を秒の単位まで取得
            Date now = new Date(System.currentTimeMillis());
            //  取得したデータをフォーマッタを使用して整形
            String nowStr = dateFormat.format(now);

            String fileName = "CameraIntentSamplePhoto_" + nowStr + ".jpg";

            ContentValues values = new ContentValues();
            //  画像ファイル名を設定
            values.put(MediaStore.Images.Media.TITLE,fileName);
            //  画像ファイルの種類を設定
            values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");

            ContentResolver resolver = getContentResolver();

            //  ContentResolverを使用してURIオブジェクトを生成
            mImageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //  Extra情報として生成したURI情報を付与
            intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);

            //  アクティビティを起動
            mLauncher.launch(intent);
        }
    }
}