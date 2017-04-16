
package com.example.charlesanderson.streamline;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

class TimerPickerDialog extends TimePickerDialog {
    private int hours;
    private int minutes;
    private int timeInMilliseconds;

    public TimerPickerDialog(Context context, int theme,
                             OnTimeSetListener callBack, int hour, int minute) {
        super(context, theme, callBack, hour, minute, true);
        updateTitle(hour, minute);
        this.hours = hour;
        this.minutes = minute;
        this.timeInMilliseconds = hour*1000*60*60 + minute*1000*60;
    }

    TimerPickerDialog(Context context, OnTimeSetListener callBack,
                      int hour, int minute) {
        super(context, callBack, hour, minute, true);
        updateTitle(hour, minute);
        this.hours = hour;
        this.minutes = minute;
        this.timeInMilliseconds = hour*1000*60*60 + minute*1000*60;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hour, int minute) {
        super.onTimeChanged(view, hour, minute);
        updateTitle(hour, minute);
        this.hours = hour;
        this.minutes = minute;
        this.timeInMilliseconds = hour*1000*60*60 + minute*1000*60;
    }

    private void updateTitle(int hour, int minute) {
        setTitle("Duration: " + hour + ":" + formatNumber(minute));
    }

    private String formatNumber(int number) {
        String result = "";
        if (number < 10) {
            result += "0";
        }
        result += number;
        return result;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getTimeInMilliseconds() {
        return timeInMilliseconds;
    }
}