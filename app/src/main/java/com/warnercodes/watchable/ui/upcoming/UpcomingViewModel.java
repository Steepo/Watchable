package com.warnercodes.watchable.ui.upcoming;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpcomingViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public UpcomingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
