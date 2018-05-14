package com.doooge.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-27.
 */

public class QuickAccessTaskFragment extends Fragment {
    private ListView mListView;
    private QuickAccessTaskAdapter adapter;
    private ArrayList<Task> tasks;
    private LocalDatabaseHelper ldh;
    private TextView pageTitle;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_quick_access_task, container, false);

        ldh = LocalDatabaseHelper.getInstance(getContext());
        pageTitle = rootView.findViewById(R.id.activityTitleText);
        //pageTitle.setBackground(getResources().getDrawable(R.drawable.title3));

        tasks = ldh.getAllTask();
        adapter = new QuickAccessTaskAdapter(tasks, getContext());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);
        ImageView viewAddTasksBtn = rootView.findViewById(R.id.addingTaskBtn);
        viewAddTasksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SpecificTaskCreator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }
        });




        return rootView;


    }

}

