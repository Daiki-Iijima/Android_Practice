package com.djima.dev.recyclerviewsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.lvMenu);

        //  リサイクラービューにレイアウトマネージャーが必須
        //  レイアウトマネージャーを変更することで各行の配置を変更できる
        //  LinearLayoutManager : 通常のスクロールビューのような配置
        //  GridLayoutManager : グリッド状になる
        //  StaggeredGridLayoutManager : スタッガード格子状になる
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewOriginalAdapter(crateData()));
    }

    private List<Map<String,Object>> crateData(){
        List<Map<String,Object>> retData = new ArrayList<>();

        for(int i=0;i<30;i++) {
            Map<String,Object> tmpData = new HashMap<>();
            //  データ生成
            tmpData.put("name", "カレー定食" + i);
            tmpData.put("price", 900 + i);
            //  データの登録
            retData.add(tmpData);
        }

        return retData;
    }

    //  行レイアウトの情報を保持しておくクラス
    private class RecyclerViewOriginalViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvMenuName;
        private TextView mTvMenuPrice;

        //  継承元のViewHolderクラスには引数なしコンストラクタがないので、引数ありコンストラクタを定義しないと
        //  エラーになる
        //  今回引数に来るViewは、自作のrow.xmlのレイアウトなはず
        public RecyclerViewOriginalViewHolder(@NonNull View itemView){
            super(itemView);

            //  引数で渡されたリストから、画面部品を取得
            mTvMenuName = itemView.findViewById(R.id.tvMenuName);
            mTvMenuPrice = itemView.findViewById(R.id.tvMenuPrice);
        }

        public void setData(String name,String price){
            mTvMenuName.setText(name);
            mTvMenuPrice.setText(price + getString(R.string.tv_menu_unit));
        }
    }

    private class RecyclerViewOriginalAdapter extends RecyclerView.Adapter<RecyclerViewOriginalViewHolder>{

        //  データストア用
        private List<Map<String,Object>> mListData;

        //  コンストラクタ(必須ではない)
        public RecyclerViewOriginalAdapter(List<Map<String,Object>> listData){
            mListData = listData;
        }

        //  必須
        //  行用の部品の生成
        @NonNull
        @Override
        public RecyclerViewOriginalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

            //  リスト1行分のビューをxmlから生成
            View view = inflater.inflate(R.layout.row,parent,false);

            //  ビューホルダーを生成
            RecyclerViewOriginalViewHolder viewHolder = new RecyclerViewOriginalViewHolder(view);

            //  生成したビューホルダーを返す
            return viewHolder;
        }

        //  必須
        //  各行のデータを設定
        @Override
        public void onBindViewHolder(RecyclerViewOriginalViewHolder holder, int position) {
            Map<String,Object> data = new HashMap<>();
            data = mListData.get(position);
            String name = String.format("%s",data.get("name"));
            String price = String.format("%d",data.get("price"));

            holder.setData(name,price);
        }

        //  必須
        //  コンストラクタで受けとったデータの個数を返す
        @Override
        public int getItemCount() {
            return mListData.size();
        }
    }

}