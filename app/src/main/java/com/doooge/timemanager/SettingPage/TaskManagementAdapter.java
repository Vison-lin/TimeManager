package com.doooge.timemanager.SettingPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;
import com.doooge.timemanager.SpecificTaskCreator;

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
    public View getView(int position, View view, final ViewGroup viewGroup) {
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_all_specifictasks_item, viewGroup, false);
        TextView taskName = rowView.findViewById(R.id.taskName);
        final SpecificTask specificTask = (SpecificTask) getItem(position);
        taskName.setText(specificTask.getTaskName());

        taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewGroup.getContext(), SpecificTaskCreator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("givenSpecificTask", specificTask);
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
