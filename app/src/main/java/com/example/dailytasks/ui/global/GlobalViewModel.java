package com.example.dailytasks.ui.global;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GlobalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GlobalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is global fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}