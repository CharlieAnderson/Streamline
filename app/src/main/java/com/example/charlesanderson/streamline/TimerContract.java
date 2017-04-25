package com.example.charlesanderson.streamline;

import android.provider.BaseColumns;

/**
 * Created by charlesanderson on 4/24/17.
 */

public final class TimerContract {
    private TimerContract(){}

    public static class Timer implements BaseColumns {
        public static final String TABLE_NAME = "timers";
        public static final String COLUMN_TASK = "task";
        public static final String COLUMN_TIME_ELAPSED = "time_elapsed";
        public static final String COLUMN_TIME_TOTAL = "time_total";
        public static final String COLUMN_SECTION = "section";
        public static final String COLUMN_COLOR = "color";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+
                TABLE_NAME+" ("+
                _ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_TASK+" TEXT, "+
                COLUMN_TIME_ELAPSED+" INTEGER, "+
                COLUMN_TIME_TOTAL+" INTEGER, "+
                COLUMN_SECTION+" INTEGER, "+
                COLUMN_COLOR+" INTEGER "
                + ")";
    }
}
