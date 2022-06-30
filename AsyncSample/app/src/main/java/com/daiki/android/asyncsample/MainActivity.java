package com.daiki.android.asyncsample;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //  Log表示に設定するTAG
    private static final String TAG = "MainActivity";

    //  WeatherAPIのAPIリクエストのためのURL
    private static final String WEATHER_INFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja";

    //  APIキー
    private static final String APP_ID = "";

    private static final int TIME_OUT = 1000;
    private static final int CAN_READ_TIME = 1000;

    //  ビューに表示させるデータ
    private List<Map<String,String>> m_WeatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  表示させるデータの設定
        m_WeatherList = createCityList();

        ListView listView = findViewById(R.id.lvCityList);

        String[] from = {"name"};
        //  android.R.layout.simple_list_item_1のText部分のID
        int[] to = {android.R.id.text1};

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getApplicationContext(),
                m_WeatherList,
                android.R.layout.simple_list_item_1,
                from,
                to
        );

        //  データの表示
        listView.setAdapter(simpleAdapter);

        //  コンテンツクリック時イベント
        listView.setOnItemClickListener(new onClickItemListener());
    }

    //  リストビューで表示する都市データの表示
    private List<Map<String,String>> createCityList(){
        List<Map<String,String>> retData = new ArrayList<>();

        //  データの追加
        //  q : リクエスト時に使うデータ
        retData.add(new HashMap<String,String>(){{
            put("name","大阪");
            put("q","Osaka");
        }});
        retData.add(new HashMap<String,String>(){{
            put("name","東京");
            put("q","Tokyo");
        }});
        retData.add(new HashMap<String,String>(){{
            put("name","神戸");
            put("q","Kobe");
        }});

        return retData;
    }

    //  都市リストのコンテンツクリック時イベントリスナークラス
    private class onClickItemListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Map<String,String> selectItem = (Map<String, String>) adapterView.getItemAtPosition(i);

            //  天気情報取得のためのURL生成
            String q = selectItem.get("q");
            String urlFull = WEATHER_INFO_URL + "&q=" + q + "&appid=" + APP_ID;

            receiveWeatherInfo(urlFull);
        }
    }

    //  天気情報の取得を行う
    //  この中の処理でスレッドが分かれるので、引数はfinalとしてロックしている
    @UiThread
    private void receiveWeatherInfo(final String urlFull){

        //  UIスレッドを確実に取得するためにgetMainLooperを呼ぶ
        Looper mainLooper = Looper.getMainLooper();
        //  LooperからUIスレッドのハンドラーを生成
        Handler handler = HandlerCompat.createAsync(mainLooper);

        //  非同期処理を実行する
        //  結果を表示するためにUIスレッドでの処理が必要なので、ハンドラーを渡す
        WeatherInfoBackgroundReceiver backgroundReceiver = new WeatherInfoBackgroundReceiver(handler,urlFull);
        //  Executors : ファクトリクラス,インスタンスを生成して返してくれる
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //  実行
        executorService.submit(backgroundReceiver);
    }

    //  非同期で天気情報APIにアクセスして結果を取得する
    private class WeatherInfoBackgroundReceiver implements Runnable{

        //  finalをつけているのは、スレッドをまたいだ情報になるので、書き換えが起きないようにロックするため
        private final Handler m_UIThreadHandler;
        private final String m_UrlFull;

        //  コンストラクタ
        public WeatherInfoBackgroundReceiver(Handler uiThreadHandler,String urlFull){
            m_UIThreadHandler = uiThreadHandler;
            m_UrlFull = urlFull;
        }

        @WorkerThread
        @Override
        public void run() {
            //  HTTP接続を行うためのオブジェクトを宣言
            //  HttpURLConnectionはURLConnectionの派生クラス
            //  解放する必要があるので、try外で宣言しておく
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";     //  取得結果

            try{
                //  URLオブジェクトを生成
                URL url = new URL(m_UrlFull);

                //  URLオブジェクトから、HttpURLConnectionオブジェクトを取得
                con = (HttpURLConnection)url.openConnection();

                //  タイムアウト時間の設定
                con.setConnectTimeout(TIME_OUT);
                //  データ取得時間の制限を設定
                con.setReadTimeout(CAN_READ_TIME);

                //  接続開始
                //  結果が返るまでここで止まる
                con.connect();

                //  レスポンスデータを取得
                //  この処理が動いているということは結果は何かしら帰ってきている
                is = con.getInputStream();

                //  InputStream型で受け取った結果をString型に変換
                result = inputStream2String(is);

            }catch (MalformedURLException ex){  //  URLの変換失敗
                Log.e(TAG,"URL変換失敗",ex);
            }catch (SocketTimeoutException ex){ //  通信のタイムアウト(setConnectTimeoutメソッドで設定した時間)
                Log.e(TAG,"通信タイムアウト",ex);
            }catch (IOException ex) {     //  通信失敗
                Log.e(TAG,"通信失敗",ex);
            }finally {  //  開放処理
                if(con != null){
                    con.disconnect();
                }

                if(is != null){
                    try {
                        is.close();
                    }catch (IOException ex){
                        Log.e(TAG,"InputStreamの解放失敗");
                    }
                }
            }

            WeatherInfoPostExecutor postExecutor = new WeatherInfoPostExecutor(result);
            m_UIThreadHandler.post(postExecutor);
        }

        //  InputStreamをString型に変換する処理
        //  よく使われるテンプレート
        private String inputStream2String(InputStream is) throws IOException{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

            StringBuffer sb = new StringBuffer();

            char[] b = new char[1024];
            int line;
            while (0 <= (line = reader.read(b))){
                sb.append(b,0,line);
            }

            return sb.toString();
        }
    }


    //  天気情報の表示処理をUIスレッドで処理するためのクラス
    private class WeatherInfoPostExecutor implements Runnable{

        private String m_Result;

        public WeatherInfoPostExecutor(String result){
            m_Result = result;
        }

        @UiThread
        @Override
        public void run() {
            Log.i(TAG,"天気データ取得結果");
            Log.i(TAG,m_Result);
        }
    }
}