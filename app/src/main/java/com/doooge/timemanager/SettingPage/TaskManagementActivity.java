package com.doooge.timemanager.SettingPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;
import com.doooge.timemanager.Type;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManagementActivity extends AppCompatActivity {
    private ListView mListView;
    private TaskManagementAdapter adapter;
    private ArrayList<SpecificTask> specificTasks;
    private LocalDatabaseHelper ldh;
    private Spinner mSpinner;
    private Type type;
    private HashMap<String, Object> viewGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_specifictasks);
        ldh = LocalDatabaseHelper.getInstance(this);
        type = (Type)getIntent().getSerializableExtra("TypeInfo");

        if (type != null) {
            specificTasks = ldh.findSpecificTasksByTypes(type);
        }else {
            specificTasks = ldh.getAllSpecificTask();
        }

        Button cancel = findViewById(R.id.bt_all_cancel);
        Button delete = findViewById(R.id.bt_all_delete);
        TextView tv = findViewById(R.id.tv_all_sum);
        viewGroup = new HashMap<String, Object>();
        viewGroup.put("cancel", cancel);
        viewGroup.put("delete", delete);
        viewGroup.put("text", tv);
        viewGroup.put("startColor", getResources().getColor(R.color.statpage_blue));
        viewGroup.put("endColor", getResources().getColor(R.color.background_color));
        adapter = new TaskManagementAdapter(specificTasks, TaskManagementActivity.this, viewGroup);
        mListView = findViewById(R.id.taskList);
        mListView.setAdapter(adapter);
        specificTasks = ldh.getAllSpecificTask();
        spinner(adapter);
    }

    private void spinner(final TaskManagementAdapter adapter) {
        mSpinner = findViewById(R.id.taskManagement_spinner);
        ArrayList<String> mList = new ArrayList<String>();
        final ArrayList<Type> typeList = ldh.getAllType();
        mList.add("show all types");

        for (Type i : typeList) {
            mList.add(i.getName());
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                /* 将所选mySpinner 的值带入myTextView 中*/
                if (arg2 == 0) {//"Show all types"
                    specificTasks.clear();
                    specificTasks = new ArrayList<SpecificTask>();
                    specificTasks = ldh.getAllSpecificTask();
                } else {
                    Type selectType = typeList.get(arg2 - 1);//because "Show all types" is not in the typeList
                    specificTasks.clear();
                    specificTasks = new ArrayList<SpecificTask>();
                    specificTasks = ldh.findSpecificTasksByTypes(selectType);
                }
                adapter.updateSpecificTaskOverviewAdapter(specificTasks);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
    }
}
