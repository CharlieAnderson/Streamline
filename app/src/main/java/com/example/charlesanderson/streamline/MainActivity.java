package com.example.charlesanderson.streamline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<List<TaskItem>> taskItemsLists;
    private List<HeaderItem> headerItems;
    private List<TimerBarAdapter> timerAdapters;
    private TimerBarAdapter timerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportFragmentManager().findFragmentById(R.id.content_main) == null) {
            this.headerItems = new ArrayList<>();
            this.taskItemsLists = new ArrayList<>();
            this.timerAdapters = new ArrayList<>();

            this.taskItemsLists.add(new ArrayList<TaskItem>());
            this.taskItemsLists.add(new ArrayList<TaskItem>());
            this.taskItemsLists.add(new ArrayList<TaskItem>());
            this.taskItemsLists.add(new ArrayList<TaskItem>());

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