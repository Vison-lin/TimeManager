package com.doooge.timemanager.SettingPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by diana on 2018-02-16.
 */

public class TaskManagementActivity extends AppCompatActivity {
    private ListView mListView;
    private TaskManagementAdapter adapter;
    private ArrayList<SpecificTask> specificTasks;
    private LocalDatabaseHelper ldh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_specifictasks);

        ldh = new LocalDatabaseHelper(this);
        specificTasks = ldh.getAllSpecificTask();

        adapter = new TaskManagementAdapter(specificTasks, ldh, this);
        mListView = findViewById(R.id.taskList);
        mListView.setAdapter(adapter);

        specificTasks = ldh.getAllSpecificTask();
        finishAll();
    }

    public void finishAll() {
        Iterator<SpecificTask> iterator = specificTasks.iterator();
        while (iterator.hasNext()) {
            SpecificTask data = iterator.next();
            System.out.println(data.getTaskName());
        }
    }

}
