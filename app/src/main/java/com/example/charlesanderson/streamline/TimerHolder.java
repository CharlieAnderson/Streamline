package com.example.charlesanderson.streamline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by charlesanderson on 3/8/17.
 */

public class TimerHolder extends RecyclerView.ViewHolder {
    private TaskItem taskItem;
    private Context context;
    private TaskItem.Section section;
    private ProgressBar timerBar;
    private ImageButton startStopButton;
    private TextView timeElapsed;
    private TextView timeLeft;
    private TextView taskName;

    public TimerHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
       // this.timerBar = (TimerBar)itemView.findViewById(R.id.timerBar);
        this.startStopButton = (ImageButton)itemView.findViewById(R.id.startStopButton);
        this.timeElapsed = (TextView)itemView.findViewById(R.id.timeElapsed);
        this.timeLeft = (TextView)itemView.findViewById(R.id.timeLeft);
        this.taskName = (TextView)itemView.findViewById(R.id.taskName);
        this.timerBar = (ProgressBar)itemView.findViewById(R.id.timerBar);

    }

    public void bindTaskTimer(TaskItem taskItem) {
        this.section = taskItem.getSection();
        this.taskItem = taskItem;
        this.timerBar.setBackgroundColor(taskItem.getColor());
        this.timeElapsed.setText(R.string.timeStart);
        this.timeLeft.setText(parseTime(taskItem.getHours(), taskItem.getMinutes()));
        this.taskName.setText(taskItem.getTaskName());
    }

    public String parseTime(int hours, int minutes) {
        String formattedHours;
        String formattedMinutes;
        formattedHours = Integer.toString(hours);
        if(minutes < 10)
            formattedMinutes = "0"+minutes;
        else
            formattedMinutes = Integer.toString(minutes);
        return  formattedHours+":"+formattedMinutes;
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
