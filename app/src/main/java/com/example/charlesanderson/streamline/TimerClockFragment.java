package com.example.charlesanderson.streamline;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimerClockFragment extends Fragment {
    int hours;
    int minutes;
    int seconds;
    String taskName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer_clock, container, false);
        Bundle arguments = this.getArguments();
        this.taskName = arguments.getString("taskName");
        this.hours = arguments.getInt("hours");
        this.minutes = arguments.getInt("minutes");
        this.seconds = 0;
        TextView taskNameText = (TextView)rootView.findViewById(R.id.taskNameClock);
        taskNameText.setText(this.taskName);
        final TextView countdownTimer = (TextView)rootView.findViewById(R.id.countdownTimer);
        int countDownStart = (hours*60*60*1000) + (minutes*60*1000);
        new CountDownTimer(countDownStart, 1000) {

            public void onTick(long millisUntilFinished) {
                countdownTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countdownTimer.setText("done!");
            }
        }.start();

        return rootView;
    }
}
