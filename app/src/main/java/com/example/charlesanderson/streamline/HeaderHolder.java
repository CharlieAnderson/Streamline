package com.example.charlesanderson.streamline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by charlesanderson on 3/11/17.
 */

public class HeaderHolder extends RecyclerView.ViewHolder  {
    TextView headerTitle;
    Context context;
    TaskItem.Section section;
    public HeaderHolder(Context context, View itemView, TaskItem.Section section) {
        super(itemView);
        this.context = context;
        this.headerTitle = (TextView) itemView.findViewById(R.id.headerTitle);
        this.section = section;
        setHeaderTitleText();
    }

    public void setHeaderTitleText() {
        int index = section.ordinal();
        switch(index) {
            case 0: this.headerTitle.setText(R.string.important_and_urgent);
                break;
            case 1: this.headerTitle.setText(R.string.important_but_not_urgent);
                break;
            case 2: this.headerTitle.setText(R.string.not_important_but_urgent);
                break;
            case 3: this.headerTitle.setText(R.string.not_important_and_not_urgent);
                break;
        }
    }
}
