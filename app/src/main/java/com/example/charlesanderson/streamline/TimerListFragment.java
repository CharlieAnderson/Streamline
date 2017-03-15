package com.example.charlesanderson.streamline;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimerListFragment extends Fragment {
    public TimerListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_timers, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addTimerFragment = new AddTimerFragment();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_content, addTimerFragment, "ADD_FRAGMENT").commit();
            }
        });

        List<TimerBarAdapter> timerAdapters = ((MainActivity) getActivity()).getTimerAdapters();
        final TimerBarAdapter timerBarAdapter = ((MainActivity) getActivity()).getTimerAdapter();
        final RecyclerView recyclerView1= (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView1.setAdapter(timerBarAdapter);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper swipeToDismissHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                if(viewHolder instanceof TimerHolder) {
                    timerBarAdapter.removeTaskItem(((TimerHolder) viewHolder).getSection(), viewHolder.getAdapterPosition());
                }
            }
        });
        swipeToDismissHelper.attachToRecyclerView(recyclerView1);

        return rootView;
    }
}
