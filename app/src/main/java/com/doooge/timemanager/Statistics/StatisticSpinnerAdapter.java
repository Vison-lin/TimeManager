package com.doooge.timemanager.Statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.Type;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-31.
 */

public class StatisticSpinnerAdapter extends BaseAdapter {
    ArrayList<Type> selectList;
    private ArrayList<Type> tasks;
    private Context context;
    private LocalDatabaseHelper ldh;


    public StatisticSpinnerAdapter(ArrayList<Type> tasks, ArrayList<Type> selectList, Context context) {
        this.tasks = tasks;
        this.context = context;
        this.ldh = LocalDatabaseHelper.getInstance(context);
        this.selectList = selectList;
        System.out.println("!!!ss!!!!" + selectList.size());
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Type getItem(int position) {

        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, final ViewGroup viewGroup) {

        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistic_page_spinner, viewGroup, false);

        final Type task = getItem(position);

        TextView typeName = rowView.findViewById(R.id.typeName);
        typeName.setText(task.getName());
        ImageView taskTypeBlock = rowView.findViewById(R.id.typeBtn);
        int color = Integer.parseInt(task.getColor());
        taskTypeBlock.setBackgroundColor(color);
        final CheckBox cb = rowView.findViewById(R.id.cb_select);

        for (Type i : selectList) {
            if (i.getName().equals(task.getName())) {
                cb.setChecked(true);
            }
        }

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cb.isChecked()) {
                    cb.setChecked(false);
                    selectList.remove(task);


                } else {
                    cb.setChecked(true);
                    selectList.add(task);


                }

            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb.isChecked()) {
                    cb.setChecked(false);
                    selectList.remove(task);


                } else {
                    cb.setChecked(true);
                    selectList.add(task);


                }


            }
        });


        return rowView;
    }

}
