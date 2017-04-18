package com.example.charlesanderson.streamline;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
    TaskItem taskItem;
    TimerBarAdapter timerAdapter;
    int taskPosition;
    long start;
    long timeLeft;
    long timeTotal;

    public CustomCountDownTimer(Context context, long start, long timeTotal, int interval, TextView countDownTimerText, CircleProgressView circleProgressViewSeconds, int taskPosition) {
        super(timeTotal-start, interval);
        this.context = context;
        this.timeLeft = timeTotal-start;
        this.timeTotal = timeTotal;
        this.start = start;
        this.isPaused = false;
        this.countDownTimerText = countDownTimerText;
        this.circleProgressViewSeconds = circleProgressViewSeconds;
        this.taskPosition = taskPosition;
        this.timerAdapter = (((MainActivity)this.context).getTimerAdapter());
        this.taskItem = timerAdapter.getTaskItem(taskPosition);
        ((MainActivity)context).createTimerNotification(this.taskItem);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(!this.isPaused) {
            this.timeLeft = (int)millisUntilFinished;
            this.countDownTimerText.setText(parseTime((int) millisUntilFinished));
            this.circleProgressViewSeconds.setProgressWithAnimation((float) (((millisUntilFinished / 1000) % 60) / 60.0 * 100.0), 500);
            this.taskItem.setHours(getHoursLeft());
            this.taskItem.setTimeElapsed(timeTotal-timeLeft);
            double progress = (100.0*((double)timeTotal-timeLeft)/(timeTotal));
            this.taskItem.setProgress((int)progress);
            ((MainActivity)context).updateTimerNotification(this.taskItem);
            this.timerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFinish() {
        countDownTimerText.setText("done!");
        this.timeLeft = 0;
        circleProgressViewSeconds.setProgressWithAnimation((float) (timeLeft), 500);
        TimerBarAdapter timerAdapter = (((MainActivity)this.context).getTimerAdapter());
        timerAdapter.getTaskItem(taskPosition).setProgress(100);
        timerAdapter.getTaskItem(taskPosition).setTimeElapsed(timeTotal);
        timerAdapter.notifyDataSetChanged();
        ((MainActivity)context).updateTimerNotification(this.taskItem);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPaused(boolean isPaused){
        this.isPaused = isPaused;
        if(isPaused) {
            this.cancel();
        }
    }

    public int getHoursLeft() {
        return (int)(((this.timeLeft/1000)/60)/60);
    }

    public int getMinutesLeft() {
        return (int)(((this.timeLeft/1000)/60)%60);

    }

    public int getSecondsLeft() {
        return (int)((this.timeLeft/1000)%60);

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
