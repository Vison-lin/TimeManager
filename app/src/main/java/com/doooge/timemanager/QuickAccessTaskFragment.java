package com.doooge.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.doooge.timemanager.SettingPage.SettingActivity;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-27.
 */

public class QuickAccessTaskFragment extends Fragment implements View.OnClickListener {
    private ListView mListView;
    private QuickAccessTaskAdapter adapter;
    private ArrayList<Task> tasks;
    private LocalDatabaseHelper ldh;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_quick_access_task, container, false);

        ldh = LocalDatabaseHelper.getInstance(getContext());
        ImageView settingBtn = rootView.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(this);

        tasks = ldh.getAllTask();
        adapter = new QuickAccessTaskAdapter(tasks, getContext());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);

        return rootView;
    }

    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

}

