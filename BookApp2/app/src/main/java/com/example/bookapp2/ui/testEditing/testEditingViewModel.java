package com.example.bookapp2.ui.testEditing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class testEditingViewModel extends ViewModel {









    private final MutableLiveData<String> mText;

    public testEditingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is testEditing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}