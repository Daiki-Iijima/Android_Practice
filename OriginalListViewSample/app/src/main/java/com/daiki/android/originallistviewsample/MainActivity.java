package com.daiki.android.originallistviewsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView m_LvMenu;                  //  データを表示するリストビュー

    //  private static final XXX : そのクラスでしかアクセスできない && クラスに１つである定数
    //  final : 再代入できない(変数の場合)
    //  static : クラスの中で1つだけ、インスタンスを複数作っても、値が共有される
    //  private static : 複数のインスタンスが生成されても、クラスが同じインスタンスの内部だけで共有される変数
    private static final int[] TO = {R.id.tvMenuNameRow,R.id.tvMenuPriceRow};
    private static final String[] FROM = {"name","price"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_LvMenu = findViewById(R.id.lvMenu);

        setListViewData(createMenuCurryList());

        //  コンテンツクリック時のリスナーを設定
        m_LvMenu.setOnItemClickListener(new onClickMenuItemListener());

        //  コンテキストメニューを表示させるビューを設定
        //  Viewを継承しているクラスであれば、設定できる
        registerForContextMenu(m_LvMenu);
    }

    //  リストビューにデータを表示する
    private void setListViewData(List<Map<String,Object>> data){
        SimpleAdapter adapter = new SimpleAdapter(
                MainActivity.this,
                data,
                R.layout.row,       //  自作セルを指定
                FROM,
                TO
        );

        m_LvMenu.setAdapter(adapter);
    }

    private List<Map<String,Object>> createMenuTeisyokuList(){

        //  priceを数値として扱いたため、ValueはObjectとしている
        List<Map<String,Object>> menuList = new ArrayList<>();

        //  データを登録
        //  Mapはインターフェイスなので、インスタンス化できないので、Mapを実装しているXXXMapクラスをインスタンス化する
        //  初期化ブロックを使って1行で書いている
        menuList.add(new HashMap<String,Object>(){{
            put("name","ハンバーグ定食");
            put("price",800);
            put("desc","手袋をせずに、手でこねて作っています。とても愛情がこもっています。");
        }});

        menuList.add(new HashMap<String,Object>(){{
            put("name","味噌カツ定食");
            put("price",900);
            put("desc","カツと書いてありますが、実は牛です。");
        }});

        return menuList;
    }

    private List<Map<String,Object>> createMenuCurryList(){

        //  priceを数値として扱いたため、ValueはObjectとしている
        List<Map<String,Object>> menuList = new ArrayList<>();

        //  データを登録
        //  Mapはインターフェイスなので、インスタンス化できないので、Mapを実装しているXXXMapクラスをインスタンス化する
        //  初期化ブロックを使って1行で書いている
        menuList.add(new HashMap<String,Object>(){{
            put("name","ポークカレー");
            put("price",900);
            put("desc","実は豚を使っています。");
        }});

        menuList.add(new HashMap<String,Object>(){{
            put("name","ジャワカレー");
            put("price",900);
            put("desc","ジャワカレーです。");
        }});

        return menuList;
    }

    public class onClickMenuItemListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            @SuppressWarnings("unchecked")
            Map<String,Object> item = (Map<String,Object>)parent.getItemAtPosition(position);

            order(item);
        }
    }

    //  注文確定画面へintentを使用してデータを渡しながら遷移する
    private void order(Map<String,Object> item){
        String name = (String)item.get("name");
        //  priceはintなので注意
        Integer price = (Integer)(item.get("price"));


        //  インテントクラスを使って次の画面に情報を渡す
        //  インテント生成時に、今の画面、次の画面の順で引数を渡す
        Intent intent = new Intent(MainActivity.this,MenuThanksActivity.class);
        intent.putExtra("menuName",name);
        //  表示(MenuThanksActivity側でString型で受け取っているので、String型にしている
        intent.putExtra("menuPrice",price + "円");

        //  画面遷移
        startActivity(intent);
    }

    //  オプションメニューをアクションバーに表示する
    //  この書き方がセオリーらしい
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  メニューインフレーターの取得
        MenuInflater inflater = getMenuInflater();
        //  オプションメニュー用.xmlファイルをインフレート
        inflater.inflate(R.menu.menu_options_menu_list,menu);

        //  オーバーライドした場合、常にtrueを返すようにする
        return true;
    }

    //  コンテキストメニューを生成する
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //  親クラスのメソッド呼び出しを最初に行う必要がある
        super.onCreateContextMenu(menu,v,menuInfo);

        MenuInflater inflater = getMenuInflater();

        //  どの項目を選択したかを取得
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int listPosition = info.position;

        @SuppressWarnings("unchecked")
        Map<String,Object> item = (Map<String, Object>) m_LvMenu.getItemAtPosition(listPosition);

        if ("ポークカレー".equals((String) item.get("name"))) {
            inflater.inflate(R.menu.menu_context_spetial, menu);
        } else {//  コンテキストメニューに自作の.xmlをインフレート
            inflater.inflate(R.menu.menu_context_menu_list, menu);
        }

        //  コンテキストメニューのタイトルを設定
        menu.setHeaderTitle(R.string.menu_list_context_header);
    }

    //  オプションメニューのコンテンツが選択されたときに発火する
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //  戻り値 : オプションメニューの選択時の処理を行った場合 = true
        boolean returnVal = true;

        //  選択されたメニューのID
        int itemId = item.getItemId();

        //  IDの値による処理の分岐
        if(itemId == R.id.menuListOptionTeishoku)
            setListViewData(createMenuTeisyokuList());
        else if(itemId == R.id.menuListOptionCurry)
            setListViewData(createMenuCurryList());
        else
            returnVal = super.onOptionsItemSelected(item); //  親クラスの結果をそのまま流す

        return returnVal;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        boolean returnVal = true;

        //  ListViewで何が選択されたのかを取得
        //  長押しされたコンテンツの情報を取得
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int listPosition = info.position;
        @SuppressWarnings("unchecked")
        Map<String,Object> menu = (Map<String, Object>) m_LvMenu.getItemAtPosition(listPosition);

        //  コンテキストメニューで選択されたボタンによって処理を分岐
        int id = item.getItemId();
        if (id == R.id.menuListContextDesc)  // 説明
            Toast.makeText(MainActivity.this,(String)menu.get("desc"),Toast.LENGTH_SHORT).show();
        else if(id == R.id.menuListContextOrder) // 注文
            order(menu);
        else if(id ==  R.id.special)  //  ポークカレー用オプション(menu_context_special)
            Toast.makeText(
                    MainActivity.this,
                    "スペシャルオプションを選択してくれてありがとう。",
                    Toast.LENGTH_SHORT).show();
        else
            returnVal = super.onContextItemSelected(item);

        return returnVal;
    }
}