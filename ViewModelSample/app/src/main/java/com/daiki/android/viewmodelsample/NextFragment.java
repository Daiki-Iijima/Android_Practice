package com.daiki.android.viewmodelsample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NextFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next, container, false);

        //  ビューモデルを生成
        //  このフラグメントが生成されているActivityを取得することで、同一のActivity上であれば、データが共有される
        CountViewModel countViewModel = new ViewModelProvider(getActivity()).get(CountViewModel.class);

        TextView tvResult = view.findViewById(R.id.tvResult);
        tvResult.setText(countViewModel.toString());

        view.findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.flFragmentHolder,new FinalFragment());
                transaction.addToBackStack("NextFragment");
                transaction.commit();
            }
        });

        return view;
    }
}