package com.doooge.timemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewAdapter extends BaseAdapter implements NumberPicker.OnValueChangeListener {

    LocalDatabaseHelper ldh;
    private ArrayList<SpecificTask> specificTasks;
    private Context context;
    private SpecificTask selectedSpecificTask;
    private ArrayList<SpecificTask> completeList;
    private ArrayList<SpecificTask> incompleteList;

    public SpecificTaskOverviewAdapter(ArrayList<SpecificTask> specificTasks, Context context) {

        this.ldh = LocalDatabaseHelper.getInstance(context);
        this.context = context;
        inititalList(specificTasks);
    }

    public void inititalList(ArrayList<SpecificTask> specificTasks) {
        separeteList(specificTasks);
        for (SpecificTask item : incompleteList) {
            this.specificTasks.add(item);
        }
        this.specificTasks.addAll(completeList);
        System.out.println("initial: " + specificTasks.size());
        System.out.println("complete: " + completeList.size());
        System.out.println("incomplete: " + incompleteList.size());
        Iterator a = incompleteList.iterator();
        while (a.hasNext()) {
            System.out.println(((SpecificTask) a.next()).getTaskName());
        }
        notifyDataSetChanged();


    }

    public void separeteList(ArrayList<SpecificTask> specificTasks) {
        this.specificTasks = new ArrayList<>();
        completeList = new ArrayList<>();
        incompleteList = new ArrayList<>();
        System.out.println("=!!!!!!!!!!!!!!!!!====" + specificTasks.size());
        for (SpecificTask item : specificTasks) {
            System.out.println(item.getTaskName() + "===item==");
            if (item.isCompletedInBoolean()) {
                System.out.println(item.getTaskName() + "||||" + item.isCompletedInBoolean());
                completeList.add(item);
            } else {
                System.out.println(item.getTaskName() + "||||" + item.isCompletedInBoolean());
                incompleteList.add(item);
            }

        }

        System.out.println(incompleteList.size() + "!!!!!!!!!!!!!!!!!");

    }

    public void sortList(ArrayList<SpecificTask> specificTasks) {
        Collections.sort(specificTasks, new Comparator<SpecificTask>() {
            @Override
            public int compare(SpecificTask s1, SpecificTask s2) {
                int num2;
                if (s1.getCompareTime() > s2.getCompareTime()) {
                    num2 = 1;
                } else if (s1.getCompareTime() == s2.getCompareTime()) {
                    num2 = 0;
                } else {
                    num2 = -1;
                }
                return num2;
            }

        });
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
        final TextView taskName = rowView.findViewById(R.id.taskName);
        final TextView taskHour = rowView.findViewById(R.id.taskHour);
        final Button taskType = rowView.findViewById(R.id.typeBtn);
        final SpecificTask specificTask = getItem(position);
        int color = Integer.parseInt(specificTask.getType().getColor());
        if (color == viewGroup.getResources().getColor(R.color.violet)) {
            taskType.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_purple));
        } else if (color == -6710836) {
            taskType.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_default));
        } else if (color == viewGroup.getResources().getColor(R.color.green)) {
            taskType.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_green));
        } else if (color == viewGroup.getResources().getColor(R.color.blue)) {
            taskType.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_blue));
        } else if (color == viewGroup.getResources().getColor(R.color.red)) {
            taskType.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_red));
        } else if (color == viewGroup.getResources().getColor(R.color.yellow)) {
            taskType.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_yellow));
        }

        if (specificTask.isCompletedInBoolean() == true) {
            rowView.setBackground(viewGroup.getResources().getDrawable(R.color.task_comp));
            taskName.setTextColor(viewGroup.getResources().getColor(R.color.gray));
            taskHour.setTextColor(viewGroup.getResources().getColor(R.color.gray));
            taskType.setTextColor(viewGroup.getResources().getColor(R.color.gray));
        } else {
            //rowView.setBackground(viewGroup.getResources().getDrawable(R.color.task_incomp));
            taskName.setTextColor(viewGroup.getResources().getColor(R.color.black));
            taskHour.setTextColor(viewGroup.getResources().getColor(R.color.black));
            taskType.setTextColor(viewGroup.getResources().getColor(R.color.black));
        }

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
                inititalList(specificTasks);
                if (specificTask.isCompletedInBoolean()) {// If currently is completed and will be marked as incomplete

                    specificTask.setCompleted(0);

                    taskName.setTextColor(viewGroup.getResources().getColor(R.color.black));
                    taskHour.setTextColor(viewGroup.getResources().getColor(R.color.black));
                    taskType.setTextColor(viewGroup.getResources().getColor(R.color.black));
                    //rowView.setBackground(viewGroup.getResources().getDrawable(R.color.task_incomp));
                    success = ldh.updateSpecificTaskTable(specificTask);
                    completeList.remove(specificTask);
                    incompleteList.add(specificTask);
                    System.out.println("complete: " + completeList.size());
                    System.out.println("incomplete: " + incompleteList.size());
                    sortList(completeList);
                    sortList(incompleteList);
                    specificTasks.clear();
                    boolean a = specificTasks.addAll(incompleteList);
                    boolean b = specificTasks.addAll(completeList);
                    System.out.println("total: " + specificTasks.size());
                    System.out.println("==a==" + a);
                    System.out.println("==b==" + b);
                    notifyDataSetChanged();

                } else {// If currently is incomplete and will be marked as completed
                    specificTask.setCompleted(1);
                    taskName.setTextColor(viewGroup.getResources().getColor(R.color.gray));
                    taskHour.setTextColor(viewGroup.getResources().getColor(R.color.gray));
                    taskType.setTextColor(viewGroup.getResources().getColor(R.color.gray));
                    //rowView.setBackground(viewGroup.getResources().getDrawable(R.color.task_comp));
                    success = ldh.updateSpecificTaskTable(specificTask);
                    completeList.add(specificTask);
                    System.out.println("incomplete??: " + incompleteList.size());
                    System.out.println("index: " + incompleteList.indexOf(specificTask));
                    boolean test = incompleteList.remove(specificTask);
                    System.out.println("test====" + test);
                    System.out.println("incomplete??: " + incompleteList.size());
                    sortList(completeList);
                    sortList(incompleteList);
                    System.out.println("complete: " + completeList.size());
                    System.out.println("incomplete: " + incompleteList.size());
                    specificTasks.clear();
                    specificTasks.addAll(incompleteList);
                    specificTasks.addAll(completeList);
                    System.out.println("total: " + specificTasks.size());
                    notifyDataSetChanged();
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
        specificTasks.clear();
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

