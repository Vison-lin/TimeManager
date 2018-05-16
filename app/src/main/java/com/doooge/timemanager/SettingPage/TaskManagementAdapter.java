package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;
import com.doooge.timemanager.SpecificTaskCreator;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by diana on 2018-02-16.
 */

public class TaskManagementAdapter extends BaseAdapter {
    private ArrayList<SpecificTask> specificTasks;


    public TaskManagementAdapter(ArrayList<SpecificTask> specificTasks) {
        this.specificTasks = specificTasks;
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
    public View getView(int position, View view, final ViewGroup viewGroup) {
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_all_specifictasks_item, viewGroup, false);
        TextView taskName = rowView.findViewById(R.id.taskName);
        TextView taskHour = rowView.findViewById(R.id.taskHour);
        Button taskType = rowView.findViewById(R.id.typeBtn);
        final SpecificTask specificTask = (SpecificTask) getItem(position);
        int color = Integer.parseInt(specificTask.getType().getColor());

        taskType.setBackgroundColor(color);

        taskName.setText(specificTask.getTaskName());

        if (specificTask.getType().getName().length() <= 1) {
            taskType.setText(String.format("%s", specificTask.getType().getName().substring(0, 1)));
        } else {
            taskType.setText(String.format("%s", specificTask.getType().getName().substring(0, 2)));
        }
        if (specificTask.getTaskName().length() >= 20) {
            taskName.setText(String.format("%s", specificTask.getTaskName().substring(0, 10) + "..."));
        }
        Calendar start = specificTask.getStartTime();
        Calendar end = specificTask.getEndTime();
        String display = (start.get(Calendar.MONTH) + 1) + "." + start.get(Calendar.DAY_OF_MONTH) + " " + start.get(Calendar.HOUR_OF_DAY) + ":" + start.get(Calendar.MINUTE) +
                " - " + (end.get(Calendar.MONTH) + 1) + "." + end.get(Calendar.DAY_OF_MONTH) + " " + end.get(Calendar.HOUR_OF_DAY) + ":" + end.get(Calendar.MINUTE);
        taskHour.setText(display);


        taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewGroup.getContext(), SpecificTaskCreator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("givenSpecificTask", specificTask);
                intent.putExtra("taskManagement","taskManagement");
                viewGroup.getContext().startActivity(intent);
            }
        });

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
