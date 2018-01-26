package com.doooge.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.doooge.timemanager.SettingPage.SettingActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewActivity extends AppCompatActivity {
    private final String UP_NAVIGATION_EXTRA = "UP_NAVIGATION";

    private ListView mListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        mListView = (ListView) findViewById(R.id.taskList);
    }

    protected void onResume() {
        super.onResume();
        LocalDatabaseHelper ldb = new LocalDatabaseHelper(this);
        Calendar calender = Calendar.getInstance();
        calender.set(2018, 0, 26);
        ArrayList<SpecificTask> specificTasks = new ArrayList<>();//ldb.specificTasksSortByStartTime(calender);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2000, 0, 01);
        SpecificTask specificTask = new SpecificTask("name", calender, calendar1);
        specificTasks.add(specificTask);
        // ldb.showAllData(this);

        SpecificTaskOverviewAdapter adapter = new SpecificTaskOverviewAdapter(specificTasks);
        mListView.setAdapter(adapter);

    }

    public void goSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

  /*
    public void createTask(View view)
    {
    }

  */


}
