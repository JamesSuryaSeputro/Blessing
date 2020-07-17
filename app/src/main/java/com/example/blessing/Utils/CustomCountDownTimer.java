package com.example.blessing.Utils;

import android.os.CountDownTimer;
import android.text.format.DateUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

<<<<<<< HEAD
=======
import com.example.blessing.R;

>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
import java.util.concurrent.TimeUnit;

public class CustomCountDownTimer extends CountDownTimer {

    private MutableLiveData<String> countDownTimer = new MutableLiveData();
    private MutableLiveData<Boolean> isStillRunning = new MutableLiveData<Boolean>();

<<<<<<< HEAD
=======
    public LiveData<String> getCountDownTimer() {
        return countDownTimer;
    }

    public LiveData<Boolean> getIsStillRunning() {
        return isStillRunning;
    }

>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     */
    public CustomCountDownTimer(long millisInFuture) {
        super(millisInFuture, 1000);
    }

<<<<<<< HEAD
    public LiveData<String> getCountDownTimer() {
        return countDownTimer;
    }

    public LiveData<Boolean> getIsStillRunning() {
        return isStillRunning;
    }

=======
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
    @Override
    public void onTick(long millisUntilFinished) {
        countDownTimer.postValue(TimeUtils.formatElapseTime(millisUntilFinished));
        isStillRunning.postValue(true);
    }

    @Override
    public void onFinish() {
        countDownTimer.postValue("Time up!");
        isStillRunning.postValue(false);
    }

    static class TimeUtils{
        public static String formatElapseTime(long milliesInFuture){
            return DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(milliesInFuture));
        }
    }
}
