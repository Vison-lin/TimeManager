package com.doooge.timemanager;

import android.content.Context;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_quick_access_task, viewGroup, false);

        Button taskTypeBlock = rowView.findViewById(R.id.taskTypeBtn);

        return rowView;
    }

    public void updateQuickAccessTaskAdapter(ArrayList<SpecificTask> newSpecificTasks) {
        specificTasks = new ArrayList<>(newSpecificTasks);
        this.notifyDataSetChanged();
    }

}
