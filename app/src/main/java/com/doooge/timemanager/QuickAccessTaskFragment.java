package com.doooge.timemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by diana on 2018-01-27.
 */

public class QuickAccessTaskFragment extends Fragment {
    private ListView mListView;
    private QuickAccessTaskAdapter adapter;
    private ArrayList<SpecificTask> specificTasks;
    private LocalDatabaseHelper ldh;

    //TODO To be deleted: Facked Calendar (搜索条件)
    private Calendar calendar;
    //================================================

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_quick_access_task, container, false);

        ldh = new LocalDatabaseHelper(getActivity());

        //TODO To be deleted: Facked Calendar (搜索条件)
        calendar = Calendar.getInstance();//faked calendar
        calendar.set(2010, 0, 01);
        specificTasks = ldh.specificTasksSortByStartTime(calendar);//search all specificTasks that start at the faked time
        //================================================

        adapter = new QuickAccessTaskAdapter(specificTasks);
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);
        return rootView;
    }

}

