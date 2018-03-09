package com.doooge.timemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewAdapter extends BaseAdapter implements NumberPicker.OnValueChangeListener {

    LocalDatabaseHelper ldh;
    private ArrayList<SpecificTask> specificTasks;
    private Context context;
    private SpecificTask selectedSpecificTask;

    public SpecificTaskOverviewAdapter(ArrayList<SpecificTask> specificTasks, Context context) {
        this.specificTasks = specificTasks;
        this.ldh = LocalDatabaseHelper.getInstance(context);
        this.context = context;
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
    public View getView(int position, View view, final ViewGroup viewGroup) {
        //Get view for row item
        final View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_daily_task_list, viewGroup, false);
        TextView taskName = rowView.findViewById(R.id.taskName);
        TextView taskHour = rowView.findViewById(R.id.taskHour);
        final SpecificTask specificTask = getItem(position);
        taskName.setText(specificTask.getTaskName());
        Calendar start = specificTask.getStartTime();
        Calendar end = specificTask.getEndTime();
        String display = (start.get(Calendar.MONTH)+1) + "." + start.get(Calendar.DAY_OF_MONTH) + " " + start.get(Calendar.HOUR_OF_DAY) + ":" + start.get(Calendar.MINUTE) +
                " - " + (end.get(Calendar.MONTH)+1) + "." + end.get(Calendar.DAY_OF_MONTH) + " " + end.get(Calendar.HOUR_OF_DAY) + ":" + end.get(Calendar.MINUTE);
        taskHour.setText(display);

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedSpecificTask = null;
                selectedSpecificTask = specificTask;
                specificTasks.remove(specificTask);

                NumberPickerDialog newFragment = new NumberPickerDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("givenSpecificTask", specificTask);
                newFragment.setArguments(bundle);
                newFragment.setValueChangeListener(SpecificTaskOverviewAdapter.this);
                newFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "time picker");

                return false;

            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success;
                if (specificTask.isCompletedInBoolean()) {// If currently is completed and will be marked as incomplete
                    specificTask.setCompleted(0);
                    success = ldh.updateSpecificTaskTable(specificTask);
                } else {// If currently is incomplete and will be marked as completed
                    specificTask.setCompleted(1);
                    success = ldh.updateSpecificTaskTable(specificTask);
                }
                if (!success) {
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

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

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        int differInMinutes = CalendarHelper.correctMinutes(numberPicker.getValue());
        Calendar endCalendar = selectedSpecificTask.getEndTime();
        endCalendar.add(Calendar.MINUTE, differInMinutes);
        selectedSpecificTask.setEndTime(endCalendar);
        ldh.updateSpecificTaskTable(selectedSpecificTask);
        specificTasks.add(selectedSpecificTask);
        notifyDataSetChanged();
    }



}

