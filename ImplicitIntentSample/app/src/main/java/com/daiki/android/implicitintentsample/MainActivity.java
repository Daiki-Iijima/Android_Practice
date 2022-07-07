package com.daiki.android.implicitintentsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //  緯度経度ストア用
    private double mLatitude = 0;
    private double mLongitude = 0;

    //  FusedLocationライブラリ関係
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    //  位置情報が変更されたときの処理を行うコールバックオブジェクトフィールド
    private OnUpdateLocation mOnUpdateLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btSearch = findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new OnClickButton());

        Button btShowCurrent = findViewById(R.id.btShowCurrent);
        btShowCurrent.setOnClickListener(new OnClickButton());

        //  FusedLocation関連の処理
        //  Clientオブジェクトの取得
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        mLocationRequest = LocationRequest.create();
        //  更新頻度
        mLocationRequest.setInterval(5000);
        //  最短更新頻度(できればこの頻度で更新する)
        mLocationRequest.setFastestInterval(1000);
        //  更新の優先度
        //  PRIORITY_HIGH_ACCURACY : 可能な限り高精度にする
        //  PRIORITY_BALANCED_POWER_ACCURACY : 電力と精度のバランス型
        //  PRIORITY_LOW_POWER : 精度をある程度犠牲にして電力消費を抑える
        mLocationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);

        //  コールバックメソッドを生成しておく
        mOnUpdateLocation = new OnUpdateLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //  位置情報の追跡を開始
        //  パーミッションの許可が必要
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {

            //  パーミッションの許可を求めるダイアログを表示
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1000);
            return;
        }

        //  座標取得を開始
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mOnUpdateLocation,
                Looper.getMainLooper());
    }

    //  パーミッション要求の結果
    //  requestPermissionsの結果がどのパーミッションでもこのコールバックが呼ばれる
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //  requestPermissionsで指定したrequestCodeかチェック
        //  どのパーミッションの結果なのかをrequestCodeで自分で判断する必要がある
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //  パーミッションのチェック
            //  許可 : PERMISSION_GRANTED
            //  非許可 : PERMISSION_DENIED
            if (ActivityCompat.checkSelfPermission(
                    MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }

        //  座標取得を開始
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mOnUpdateLocation,
                Looper.getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();

        //  位置情報の追跡を停止
        mFusedLocationClient.removeLocationUpdates(mOnUpdateLocation);
    }

    private class OnClickButton implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

            //  検索ボタンクリック
            if(id == R.id.btSearch){
                //  入力された情報を取得
                EditText etSearchWord = findViewById(R.id.etKeyword);
                String searchWord = etSearchWord.getText().toString();

                //  検索開始
                SearchKeyword(searchWord);

            }else if(id == R.id.btShowCurrent){ //  現在位置表示ボタンクリック
                ShowCurrentPos(
                        String.valueOf(mLatitude),
                        String.valueOf(mLongitude)
                );
            }
        }
    }

    private void SearchKeyword(String keyword){

        String searchWord = keyword;

        //  入力されていない場合
        if(searchWord.equals("==")) {
            return;
        }

        try{
            //  文字列をUTF-8の16進数のURL,URIで推奨される文字コードに変換
            searchWord = URLEncoder.encode(searchWord,"UTF-8");
            //  geo:0,0?q= : "geo"が地図アプリを表すURI
            //  "http://"だとブラウザ
            //  0,0?q=文字列 : 地図アプリで文字列検索を行ってくれるURI
            String uriStr = "geo:0,0?q=" + searchWord;
            Uri uri = Uri.parse(uriStr);
//            URLをブラウザで使用する場合
//            String searchStr = "http://" + searchWord;
//            Uri uri = Uri.parse(searchStr);

            //  ACTION_VIEW : 画面を表示させる
            //  ACTION_CALL : 電話をかける
            //  ACTION_SEND : メールを送信する
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        }catch (UnsupportedEncodingException ex){
            Log.e("MainActivity","検索キーワード変換失敗",ex);
        }
    }

    private void ShowCurrentPos(String lat,String log){
        String uriStr = "geo:" + lat + "," + log;
        Uri uri = Uri.parse(uriStr);

        Intent intent = new Intent(Intent.ACTION_VIEW,uri);

        startActivity(intent);
    }

    private class OnUpdateLocation extends LocationCallback{
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            //  最新の位置情報を取得
            Location location = locationResult.getLastLocation();

            if(location == null) {
                return;
            }

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            //  緯度経度をTextViewに表示
            TextView tvLatitude = findViewById(R.id.tvLatitude);
            TextView tvLongitude = findViewById(R.id.tvLongitude);
            //  doubleを桁数を指定して文字列に変換
            tvLatitude.setText(String.format(Locale.getDefault(),"%.4f",mLatitude));
            tvLongitude.setText(String.format(Locale.getDefault(),"%.4f",mLongitude));
        }
    }
}