package com.doooge.timemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SpecificTask> specificTasks;
    private LayoutInflater mInflater;
    private Context context;

    public SpecificTaskOverviewAdapter(ArrayList<SpecificTask> specificTasks, Context context) {
        this.specificTasks = specificTasks;
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
        //Get view for row item
        View rowView = LayoutInflater.from(context).inflate(R.layout.activity_daily_task_list, viewGroup, false);
        TextView taskName = rowView.findViewById(R.id.taskName);
        TextView taskHour = rowView.findViewById(R.id.taskHour);

        getItem(position);
        //   ArrayList<SpecificTask> specificTasks = (ArrayList<SpecificTask>) getItem(position);
        taskName.setText("aaa");
        taskHour.setText("111");


        return rowView;
    }

}

