package com.example.charlesanderson.streamline;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by charlesanderson on 3/8/17.
 */

public class TimerHolder extends RecyclerView.ViewHolder {
    private Context context;
    private TaskItem.Section section;
    private ProgressBar timerBar;
    private ImageButton startStopButton;
    private TextView timeElapsed;
    private TextView timeLeft;
    private TextView taskName;
    private TaskItem taskItem;
    private int position;

    public TimerHolder(final Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.startStopButton = (ImageButton)itemView.findViewById(R.id.startStopButton);
        this.timeElapsed = (TextView)itemView.findViewById(R.id.timeElapsed);
        this.timeLeft = (TextView)itemView.findViewById(R.id.timeLeft);
        this.taskName = (TextView)itemView.findViewById(R.id.taskName);
        this.timerBar = (ProgressBar)itemView.findViewById(R.id.timerBar);
        this.position = getAdapterPosition();
    }

    public void bindTaskTimer(final TaskItem taskItem) {
        this.section = taskItem.getSection();
        this.taskItem = taskItem;
        this.timerBar.getProgressDrawable().setColorFilter(taskItem.getColor(), PorterDuff.Mode.MULTIPLY);
        this.timeElapsed.setText(R.string.timeStart);
        this.timeElapsed.setText(parseTime(taskItem.getTimeElapsed(), true));
        this.timeLeft.setText(parseTime(taskItem.getTimeTotal()-taskItem.getTimeElapsed(), false));
        this.taskName.setText(taskItem.getTaskName());
        setProgress(taskItem.getProgress());

        this.startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)context;
                Fragment timerClockFragment = new TimerClockFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position", getAdapterPosition());
                bundle.putInt("hours", taskItem.getHours());
                bundle.putInt("minutes", taskItem.getMinutes());
                bundle.putInt("seconds", taskItem.getSeconds());
                bundle.putLong("timeTotal", taskItem.getTimeTotal());
                bundle.putString("timeLeft", timeLeft.toString());
                bundle.putLong("timeElapsed", taskItem.getTimeElapsed());
                bundle.putString("taskName", taskItem.getTaskName());
                bundle.putInt("color", taskItem.getColor());
                timerClockFragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_content, timerClockFragment, "CLOCK_FRAGMENT").commit();
            }
        });
    }

    public String parseTime(long milliseconds, boolean roundUp) {
        int seconds;
        if(roundUp)
            seconds = (int)Math.ceil(milliseconds/1000.00);
        else
            seconds = (int)Math.floor(milliseconds/1000.00);
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

    public void setProgress(int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            this.timerBar.setProgress(progress, true);
        else
            this.timerBar.setProgress(progress);
    }

    public TaskItem getTaskItem() {
        return taskItem;
    }

    public Context getContext() {
        return context;
    }

    public TaskItem.Section getSection() {
        return section;
    }

    public ProgressBar getTimerBar() {
        return timerBar;
    }

    public ImageButton getStartStopButton() {
        return startStopButton;
    }

    public TextView getTimeElapsed() {
        return timeElapsed;
    }

    public TextView getTimeLeft() {
        return timeLeft;
    }

    public TextView getTaskName() {
        return taskName;
    }
}
