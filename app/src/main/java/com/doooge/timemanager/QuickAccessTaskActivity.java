package com.doooge.timemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by diana on 2018-01-27.
 */

public class QuickAccessTaskActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        mListView = (ListView) findViewById(R.id.taskList);
    }
}
