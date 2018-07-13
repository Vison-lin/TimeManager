package com.doooge.timemanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-31.
 */

public class QuickAccessTaskAdapter extends BaseAdapter {
    private ArrayList<Task> tasks;


    QuickAccessTaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {

        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, final ViewGroup viewGroup) {

        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_task_item, viewGroup, false);

        final Task task = getItem(position);

        Button taskTypeBlock = rowView.findViewById(R.id.taskTypeBtn);
        int color =Integer.parseInt(task.getType().getColor());
        taskTypeBlock.setBackgroundColor(color);



        taskTypeBlock.setText(task.getTaskName());

        taskTypeBlock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(viewGroup.getContext(), SpecificTaskCreator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("givenTask", task);
                viewGroup.getContext().startActivity(intent);
            }
        });

        return rowView;
    }

}
