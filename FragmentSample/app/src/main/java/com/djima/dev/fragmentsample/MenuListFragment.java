package com.djima.dev.fragmentsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  生成時はいろいろな記述があったが、消しても問題ないらしいので、いいたん消去してる
public class MenuListFragment extends Fragment {

    //  端末が大画面かのフラグ
    private boolean m_IsLayoutXLarge = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  このフラグメントがどのアクティビティオブジェクト上で表示されているわからないので、
        //  アクティビティを動的に取得
        Activity parentActivity = getActivity();

        //  使用する.xml(レイアウトファイル)をインフレート
        //  最後のフラグは、親子関係にするかのフラグ
        View view = inflater.inflate(R.layout.fragment_menu_list,container,false);

        //  インフレートしたレイアウトファイルのviewから探す
        ListView lvView = view.findViewById(R.id.lvMenu);

        //  SimpleAdapter用のデータを生成
        List<Map<String,String>> menuList = new ArrayList<>();
        menuList.add(new HashMap<String,String>(){{
            put("name","日替わり定食");
            put("price","850");
            put("desc","シェフの気まぐれメニューになります。");
        }});
        menuList.add(new HashMap<String,String>(){{
            put("name","焼肉定食");
            put("price","900");
            put("desc","豚肉を使っています。");
        }});

        SimpleAdapter adapter = new SimpleAdapter(
                parentActivity,
                menuList,
                android.R.layout.simple_list_item_2,
                new String[]{"name","price"},
                new int[]{android.R.id.text1,android.R.id.text2}
        );

        lvView.setAdapter(adapter);

        //  リストビュー内のアイテムタップ時の処理
        lvView.setOnItemClickListener(new ItemClickListener());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  Activityの状態に依存する(frameMenuThanksがない場合がある)ので、onViewCreatedで判定する
        Activity parentActivity = getActivity();

        if(parentActivity == null){
            return;
        }

        int value =  getResources().getDisplayMetrics().widthPixels;
        Log.i("size",String.format("%d",value));

        View menuThanksFrame = parentActivity.findViewById(R.id.frameMenuThanks);
        //  画面サイズが大きい場合は、xlargeのactivity_main.xmlが使用されるので、frameMenuThanksが存在する
        if(menuThanksFrame == null){
            //  なければ、タブレットではない
            m_IsLayoutXLarge = false;
        }
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Activity parentActivity = getActivity();

            //  選択されたコンテンツを取得
            @SuppressWarnings("unchecked")
            Map<String,String> item = (Map<String,String>)adapterView.getItemAtPosition(i);
            String name = item.get("name");
            String price = item.get("price");

            //  タブレットの場合
            if(m_IsLayoutXLarge) {
                //  Bundleオブジェクトを使用してデータを渡す
                Bundle bundle = new Bundle();

                bundle.putString("menuName",name);
                bundle.putString("menuPrice",price);

                //  フラグメントマネージャーの取得
                FragmentManager manager = getParentFragmentManager();
                //  トランザクションの開始
                //  トランザクション : 一連の処理
                FragmentTransaction transaction = manager.beginTransaction();
                //  表示するFragmentを生成
                MenuThanksFragment menuThanksFragment = new MenuThanksFragment();
                //  渡すデータを設定
                menuThanksFragment.setArguments(bundle);
                //  すでにFragmentが存在している場合もあるので、置き換え処理をする
                transaction.replace(R.id.frameMenuThanks,menuThanksFragment);
                //  commitメソッドを呼ぶことで、transactionの処理が実行される。
                transaction.commit();
            }else { //  スマホの場合
                //  遷移のためのIntentを作成(遷移元、遷移先)
                Intent intent = new Intent(parentActivity, MenuThanksActivity.class);

                intent.putExtra("menuName", name);
                intent.putExtra("menuPrice", price);

                //  画面遷移
                startActivity(intent);
            }
        }
    }
}