package com.daiki.android.viewmodelsample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {

    private Integer m_Count = 0;

    private CountViewModel m_CountViewModel;

    private TextView m_TvA;
    private TextView m_TvB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //  ボタンにイベントを設定
        view.findViewById(R.id.btA).setOnClickListener(new onClickBtn());
        view.findViewById(R.id.btB).setOnClickListener(new onClickBtn());
        view.findViewById(R.id.btNext).setOnClickListener(new onClickBtn());

        m_TvA = view.findViewById(R.id.tvCountA);
        m_TvB = view.findViewById(R.id.tvCountB);

        m_TvA.setText(m_Count.toString());

        //  ビューモデルを生成
        //  このフラグメントが生成されているActivityを取得することで、同一のActivity上であれば、データが共有される
        m_CountViewModel = new ViewModelProvider(getActivity()).get(CountViewModel.class);

        //  LiveDataでCountの更新を監視
        final Observer<Integer> countObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                m_TvB.setText(count.toString());
            }
        };

        m_CountViewModel.getCount().observe(getViewLifecycleOwner(),countObserver);

        // Inflate the layout for this fragment
        return view;
    }

    //  ボタンクリック時処理
    private class onClickBtn implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

            //  カウント加算
            if(id == R.id.btA){
                m_Count++;
                m_TvA.setText(m_Count.toString());
            }
            if(id == R.id.btB){
                m_CountViewModel.add(1);
            }

            //  画面遷移
            if(id == R.id.btNext){
                //  フラグメントによる遷移
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                NextFragment nextFragment = new NextFragment();
                transaction.replace(R.id.flFragmentHolder,nextFragment);
                //  バックボタンで戻れるようにする
                transaction.addToBackStack("MainFragment");
                transaction.commit();
            }
        }
    }
}