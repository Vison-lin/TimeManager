package com.doooge.timemanager;

import android.content.Context;
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
    private Context mContext;
    private ArrayList<SpecificTask> specificTasks;


    public QuickAccessTaskAdapter(ArrayList<SpecificTask> specificTasks) {
        this.specificTasks = specificTasks;
    }

    @Override
    public int getCount() {
        return specificTasks.size();
    }

    @Override
    public SpecificTask getItem(int position) {

        return specificTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_task_item, viewGroup, false);

        Button taskTypeBlock = rowView.findViewById(R.id.taskTypeBtn);
        System.out.println("====" + (taskTypeBlock.equals(null)));

        //TODO To be deleted: Facked Type
        //================================
        taskTypeBlock.setText("Jogging");
        //================================

        taskTypeBlock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //TODO Correct
                Intent intent = new Intent(viewGroup.getContext(), SpecificTaskCreator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewGroup.getContext().startActivity(intent);
            }
        });

        return rowView;
    }

}
