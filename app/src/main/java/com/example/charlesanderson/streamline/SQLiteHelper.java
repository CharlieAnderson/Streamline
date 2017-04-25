package com.example.charlesanderson.streamline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.charlesanderson.streamline.TimerContract.Timer.TABLE_NAME;

/**
 * Created by charlesanderson on 4/24/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "streamline_database";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TimerContract.Timer.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public long insertTask(TaskItem taskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TimerContract.Timer.COLUMN_TASK, taskItem.getTaskName());
        contentValues.put(TimerContract.Timer.COLUMN_TIME_ELAPSED, taskItem.getTimeElapsed());
        contentValues.put(TimerContract.Timer.COLUMN_TIME_TOTAL, taskItem.getTimeTotal());
        contentValues.put(TimerContract.Timer.COLUMN_SECTION, taskItem.getSection().ordinal());
        contentValues.put(TimerContract.Timer.COLUMN_COLOR, taskItem.getColor());
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public int updateTask(TaskItem taskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TimerContract.Timer.COLUMN_TASK, taskItem.getTaskName());
        contentValues.put(TimerContract.Timer.COLUMN_TIME_ELAPSED, taskItem.getTimeElapsed());
        contentValues.put(TimerContract.Timer.COLUMN_TIME_TOTAL, taskItem.getTimeTotal());
        contentValues.put(TimerContract.Timer.COLUMN_SECTION, taskItem.getSection().ordinal());
        contentValues.put(TimerContract.Timer.COLUMN_COLOR, taskItem.getColor());
        return db.update(TABLE_NAME, contentValues, "_ID = ? ", new String[] {Long.toString(taskItem.getRowId())});
    }

    public int deleteTask(TaskItem taskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "_ID = ? ",
                new String[] { Long.toString(taskItem.getRowId()) });
    }

    public List<List<TaskItem>> getAllRows() {
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        List<List<TaskItem>> list = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            list.add(new ArrayList<TaskItem>());

        while(cursor.moveToNext()){
            TaskItem taskItem = new TaskItem();
            taskItem.setRowId(cursor.getLong(0));
            taskItem.setTaskName(cursor.getString(1));
            taskItem.setTimeElapsed(cursor.getLong(2));
            taskItem.setTimeTotal(cursor.getLong(3));
            taskItem.setSection(TaskItem.Section.values()[cursor.getInt(4)]);
            taskItem.setColor(cursor.getInt(5));
            list.get(cursor.getInt(4)).add(taskItem);
        }
        cursor.close();
        return list;
    }


}
