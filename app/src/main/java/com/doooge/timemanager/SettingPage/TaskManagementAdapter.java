package com.doooge.timemanager.SettingPage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;
import com.doooge.timemanager.SpecificTaskCreator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by diana on 2018-02-16.
 */

public class TaskManagementAdapter extends BaseAdapter {
    private final Button bt_cancel_task;
    private final Button bt_delete_task;
    LocalDatabaseHelper ldh;
    private ArrayList<SpecificTask> specificTasks;
    private ArrayList<SpecificTask> list_delete = new ArrayList<SpecificTask>();
    private boolean isMultiSelect = false;
    private TextView tv_sum_task;


    public TaskManagementAdapter(ArrayList<SpecificTask> specificTask, Context context, HashMap delete) {
        this.ldh = LocalDatabaseHelper.getInstance(context);
        this.specificTasks = specificTask;
//        LayoutInflater factory = LayoutInflater.from(deleteView);
//        View delete = factory.inflate(R.layout.activity_all_specifictasks,null);
        bt_cancel_task = (Button) delete.get("cancel");
        bt_delete_task = (Button) delete.get("delete");
        btnDisplayModification(
                bt_cancel_task,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) delete.get("startColor"),
                (int) delete.get("endColor")

        );
        btnDisplayModification(
                bt_delete_task,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) delete.get("startColor"),
                (int) delete.get("endColor")
        );
        tv_sum_task = (TextView) delete.get("text");


        bt_cancel_task.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isMultiSelect = false;
                bt_cancel_task.setVisibility(View.INVISIBLE);
                bt_delete_task.setVisibility(View.INVISIBLE);
                tv_sum_task.setVisibility(View.INVISIBLE);
                list_delete.clear();
                notifyDataSetChanged();

            }
        });


        bt_delete_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_delete.size() != 0 && isMultiSelect) {
                    for (SpecificTask task : list_delete) {
                        ldh.deleteSpecificTaskTable(task.getId());
                        specificTasks.remove(task);
                        tv_sum_task.setText("You have chooseed: 0 item.");

                    }
                    list_delete.clear();
                    if (specificTasks.size() == 0) {
                        isMultiSelect = false;
                        bt_cancel_task.setVisibility(View.INVISIBLE);
                        bt_delete_task.setVisibility(View.INVISIBLE);
                        tv_sum_task.setVisibility(View.INVISIBLE);
                    }
                    notifyDataSetChanged();
                }
            }
        });
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
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_daily_task_list, viewGroup, false);
        TextView taskName = rowView.findViewById(R.id.taskName);
        TextView taskHour = rowView.findViewById(R.id.taskHour);
        Button taskType = rowView.findViewById(R.id.typeBtn);
        final SpecificTask specificTask = (SpecificTask) getItem(position);
        int color = Integer.parseInt(specificTask.getType().getColor());

        taskType.setBackgroundColor(color);

        taskName.setText(specificTask.getTaskName());

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
        //String display = (start.get(Calendar.MONTH) + 1) + "." + start.get(Calendar.DAY_OF_MONTH) + " " + start.get(Calendar.HOUR_OF_DAY) + ":" + start.get(Calendar.MINUTE) +
        //      " - " + (end.get(Calendar.MONTH) + 1) + "." + end.get(Calendar.DAY_OF_MONTH) + " " + end.get(Calendar.HOUR_OF_DAY) + ":" + end.get(Calendar.MINUTE);

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

        if (isMultiSelect) {
            final CheckBox cb = rowView.findViewById(R.id.cb_select);
            if (list_delete.size() == 0) {
                tv_sum_task.setText("You have chosen: 0 item.");
            } else {
                tv_sum_task.setText("You have chosen: " + list_delete.size() + " item.");
            }
            bt_cancel_task.setVisibility(View.VISIBLE);
            bt_delete_task.setVisibility(View.VISIBLE);
            tv_sum_task.setVisibility(View.VISIBLE);


            cb.setVisibility(View.VISIBLE);
            cb.setChecked(false);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("!!!!" + cb.isChecked());
                    if (!cb.isChecked()) {
                        cb.setChecked(false);
                        list_delete.remove(specificTask);
                    } else {
                        cb.setChecked(true);
                        list_delete.add(specificTask);

                    }
                    tv_sum_task.setText("You have chooseed: " + list_delete.size() + " item.");

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
                    tv_sum_task.setText("You have chooseed: " + list_delete.size() + " item.");


                }
            });


        } else {


            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(viewGroup.getContext(), SpecificTaskCreator.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("givenSpecificTask", specificTask);
                    intent.putExtra("taskManagement", "taskManagement");
                    viewGroup.getContext().startActivity(intent);


                }
            });

            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    isMultiSelect = true;
                    notifyDataSetChanged();
                    return false;
                }
            });


        }

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
