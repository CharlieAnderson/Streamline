package com.example.charlesanderson.streamline;

import static com.example.charlesanderson.streamline.TaskItem.Section.IMPORTANT_AND_URGENT;

/**
 * Created by charlesanderson on 3/7/17.
 *
 */

class TaskItem {
    private int hours;
    private int minutes;
    private int seconds;
    private int color;
    private Section section;
    private String taskName;
    private TimerBar timerBar;

    public enum Section {
        IMPORTANT_AND_URGENT,
        IMPORTANT_AND_NOT_URGENT,
        NOT_IMPORTANT_AND_URGENT,
        NOT_IMPORTANT_AND_NOT_URGENT
    }

    TaskItem() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.taskName = "task";
        this.color = R.color.material_color_red_500;
        this.section = IMPORTANT_AND_URGENT;
    }

    public TaskItem(int hours, int minutes, int seconds, String taskName) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.taskName = taskName;
        this.color = R.color.material_color_red_500;
    }

    void setHours(int hours) {
        this.hours = hours;
    }

    void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setColor(int color) {
        this.color = color;
    }

    void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTimerBar(TimerBar timerBar) {
        this.timerBar = timerBar;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getColor() {
        return color;
    }

    public String getTaskName() {
        return taskName;
    }

    public TimerBar getTimerBar() {
        return timerBar;
    }

    public Section getSection() {
        return section;
    }

}
