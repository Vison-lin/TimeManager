package com.doooge.timemanager;

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

    private ArrayList<SpecificTask> specificTasks;


    public SpecificTaskOverviewAdapter(ArrayList<SpecificTask> specificTasks) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        //Get view for row item
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_daily_task_list, viewGroup, false);
        TextView taskName = rowView.findViewById(R.id.taskName);
        TextView taskHour = rowView.findViewById(R.id.taskHour);
        SpecificTask specificTask = getItem(position);
        taskName.setText(specificTask.getTaskName());
        taskHour.setText("111");//TODO TO BE IMPLEMENTED
        return rowView;
    }

    /**
     * This method is used for updating Adapter's view. One should call this method right after one changed the content.
     *
     * @param newSpecificTasks pass a NEW ArrayList with all new elements that need to display on the screen.
     */
    public void updateSpecificTaskOverviewAdapter(ArrayList<SpecificTask> newSpecificTasks) {
        specificTasks = new ArrayList<>(newSpecificTasks);
        this.notifyDataSetChanged();
    }
}

