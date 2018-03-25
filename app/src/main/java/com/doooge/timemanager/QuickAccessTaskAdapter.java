package com.doooge.timemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private ArrayList<Task> tasks;
    private Context context;
    private LocalDatabaseHelper ldh;


    public QuickAccessTaskAdapter(ArrayList<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
        this.ldh = LocalDatabaseHelper.getInstance(context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {

        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, final ViewGroup viewGroup) {

        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_task_item, viewGroup, false);

        final Task task = getItem(position);

        Button taskTypeBlock = rowView.findViewById(R.id.taskTypeBtn);
        int color =Integer.parseInt(task.getType().getColor());
        if(color==viewGroup.getResources().getColor(R.color.violet)){
            taskTypeBlock.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_purple));
        }else if(color==-1){
            taskTypeBlock.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_default));
        }else if(color==viewGroup.getResources().getColor(R.color.green)){
            taskTypeBlock.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_green));
        }else if(color==viewGroup.getResources().getColor(R.color.blue)){
            taskTypeBlock.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_blue));
        }else if(color==viewGroup.getResources().getColor(R.color.red)){
            taskTypeBlock.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_red));
        }else if(color==viewGroup.getResources().getColor(R.color.yellow)){
            taskTypeBlock.setBackground(viewGroup.getResources().getDrawable(R.drawable.btn_bkgd_yellow));
        }


        taskTypeBlock.setText(task.getTaskName());

        taskTypeBlock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(viewGroup.getContext(), SpecificTaskCreator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("givenTask", task);
                viewGroup.getContext().startActivity(intent);
            }
        });

        taskTypeBlock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.quick_access_delete_predefined_task_confirm_message);

                // Add the buttons
                builder.setPositiveButton(R.string.quick_access_confirm_delete_pre_defined_task, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ldh.deleteTaskTable(task.getId());
                        dialog.dismiss();
                        tasks.remove(task);//delete from local syn
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.quick_access_not_delete_pre_defined_task, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                // Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        return rowView;
    }

}
