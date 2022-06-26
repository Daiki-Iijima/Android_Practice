package com.djima.dev.fragmentsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MenuThanksFragment extends Fragment {

    //  端末が大画面かのフラグ
    private boolean m_IsLayoutXLarge = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity parentActivity = getActivity();

        View menuThanksFrame = parentActivity.findViewById(R.id.frameMenuThanks);

        //  画面サイズが大きい場合は、xlargeのactivity_main.xmlが使用されるので、frameMenuThanksが存在する
        if(menuThanksFrame == null){
            //  なければ、タブレットではない
            m_IsLayoutXLarge = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Activity parentActivity = getActivity();

        //  .xmlからビューを生成
        View view = inflater.inflate(R.layout.fragment_menu_thanks, container, false);

        Bundle extras;

        //  大画面の場合
        if(m_IsLayoutXLarge){
            //  生成時にsetArgumentsメソッドで設定されたデータを取得する
            extras = getArguments();
        }else{
            //  生成されたActivityからインテントを取得
            Intent intent = parentActivity.getIntent();

            //  受け取ったデータを取得
            extras = intent.getExtras();
        }

        TextView tvMenuName= view.findViewById(R.id.tvMenuName);
        TextView tvMenuPrice= view.findViewById(R.id.tvMenuPrice);

        //  前の画面からデータが来ていたら
        if(extras != null){
            //  Viewを取得してデータの表示
            tvMenuName.setText(extras.getString("menuName"));
            tvMenuPrice.setText(extras.getString("menuPrice"));
        }else{
            //  エラーを表示
            tvMenuName.setText(R.string.menu_name_error);
            tvMenuPrice.setText(R.string.menu_price_error);
        }

        //  戻るボタンのクリック時イベントを追加
        Button btBackButton = view.findViewById(R.id.btBackButton);
        btBackButton.setOnClickListener(new BackButtonClickListener());

        return view;
    }

    //  戻るボタンクリック時イベント処理
    private class BackButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(m_IsLayoutXLarge){
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(MenuThanksFragment.this);
                transaction.commit();
            }else {
                //  フラグメントなので、表示されているアクティビティを終了させて画面遷移する必要がある
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        }
    }
}