package com.daiki.android.viewmodelsample;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CountViewModel extends ViewModel {
    private MutableLiveData<Integer> Count;

    public MutableLiveData<Integer> getCount(){
        if(Count == null) {
            Count = new MutableLiveData<Integer>();
            Count.setValue(0);
        }
        return Count;
    }

    public void add(int value){
        if(Count == null) {
            Count = new MutableLiveData<Integer>();
            Count.setValue(0);
        }
        Integer latestCount = Count.getValue();
        int count = latestCount.intValue();
        Count.setValue(count + value);
    }
}
