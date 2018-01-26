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
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewFragment extends Fragment {

    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_task_overview, container, false);


        LocalDatabaseHelper ldb = new LocalDatabaseHelper(getActivity());
        Calendar calender = Calendar.getInstance();
        calender.set(2018, 0, 26);
        ArrayList<SpecificTask> specificTasks = new ArrayList<>();//ldb.specificTasksSortByStartTime(calender);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2000, 0, 01);
        SpecificTask specificTask = new SpecificTask("name", calender, calendar1);
        specificTasks.add(specificTask);

        SpecificTaskOverviewAdapter adapter = new SpecificTaskOverviewAdapter(specificTasks, getActivity());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);


        return rootView;
    }

}
