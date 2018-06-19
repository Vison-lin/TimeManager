package com.doooge.timemanager;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewAdapter extends BaseAdapter implements NumberPicker.OnValueChangeListener {

    private final Button bt_cancel;
    private final Button bt_delete;
    LocalDatabaseHelper ldh;
    private ArrayList<SpecificTask> specificTasks;
    private Context context;
    private SpecificTask selectedSpecificTask;
    private ArrayList<SpecificTask> completeList;
    private ArrayList<SpecificTask> incompleteList;
    private ArrayList<SpecificTask> list_delete = new ArrayList<SpecificTask>();
    private boolean isMultiSelect = false;
    private TextView tv_sum;

    public SpecificTaskOverviewAdapter(final ArrayList<SpecificTask> specificTask, final Context context, int position, ViewGroup deletView) {
        this.ldh = LocalDatabaseHelper.getInstance(context);
        this.context = context;
        inititalList(specificTask);
        bt_cancel = deletView.findViewById(R.id.bt_cancel);
        bt_delete = deletView.findViewById(R.id.bt_delete);
        btnDisplayModification(bt_cancel,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                deletView.getResources().getColor(R.color.statpage_blue),
                deletView.getResources().getColor(R.color.background_color)
        );
        btnDisplayModification(
                bt_delete,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                deletView.getResources().getColor(R.color.statpage_blue),
                deletView.getResources().getColor(R.color.background_color)
        );
        tv_sum = deletView.findViewById(R.id.tv_sum);


        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isMultiSelect = false;
                bt_cancel.setVisibility(View.INVISIBLE);
                bt_delete.setVisibility(View.INVISIBLE);
                tv_sum.setVisibility(View.INVISIBLE);
                list_delete.clear();
                notifyDataSetChanged();

            }
        });


        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_delete.size() != 0 && isMultiSelect) {
                    for (SpecificTask task : list_delete) {
                        ldh.deleteSpecificTaskTable(task.getId());
                        specificTasks.remove(task);
                        tv_sum.setText("You have chooseed: 0 item.");

                    }
                    list_delete.clear();
                    if (specificTasks.size() == 0) {
                        isMultiSelect = false;
                        bt_cancel.setVisibility(View.INVISIBLE);
                        bt_delete.setVisibility(View.INVISIBLE);
                        tv_sum.setVisibility(View.INVISIBLE);
                    }
                    notifyDataSetChanged();
                }
            }
        });


    }

    public void inititalList(ArrayList<SpecificTask> specificTasks) {
        separeteList(specificTasks);
        for (SpecificTask item : incompleteList) {
            this.specificTasks.add(item);
        }
        this.specificTasks.addAll(completeList);
//        System.out.println("initial: " + specificTasks.size());
//        System.out.println("complete: " + completeList.size());
//        System.out.println("incomplete: " + incompleteList.size());
//        Iterator a = incompleteList.iterator();
//        while (a.hasNext()) {
//            System.out.println(((SpecificTask) a.next()).getTaskName());
//        }
        notifyDataSetChanged();


    }

    public void separeteList(ArrayList<SpecificTask> specificTasks) {
        this.specificTasks = new ArrayList<>();
        completeList = new ArrayList<>();
        incompleteList = new ArrayList<>();
//        System.out.println("=!!!!!!!!!!!!!!!!!====" + specificTasks.size());
        for (SpecificTask item : specificTasks) {
//            System.out.println(item.getTaskName() + "===item==");
            if (item.isCompletedInBoolean()) {
//                System.out.println(item.getTaskName() + "||||" + item.isCompletedInBoolean());
                completeList.add(item);
            } else {
//                System.out.println(item.getTaskName() + "||||" + item.isCompletedInBoolean());
                incompleteList.add(item);
            }

        }

//        System.out.println(incompleteList.size() + "!!!!!!!!!!!!!!!!!");

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

        taskName.setSingleLine(true);

        taskHour.setSingleLine(true);


        final SpecificTask specificTask = getItem(position);
        int color = Integer.parseInt(specificTask.getType().getColor());
        taskType.setBackgroundColor(color);

        taskType.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isMultiSelect = true;
                //deletLayout.setVisibility(view.VISIBLE);
                notifyDataSetChanged();


                return false;
            }
        });

        if (isMultiSelect) {
            final CheckBox cb = rowView.findViewById(R.id.cb_select);
            tv_sum.setText("You have chosen: 0 item.");
            bt_cancel.setVisibility(View.VISIBLE);
            bt_delete.setVisibility(View.VISIBLE);
            tv_sum.setVisibility(View.VISIBLE);


            cb.setVisibility(View.VISIBLE);
            cb.setChecked(false);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!cb.isChecked()) {
                        cb.setChecked(false);
                        list_delete.remove(specificTask);
                    } else {
                        cb.setChecked(true);
                        list_delete.add(specificTask);

                    }
                    tv_sum.setText("You have chosen: " + list_delete.size() + " item.");

                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (cb.isChecked()) {
                        cb.setChecked(false);
                        list_delete.remove(specificTask);
                    } else {
                        cb.setChecked(true);
                        list_delete.add(specificTask);

                    }
                    tv_sum.setText("You have chosen: " + list_delete.size() + " item.");


                }
            });


        } else {

            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectedSpecificTask = null;
                    selectedSpecificTask = specificTask;
                    for (SpecificTask item : specificTasks) {
                        if (item.getTaskName().equals(specificTask.getTaskName())) {
                            specificTasks.remove(item);
                        }
                    }


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
                        success = ldh.updateSpecificTaskTable(specificTask);
                        for (SpecificTask item : completeList) {
                            if (item.getTaskName().equals(specificTask.getTaskName())) {
                                completeList.remove(item);
                            }
                        }

                        incompleteList.add(specificTask);
                        sortList(completeList);
                        sortList(incompleteList);
                        specificTasks.clear();
                        specificTasks.addAll(incompleteList);
                        specificTasks.addAll(completeList);

                        notifyDataSetChanged();

                    } else {// If currently is incomplete and will be marked as completed
                        specificTask.setCompleted(1);
                        taskName.setTextColor(viewGroup.getResources().getColor(R.color.gray));
                        taskHour.setTextColor(viewGroup.getResources().getColor(R.color.gray));
                        taskType.setTextColor(viewGroup.getResources().getColor(R.color.gray));
                        success = ldh.updateSpecificTaskTable(specificTask);
                        completeList.add(specificTask);
                        for (SpecificTask item : incompleteList) {
                            if (item.getTaskName().equals(specificTask.getTaskName())) {
                                incompleteList.remove(item);
                            }
                        }
                        sortList(completeList);
                        sortList(incompleteList);
                        specificTasks.clear();
                        specificTasks.addAll(incompleteList);
                        specificTasks.addAll(completeList);
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


        }

        if (specificTask.isCompletedInBoolean() == true) {
            //rowView.setBackground(viewGroup.getResources().getDrawable(R.color.task_comp));
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
        String display = (start.get(Calendar.MONTH) + 1) + "." + start.get(Calendar.DAY_OF_MONTH) + " ";
        if (start.get(Calendar.HOUR_OF_DAY) == 0) {
            display += "00";
        } else {
            display += start.get(Calendar.HOUR_OF_DAY) + "";
        }
        display += ":";
        if (start.get(Calendar.MINUTE) == 0) {
            display += "00";
        } else {
            display += start.get(Calendar.MINUTE);
        }

        display += " - " + (end.get(Calendar.MONTH) + 1) + "." + end.get(Calendar.DAY_OF_MONTH) + " ";

        if (end.get(Calendar.HOUR_OF_DAY) == 0) {
            display += "00";
        } else {
            display += end.get(Calendar.HOUR_OF_DAY) + "";
        }
        display += ":";
        if (end.get(Calendar.MINUTE) == 0) {
            display += "00";
        } else {
            display += end.get(Calendar.MINUTE);
        }

        taskHour.setText(display);




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
        inititalList(specificTasks);
        this.notifyDataSetChanged();
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Calendar endTime = selectedSpecificTask.getEndTime();
        Calendar startTime = selectedSpecificTask.getStartTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int differInMinutes = CalendarHelper.correctMinutes(numberPicker.getValue());
        long minute = 0;
        try {
            Date d1 = df.parse(startTime.get(Calendar.YEAR) + "-" + (startTime.get(Calendar.MONTH) + 1) + "-" + startTime.get(Calendar.DAY_OF_MONTH) + " " + startTime.get(Calendar.HOUR_OF_DAY) + ":" + startTime.get(Calendar.MINUTE));
            Date d2 = df.parse(endTime.get(Calendar.YEAR) + "-" + (endTime.get(Calendar.MONTH) + 1) + "-" + endTime.get(Calendar.DAY_OF_MONTH) + " " + endTime.get(Calendar.HOUR_OF_DAY) + ":" + endTime.get(Calendar.MINUTE));
            long diff = d2.getTime() - d1.getTime();
            minute = diff / (1000 * 60);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (minute + differInMinutes > 0 && minute + differInMinutes <= 24 * 60) {

            Calendar endCalendar = selectedSpecificTask.getEndTime();
            endCalendar.add(Calendar.MINUTE, differInMinutes);
            selectedSpecificTask.setEndTime(endCalendar);
            ldh.updateSpecificTaskTable(selectedSpecificTask);
            specificTasks.add(selectedSpecificTask);
            inititalList(specificTasks);
            notifyDataSetChanged();
            Toast.makeText(context, "Success !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed, impossible time !", Toast.LENGTH_SHORT).show();
        }
    }

    private void btnDisplayModification(Button button, int layoutParamsWidth, int startColor, int endColor) {
        GradientDrawable btnDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{startColor, endColor});
        btnDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        btnDrawable.setGradientRadius(210.0f);
        btnDrawable.setCornerRadius(50.f);
        button.setBackground(btnDrawable);
        button.setWidth(layoutParamsWidth);
        button.setHeight(button.getHeight() - 10);
    }

}

