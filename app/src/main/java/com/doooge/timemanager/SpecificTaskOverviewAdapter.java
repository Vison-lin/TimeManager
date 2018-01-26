package com.doooge.timemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SpecificTask> specificTasks;

    public SpecificTaskOverviewAdapter(ArrayList<SpecificTask> specificTasks) {
        this.specificTasks = specificTasks;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}

