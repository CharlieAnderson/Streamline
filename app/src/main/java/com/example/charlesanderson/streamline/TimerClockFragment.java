package com.example.charlesanderson.streamline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;

import static com.example.charlesanderson.streamline.R.id.countdownTimer;

public class TimerClockFragment extends Fragment {
    int hours;
    int minutes;
    int seconds;
    int taskPosition;
    long timeLeft;
    String taskName;
    CustomCountDownTimer countDownTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_timer_clock, container, false);
        final Bundle arguments = this.getArguments();
        this.taskName = arguments.getString("taskName");
        this.hours = arguments.getInt("hours");
        this.minutes = arguments.getInt("minutes");
        this.seconds = arguments.getInt("seconds");
        this.taskPosition = arguments.getInt("position");

        TextView taskNameText = (TextView)rootView.findViewById(R.id.taskNameClock);
        taskNameText.setText(this.taskName);
        final TextView countdownTimerText = (TextView)rootView.findViewById(countdownTimer);
        final CircleProgressView circleProgressViewSeconds = (CircleProgressView)rootView.findViewById(R.id.circularProgressViewSeconds);
        final int countDownStart = (hours*60*60*1000) + (minutes*60*1000) + (seconds*1000);

        circleProgressViewSeconds.setStartAngle(-90);
        circleProgressViewSeconds.setTextEnabled(false);
        circleProgressViewSeconds.setTextSuffix("% Complete");
        circleProgressViewSeconds.setCircleColor(arguments.getInt("color"));

        this.countDownTimer = new CustomCountDownTimer(getActivity(), countDownStart, 1000, countdownTimerText, circleProgressViewSeconds, taskPosition);
        this.countDownTimer.start();

        final ImageButton pauseButton = (ImageButton)rootView.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.setPaused(!countDownTimer.isPaused);
                if(countDownTimer.isPaused) {
                    timeLeft = countDownTimer.millisUntilFinished;
                    pauseButton.setImageDrawable(rootView.getResources().getDrawable(R.mipmap.ic_play_arrow_black_24dp));
                }
                else {
                    pauseButton.setImageDrawable(rootView.getResources().getDrawable(R.mipmap.ic_pause_black_24dp));
                    countDownTimer = new CustomCountDownTimer(getActivity(), (int)timeLeft, 1000, countdownTimerText, circleProgressViewSeconds, taskPosition);
                    circleProgressViewSeconds.setStartAngle(-90);
                    circleProgressViewSeconds.setTextEnabled(false);
                    circleProgressViewSeconds.setTextSuffix("% Complete");
                    circleProgressViewSeconds.setCircleColor(arguments.getInt("color"));
                    countDownTimer.start();
                }
            }
        });

        ImageButton stopButton = (ImageButton)rootView.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.setPaused(true);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }
}
