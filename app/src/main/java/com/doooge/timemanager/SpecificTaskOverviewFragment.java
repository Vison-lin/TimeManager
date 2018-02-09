package com.doooge.timemanager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    //TODO To be deleted: Facked Calendar (搜索条件)
    private Calendar calendar1;
    private Calendar calendar2;
    //================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_task_overview, container, false);

        ldh = new LocalDatabaseHelper(getActivity());

        //Assign button listeners to here
        // Button addBtn = rootView.findViewById(R.id.addBtn);
        ImageView settingBtn = rootView.findViewById(R.id.settingBtn);
        //Button delBtn = rootView.findViewById(R.id.delBtn);
        //addBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        // delBtn.setOnClickListener(this);
        ImageView addBtn = rootView.findViewById(R.id.showCalender);
        addBtn.setOnClickListener(this);

        //TODO To be deleted: Facked Calendar (搜索条件)
        calendar1 = Calendar.getInstance();//faked calendar
        calendar1.set(2010, 0, 01, 22, 40);
        calendar2 = Calendar.getInstance();//faked calendar
        calendar2.set(2010, 0, 01, 23, 00);
        specificTasks = ldh.specificTasksSortByStartTime(calendar1);//search all specificTasks that start at the faked time
        //================================================


        adapter = new SpecificTaskOverviewAdapter(specificTasks, ldh, getActivity());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onClick(View view) {


        //TODO To be deleted: Faked Type
        Type type = new Type("Type", "Color");
        type.setId(1);

        Type type1 = new Type("type 1", "red");
        type1.setId(2);
        Type type2 = new Type("type 2", "blue");
        type2.setId(3);

        //================================================

        //TODO SAMPLE: ADD AND DELETE FROM DB!
        switch (view.getId()) {

            case R.id.settingBtn:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.showCalender:
                showDatePickerDialog();
                break;
        }


    }


    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        ((DatePickerFragment) newFragment).getCalendar();
    }
}
