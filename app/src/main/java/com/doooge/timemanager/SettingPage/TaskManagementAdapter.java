package com.doooge.timemanager.SettingPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;

import java.util.ArrayList;


/**
 * Created by diana on 2018-02-16.
 */

public class TaskManagementAdapter extends BaseAdapter {
    LocalDatabaseHelper ldh;
    private ArrayList<SpecificTask> specificTasks;
    private Context context;


    public TaskManagementAdapter(ArrayList<SpecificTask> specificTasks, LocalDatabaseHelper ldh, Context context) {
        this.specificTasks = specificTasks;
        this.ldh = ldh;
        this.context = context;
    }

    @Override
    public int getCount() {
        return specificTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return specificTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_all_specifictasks_item, viewGroup, false);
        TextView taskName = rowView.findViewById(R.id.taskName);
        SpecificTask specificTask = (SpecificTask) getItem(position);
        taskName.setText(specificTask.getTaskName());

        return rowView;
    }

}
