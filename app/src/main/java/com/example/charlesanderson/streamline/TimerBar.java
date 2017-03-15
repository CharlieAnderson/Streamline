package com.example.charlesanderson.streamline;

import android.content.Context;

/**
 * Created by charlesanderson on 1/30/17.
 *
 */

public class TimerBar extends android.support.v7.widget.AppCompatSeekBar {
    private int hours;
    private int minutes;
    private int seconds;

    public TimerBar(Context context) {
        super(context);
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    public TimerBar(Context context, int hours, int minutes) {
        super(context);
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = 0;
    }
}
