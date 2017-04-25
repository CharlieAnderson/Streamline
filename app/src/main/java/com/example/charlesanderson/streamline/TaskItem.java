package com.example.charlesanderson.streamline;

import java.io.Serializable;

import static com.example.charlesanderson.streamline.TaskItem.Section.IMPORTANT_AND_URGENT;

/**
 * Created by charlesanderson on 3/7/17.
 *
 */

class TaskItem implements Serializable {
    private int hours;
    private int minutes;
    private int seconds;
    private int color;
    private int progress;
    private long timeElapsed;
    private long timeTotal;  // millisconds that the clock was originally set for
    private long rowId;
    private Section section;
    private String taskName;

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
        this.progress = 0;
        this.timeElapsed = 0;
        this.timeTotal = 0;
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

    public void setSection(Section section) {
        this.section = section;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public void setTimeTotal(long timeTotal) {
        this.timeTotal = timeTotal;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
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

    public Section getSection() {
        return section;
    }

    public int getProgress() {
        return progress;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public long getTimeTotal() {
        return timeTotal;
    }

    public long getRowId() {
        return rowId;
    }
}
