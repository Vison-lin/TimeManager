package com.doooge.timemanager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;

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
    //TODO To be deleted: Facked Calendar (搜索条件)
    //================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_task_overview, container, false);

        ldh = new LocalDatabaseHelper(getActivity());
        //Assign button listeners to here
        ImageView settingBtn = rootView.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(this);
        ImageView addBtn = rootView.findViewById(R.id.showCalender);
        addBtn.setOnClickListener(this);
        Calendar today = Calendar.getInstance();
        specificTasks = ldh.specificTasksSortByStartTime(today);//search all specificTasks that start today


        adapter = new SpecificTaskOverviewAdapter(specificTasks, ldh, getActivity());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.settingBtn:
                ldh.showAllData(getContext());
//                Intent intent = new Intent(getActivity(), SettingActivity.class);
//                startActivity(intent);
                break;

            case R.id.showCalender:
                getSelectedDate();



                break;
        }


    }


    public void getSelectedDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final DatePicker picker = new DatePicker(getContext());
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


}
