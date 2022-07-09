package com.daiki.android.viewmodelsample;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

//  A : Activity内にデータを保持
//  B : ViewModel内にデータを保持
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  フラグメントを表示する
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //  getFragmentsに残っているということは、今は1画面しかないので、それが再生成前に表示されていたFragment
        List<Fragment> latestFragments = manager.getFragments();

        //  0より大きい場合は、何らかの理由で再生成が行われていたことになる
        if(latestFragments.size() > 0) {
            transaction.replace(R.id.flFragmentHolder,latestFragments.get(0));
        }else {
            MainFragment mainFragment = new MainFragment();
            transaction.replace(R.id.flFragmentHolder, mainFragment);
        }

        transaction.commit();

    }
}