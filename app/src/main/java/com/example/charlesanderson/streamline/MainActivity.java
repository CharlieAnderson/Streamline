package com.example.charlesanderson.streamline;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<List<TaskItem>> taskItemsLists;
    private List<HeaderItem> headerItems;
    private List<TimerBarAdapter> timerAdapters;
    private TimerBarAdapter timerAdapter;
    private BroadcastReceiver resetReceiver;
    private int notificationID;
    private NotificationCompat.Builder builder;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.dbHelper = new SQLiteHelper(this);
        String clockFragment = getIntent().getStringExtra("menuFragment");

        if(getSupportFragmentManager().findFragmentById(R.id.content_main) == null) {
            this.headerItems = new ArrayList<>();
            this.timerAdapters = new ArrayList<>();

            taskItemsLists = this.dbHelper.getAllRows();
            if(taskItemsLists == null) {
                this.taskItemsLists = new ArrayList<>();
                this.taskItemsLists.add(new ArrayList<TaskItem>());
                this.taskItemsLists.add(new ArrayList<TaskItem>());
                this.taskItemsLists.add(new ArrayList<TaskItem>());
                this.taskItemsLists.add(new ArrayList<TaskItem>());
            }

            resetReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // internet lost alert dialog method call from here...
                    resetTimers();
                }
            };
            registerReceiver(resetReceiver, new IntentFilter("RESET_TIMERS"));
            setTimerReset();

            headerItems.add(new HeaderItem(TaskItem.Section.IMPORTANT_AND_URGENT));
            headerItems.add(new HeaderItem(TaskItem.Section.IMPORTANT_AND_NOT_URGENT));
            headerItems.add(new HeaderItem(TaskItem.Section.NOT_IMPORTANT_AND_URGENT));
            headerItems.add(new HeaderItem(TaskItem.Section.NOT_IMPORTANT_AND_NOT_URGENT));

            this.timerAdapter = new TimerBarAdapter(this, headerItems, taskItemsLists);
            Fragment timerListFragment = new TimerListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, timerListFragment, "LIST_FRAGMENT").commit();
        }

        else if(getSupportFragmentManager().findFragmentByTag("CLOCK_FRAGMENT") != null ) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("ClOCK_FRAGMENT");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment, "CLOCK_FRAGMENT").commit();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        //saveFile();
        saveTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.resetReceiver);
        this.dbHelper.close();
    }

    @Override
    public void onBackPressed() {
        TimerClockFragment clockFragment = (TimerClockFragment) getSupportFragmentManager().findFragmentByTag("CLOCK_FRAGMENT");
        if(clockFragment!=null) {
            clockFragment.pauseTimer();
        }
        super.onBackPressed();
    }

    public void saveTasks() {
        for(int i = 0; i<this.taskItemsLists.size(); i++) {
            for(int j = 0; j<this.taskItemsLists.get(i).size(); j++) {
                this.dbHelper.updateTask(taskItemsLists.get(i).get(j));
            }
        }
    }

    public void setTimerReset() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long delay = 24 * 60 * 60 * 1000;

        Intent intent = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void resetTimers() {
        for(int i = 0; i<this.taskItemsLists.size(); i++) {
            for(int j = 0; j<this.taskItemsLists.get(i).size(); j++) {
                this.taskItemsLists.get(i).get(j).setTimeElapsed(0);
                this.taskItemsLists.get(i).get(j).setProgress(0);
            }
        }
        this.timerAdapter.notifyDataSetChanged();
    }

    public void createTimerNotification(TaskItem taskItem) {
        String taskTitle = taskItem.getTaskName();
        String timeElapsed = TimerHolder.parseTime(taskItem.getTimeElapsed(), true);
        String timeTotal = TimerHolder.parseTime(taskItem.getTimeTotal(), false);
        String progress = String.valueOf((int)Math.ceil(taskItem.getProgress()));
        this.builder = new NotificationCompat.Builder(this);
        // Sets an ID for the notification, so it can be updated
        this.notificationID = 1;
        this.builder.setAutoCancel(false);
        this.builder.setOnlyAlertOnce(true);
        if(progress.equals("100")) {
            this.builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle + " - Done!")
                    .setContentText("")
                    .setProgress(100, 100, false);
        }
        else {
            this.builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle +"    "+progress+"%")
                    .setProgress(100, Integer.parseInt(progress), false)
                    .setContentText(timeElapsed+" / "+timeTotal);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.builder.setVisibility(Notification.VISIBILITY_PRIVATE);
        }
        NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
                mNotificationManager.notify(this.notificationID, this.builder.build());
    }

    public void updateTimerNotification(TaskItem taskItem) {
        String taskTitle = taskItem.getTaskName();
        String timeElapsed = TimerHolder.parseTime(taskItem.getTimeElapsed(), true);
        String timeTotal = TimerHolder.parseTime(taskItem.getTimeTotal(), false);
        String progress = String.valueOf((int)Math.ceil(taskItem.getProgress()));
        // Creates an explicit intent for an Activity in your app
        if(progress.equals("100")) {
            this.builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle + " - Done!")
                    .setContentText("")
                    .setProgress(100, 100, false);
        }
        else {
            this.builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle + "    " + progress + "%")
                    .setProgress(100, Integer.parseInt(progress), false)
                    .setContentText(timeElapsed + " / " + timeTotal);
        }
        final Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        this.builder.setContentIntent(contentIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(this.notificationID, this.builder.build());
    }

    public void addTask(TaskItem task) {
        long id = this.dbHelper.insertTask(task);
        task.setRowId(id);
        this.taskItemsLists.get(task.getSection().ordinal()).add(task);
        this.timerAdapter.notifyDataSetChanged();
    }

    public void removeTask(TaskItem task, int position) {
        dbHelper.deleteTask(task);
        this.taskItemsLists.get(task.getSection().ordinal()).remove(timerAdapter.getTaskListInnerIndex(position));
        this.timerAdapter.notifyItemRemoved(position);
    }

    public List<List<TaskItem>> getTaskItemsLists() {
        return this.taskItemsLists;
    }

    public void setTaskItemsLists(List<List<TaskItem>> taskItemsLists) {
        this.taskItemsLists = taskItemsLists;
    }

    public List<HeaderItem> getHeaderItems() {
        return headerItems;
    }

    public void setHeaderItems(List<HeaderItem> headerItems) {
        this.headerItems = headerItems;
    }

    public List<TimerBarAdapter> getTimerAdapters() {
        return this.timerAdapters;
    }

    public void setTimerAdapters(List<TimerBarAdapter> timerAdapters) {
        this.timerAdapters = timerAdapters;
    }

    public TimerBarAdapter getTimerAdapter() {
        return timerAdapter;
    }

    public void setTimerAdapter(TimerBarAdapter timerAdapter) {
        this.timerAdapter = timerAdapter;
    }
}
