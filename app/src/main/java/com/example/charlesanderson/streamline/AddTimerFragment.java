package com.example.charlesanderson.streamline;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.thebluealliance.spectrum.SpectrumDialog;


public class AddTimerFragment extends Fragment {
    private int color = 0;
    private int hours;
    private int minutes;
    private int timeInMilliseconds = 0;

    public AddTimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_timer, container, false);
        final TextView taskName = (TextView) rootView.findViewById(R.id.taskName);
        final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        final Button colorButton = (Button) rootView.findViewById(R.id.colorButton);
        final Button timeButton = (Button) rootView.findViewById(R.id.timeButton);

        this.color = getActivity().getResources().getColor(R.color.material_color_red_500);
        Button addTaskButton = (Button) rootView.findViewById(R.id.addTaskButton);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SpectrumDialog.Builder(getContext()).setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        colorButton.setBackgroundColor(color);
                        AddTimerFragment.this.color = color;

                    }
                }).setColors(R.array.colorsArray).setSelectedColor(AddTimerFragment.this.color).build().show(getActivity().getSupportFragmentManager(), "testing dialog");
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerPickerDialog timerPickerDialog = new TimerPickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        AddTimerFragment.this.hours = hour;
                        AddTimerFragment.this.minutes = minute;
                        AddTimerFragment.this.timeInMilliseconds = hour*1000*60*60 + minute*1000*60;
                    }
                }, 0, 0);
                timerPickerDialog.show();
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append(hours).append(":").append(minutes);
                int selectedButtonID = radioGroup.getCheckedRadioButtonId();
                View selectedButton = rootView.findViewById(selectedButtonID);
                int buttonIndex = radioGroup.indexOfChild(selectedButton);
                TaskItem newTask = new TaskItem();
                newTask.setHours(hours);
                newTask.setMinutes(minutes);
                newTask.setTimeElapsed(0);
                newTask.setTimeTotal(timeInMilliseconds);
                newTask.setColor(AddTimerFragment.this.color);
                newTask.setTaskName(taskName.getText().toString());
                newTask.setSection(TaskItem.Section.values()[buttonIndex]);
                ((MainActivity) getActivity()).addTask(newTask);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return rootView;
    }
}
