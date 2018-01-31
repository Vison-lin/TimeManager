package com.doooge.timemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-27.
 */

public class QuickAccessTaskFragment extends Fragment {
    private ListView mListView;
    private QuickAccessTaskAdapter adapter;
    //private Button mButton;

    private ArrayList<SpecificTask> specificTasks;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_quick_access_task, container, false);

        mListView = (ListView) rootView.findViewById(R.id.taskTypeBtn);

        adapter = new QuickAccessTaskAdapter(specificTasks);
        mListView.setAdapter(adapter);
        //mButton = (Button) rootView.findViewById(R.id.taskTypeBtn);
        return rootView;
    }

}

