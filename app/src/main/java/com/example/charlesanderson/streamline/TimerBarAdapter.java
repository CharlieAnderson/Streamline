package com.example.charlesanderson.streamline;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;

import java.util.Collections;
import java.util.List;

import static android.view.LayoutInflater.from;

/**
 * Created by charlesanderson on 1/30/17.
 *
 */

class TimerBarAdapter extends RecyclerView.Adapter implements StickyHeaderHandler{
    private static final int TYPE_HEADER1 = 0;
    private static final int TYPE_HEADER2 = 1;
    private static final int TYPE_HEADER3 = 2;
    private static final int TYPE_HEADER4 = 3;

    private static final int TYPE_ITEM = 4;
    private final List<HeaderItem> headerItems;
    private final List<List<TaskItem>> taskItemsLists;
    private Context context;
    private int itemResource;

    TimerBarAdapter(@NonNull Context context, List<HeaderItem> headerItems, List<List<TaskItem>> taskItemsLists) {
        this.headerItems = headerItems;
        this.taskItemsLists = taskItemsLists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final RecyclerView.ViewHolder viewHolder;

        if(viewType == 0) {
            View view = from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            viewHolder = new HeaderHolder(this.context, view, TaskItem.Section.IMPORTANT_AND_URGENT);
        }
        else if(viewType == 1) {
            View view = from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            viewHolder = new HeaderHolder(this.context, view, TaskItem.Section.IMPORTANT_AND_NOT_URGENT);
        }
        else if(viewType == 2) {
            View view = from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            viewHolder = new HeaderHolder(this.context, view, TaskItem.Section.NOT_IMPORTANT_AND_URGENT);
        }
        else if(viewType == 3) {
            View view = from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            viewHolder = new HeaderHolder(this.context, view, TaskItem.Section.NOT_IMPORTANT_AND_NOT_URGENT);
        }
        else {
            View view = from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            viewHolder = new TimerHolder(this.context, view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof HeaderHolder) {
            //HeaderItem headerItem = headerItems.get(position);
            ((HeaderHolder)viewHolder).itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }
        else if(viewHolder instanceof TimerHolder) {
            int index = getTaskListIndex(position);
            TaskItem taskItem = taskItemsLists.get(index).get(getTaskListInnerIndex(position));
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            ((TimerHolder)viewHolder).bindTaskTimer(taskItem);
        }
    }

    @Override
    public int getItemCount() {
        return this.taskItemsLists.get(0).size()
                +this.taskItemsLists.get(1).size()
                +this.taskItemsLists.get(2).size()
                +this.taskItemsLists.get(3).size()
                +this.headerItems.size();
    }

    public int getTaskItemsListsSize() {
        return this.taskItemsLists.get(0).size()
                +this.taskItemsLists.get(1).size()
                +this.taskItemsLists.get(2).size()
                +this.taskItemsLists.get(3).size()-1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override public int getItemViewType(int position) {
        if(position == 0) // headers are at position 0
            return 0;
        else if(position == (1+taskItemsLists.get(0).size()))
            return 1;
        else if(position == (2+taskItemsLists.get(0).size()+taskItemsLists.get(1).size()))
            return 2;
        else if(position ==  (3+taskItemsLists.get(0).size()+taskItemsLists.get(1).size()+taskItemsLists.get(2).size()))
            return 3;
        else
            return 4;
    }

    public int getTaskListIndex(int position) {
        int size = 0;
        size += (1+taskItemsLists.get(0).size());
        if(position < size)
            return 0;
        size += (1+taskItemsLists.get(1).size());
        if(position < size)
            return 1;
        size += (1+taskItemsLists.get(2).size());
        if(position < size)
            return 2;
        return 3;
    }

    public int getTaskListInnerIndex(int position) {
        int size = 0;
        size += (1+taskItemsLists.get(0).size());
        if(position < size)
            return position-1;
        size += (1+taskItemsLists.get(1).size());
        if(position < size)
            return position-(2+taskItemsLists.get(0).size());
        size += (1+taskItemsLists.get(2).size());
        if(position < size)
            return position-(3+taskItemsLists.get(0).size()+taskItemsLists.get(1).size());
        return position-(4+taskItemsLists.get(0).size()+taskItemsLists.get(1).size()+taskItemsLists.get(2).size());
    }

    @Override
    public List<?> getAdapterData() {
        return taskItemsLists;
    }

    public void removeTaskItem(TaskItem.Section section, int position) {
        this.taskItemsLists.get(section.ordinal()).remove(getTaskListInnerIndex(position));
        notifyItemRemoved(position);
    }

    public boolean onItemMove(int fromPosition, TaskItem.Section fromSection, int toPosition, TaskItem.Section toSection) {
        if(fromSection == toSection) {
            // moving element down
            if (fromPosition < toPosition) {
                for (int i = fromSection.ordinal(); i < toSection.ordinal(); i++) {
                    for (int j = fromPosition; j < toPosition; j++) {
                        Collections.swap(this.taskItemsLists.get(i), j, j + 1);
                    }
                }
            }
            //  moving element up
            else {
                for (int i = fromSection.ordinal(); i > toSection.ordinal(); i--) {
                    for (int j = fromPosition; j > toPosition; j--) {
                        Collections.swap(this.taskItemsLists.get(i), j - fromSection.ordinal(), j - 1 - fromSection.ordinal());
                    }
                }
            }

            notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    public TaskItem getTaskItem(int position) {
        int section = getTaskListIndex(position);
        int innerIndex = getTaskListInnerIndex(position);
        return this.taskItemsLists.get(section).get(innerIndex);
    }
}
