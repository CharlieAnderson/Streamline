package com.example.charlesanderson.streamline;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;

/**
 * Created by charlesanderson on 3/17/17.
 */

public class CustomCountDownTimer extends CountDownTimer {
    Context context;
    boolean isPaused;
    TextView countDownTimerText;
    CircleProgressView circleProgressViewSeconds;
    int taskPosition;
    int start;
    long millisUntilFinished;

    public CustomCountDownTimer(Context context, int start, int interval, TextView countDownTimerText, CircleProgressView circleProgressViewSeconds, int taskPosition) {
        super(start, interval);
        this.context = context;
        this.start = start;
        this.isPaused = false;
        this.countDownTimerText = countDownTimerText;
        this.circleProgressViewSeconds = circleProgressViewSeconds;
        this.taskPosition = taskPosition;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(!this.isPaused) {
            this.millisUntilFinished = millisUntilFinished;
            this.countDownTimerText.setText(parseTime((int) millisUntilFinished));
            circleProgressViewSeconds.setProgressWithAnimation((float) (((millisUntilFinished / 1000) % 60) / 60.0 * 100.0), 500);
            TimerBarAdapter timerAdapter = (((MainActivity)this.context).getTimerAdapter());
            timerAdapter.getTaskItem(taskPosition).setHours(getHoursLeft());
            timerAdapter.getTaskItem(taskPosition).setMinutes(getMinutesLeft());
            timerAdapter.getTaskItem(taskPosition).setSeconds(getSecondsLeft());
            int progress = (int)((millisUntilFinished*100)/(start));
            timerAdapter.getTaskItem(taskPosition).setProgress(100-progress);
            timerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFinish() {
        countDownTimerText.setText("done!");
        TimerBarAdapter timerAdapter = (((MainActivity)this.context).getTimerAdapter());
        timerAdapter.getTaskItem(taskPosition).setProgress(100);
    }

    public void setPaused(boolean isPaused){
        this.isPaused = isPaused;
        if(isPaused) {
            this.cancel();
        }
    }

    public int getHoursLeft() {
        return (int)(((this.millisUntilFinished/1000)/60)/60);
    }

    public int getMinutesLeft() {
        return (int)(((this.millisUntilFinished/1000)/60)%60);

    }

    public int getSecondsLeft() {
        return (int)((this.millisUntilFinished/1000)%60);

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
            formattedMinutes = Integer.toString(minutes%60);
        if(seconds%60 < 10)
            formattedSeconds = "0"+(seconds%60);
        else
            formattedSeconds = Integer.toString(seconds%60);
        return  formattedHours+":"+formattedMinutes+":"+formattedSeconds;
    }
}
