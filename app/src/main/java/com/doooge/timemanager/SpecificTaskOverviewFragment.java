package com.doooge.timemanager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;

import com.doooge.timemanager.SettingPage.SettingActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private LocalDatabaseHelper ldh;
    private SpecificTaskOverviewAdapter adapter;
    private ArrayList<SpecificTask> specificTasks;
    private Button calBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_task_overview, container, false);

        ldh = LocalDatabaseHelper.getInstance(getContext());
        //Assign button listeners to here
        ImageView settingBtn = rootView.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(this);
        calBtn = rootView.findViewById(R.id.showCalender);
        calBtn.setOnClickListener(this);

        Calendar today = Calendar.getInstance();
        specificTasks = ldh.specificTasksSortByStartTime(today);//search all specificTasks that start today

        //Calendar Btn
        calBtn.setText(today.getTime().toString());

        adapter = new SpecificTaskOverviewAdapter(specificTasks, getActivity());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.settingBtn:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.showCalender:
                getSelectedDate();

                break;
        }


    }


    public void getSelectedDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final DatePicker picker = new DatePicker(getContext());
        final Calendar[] finalSelectedCalendar = new Calendar[1];
        builder.setTitle("Create Year");
        builder.setView(picker);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                specificTasks.clear();
                specificTasks = ldh.specificTasksSortByStartTime(selectedCalendar);
                adapter.updateSpecificTaskOverviewAdapter(specificTasks);
                calBtn.setText(selectedCalendar.getTime().toString());
            }
        });
        builder.setNeutralButton("Go back to today", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar selectedCalendar = Calendar.getInstance();
                specificTasks.clear();
                specificTasks = ldh.specificTasksSortByStartTime(selectedCalendar);
                adapter.updateSpecificTaskOverviewAdapter(specificTasks);

            }
        });

        builder.show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (calBtn != null) {
                calBtn.setText(Calendar.getInstance().getTime().toString());
            }
        } else {
        }
    }


}
