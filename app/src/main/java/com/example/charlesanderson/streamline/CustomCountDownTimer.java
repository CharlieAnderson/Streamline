package com.example.charlesanderson.streamline;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;

/**
 * Created by charlesanderson on 3/17/17.
 */

public class CustomCountDownTimer extends CountDownTimer {
    boolean isPaused;
    TextView countDownTimerText;
    CircleProgressView circleProgressViewSeconds;
    long millisUntilFinished;

    public CustomCountDownTimer(int start, int interval, TextView countDownTimerText, CircleProgressView circleProgressViewSeconds) {
        super(start, interval);
        this.isPaused = false;
        this.countDownTimerText = countDownTimerText;
        this.circleProgressViewSeconds = circleProgressViewSeconds;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(!this.isPaused) {
            this.millisUntilFinished = millisUntilFinished;
            this.countDownTimerText.setText(parseTime((int) millisUntilFinished));
            System.out.println((float) (((millisUntilFinished / 1000) % 60) / 60.0 * 100.0));
            circleProgressViewSeconds.setProgressWithAnimation((float) (((millisUntilFinished / 1000) % 60) / 60.0 * 100.0), 500);
        }
    }

    @Override
    public void onFinish() {
        countDownTimerText.setText("done!");
    }

    public void setPaused(boolean isPaused){
        this.isPaused = isPaused;
        if(isPaused) {
            this.cancel();
        }
    }

    public String parseTime(int milliseconds) {
        int seconds = milliseconds/1000;
        int minutes = seconds/60;
        int hours = minutes/60;
        String formattedHours;
        String formattedMinutes;
        String formattedSeconds;
        formattedHours = Integer.toString(hours);
        if(minutes%60 < 10)
            formattedMinutes = "0"+(minutes%60);
        else
            formattedMinutes = Integer.toString(minutes);
        if(seconds%60 < 10)
            formattedSeconds = "0"+(seconds%60);
        else
            formattedSeconds = Integer.toString(seconds%60);
        return  formattedHours+":"+formattedMinutes+":"+formattedSeconds;
    }
}
