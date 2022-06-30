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
    private static final String APP_ID = "xxx";

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
            String urlFull = WEATHER_INFO_URL + "&q" + q + "&appid=" + APP_ID;

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
            WeatherInfoPostExecutor postExecutor = new WeatherInfoPostExecutor();
            m_UIThreadHandler.post(postExecutor);
        }
    }

    //  天気情報の表示処理をUIスレッドで処理するためのクラス
    private class WeatherInfoPostExecutor implements Runnable{
        @UiThread
        @Override
        public void run() {
        }
    }
}