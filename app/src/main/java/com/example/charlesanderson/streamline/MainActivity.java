package com.example.charlesanderson.streamline;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportFragmentManager().findFragmentById(R.id.content_main) == null) {
            this.headerItems = new ArrayList<>();
            this.timerAdapters = new ArrayList<>();

            this.readFile();
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
        saveFile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.resetReceiver);
    }
    public void saveFile() {
        String filename = "streamlineFile";
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        try {
            fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(getTaskItemsLists());
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        String filename = "streamlineFile";
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        try {
            fileInputStream = this.openFileInput(filename);
            objectInputStream = new ObjectInputStream(fileInputStream);
            List<List<TaskItem>> input = (List<List<TaskItem>>)objectInputStream.readObject();
            if(input!=null)
                setTaskItemsLists(input);
            fileInputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimerReset() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if(progress.equals("100")) {
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle + " - Done!")
                    .setContentText("");
        }
        else {
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle +"    "+progress+"%")
                    .setContentText(timeElapsed+" / "+timeTotal);
        }

        // Sets an ID for the notification, so it can be updated

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                builder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
                mNotificationManager.notify(this.notificationID, builder.build());
    }

    public void updateTimerNotification(TaskItem taskItem) {
        String taskTitle = taskItem.getTaskName();
        String timeElapsed = TimerHolder.parseTime(taskItem.getTimeElapsed(), true);
        String timeTotal = TimerHolder.parseTime(taskItem.getTimeTotal(), false);
        String progress = String.valueOf((int)Math.ceil(taskItem.getProgress()));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if(progress.equals("100")) {
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle + " - Done!")
                    .setContentText("");
        }
        else {
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(taskTitle +"    "+progress+"%")
                    .setContentText(timeElapsed+" / "+timeTotal);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(this.notificationID, builder.build());
    }

    public void addTask(TaskItem task) {
        this.taskItemsLists.get(task.getSection().ordinal()).add(task);
        this.timerAdapter.notifyDataSetChanged();
    }

    public void removeTask(TaskItem task) {
        int position = taskItemsLists.indexOf(task);
        this.taskItemsLists.get(task.getSection().ordinal()).remove(task);
        this.timerAdapter.notifyItemRemoved(position);
    }

    public void removeTaskAt(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        this.taskItemsLists.remove(position);
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
