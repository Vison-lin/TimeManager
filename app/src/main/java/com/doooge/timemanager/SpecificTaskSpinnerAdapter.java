package com.doooge.timemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by diana on 2018-01-31.
 */

public class SpecificTaskSpinnerAdapter extends BaseAdapter {
    private ArrayList<Type> tasks;
    private Context context;
    private LocalDatabaseHelper ldh;


    public SpecificTaskSpinnerAdapter(ArrayList<Type> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
        this.ldh = LocalDatabaseHelper.getInstance(context);
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

        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.taskcreator_spinner_item, viewGroup, false);

        final Type task = getItem(position);

        TextView taskName = rowView.findViewById(R.id.itemName);
        taskName.setSingleLine(true);
        taskName.setText(task.getName());
        ImageView taskTypeBlock = rowView.findViewById(R.id.itemColor);
        int color = Integer.parseInt(task.getColor());
        taskTypeBlock.setBackgroundColor(color);


//        taskTypeBlock.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//
//                Intent intent = new Intent(viewGroup.getContext(), SpecificTaskCreator.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("givenTask", task);
//                viewGroup.getContext().startActivity(intent);
//            }
//        });

//        taskTypeBlock.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // Instantiate an AlertDialog.Builder with its constructor
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                // Chain together various setter methods to set the dialog characteristics
//                builder.setMessage(R.string.quick_access_delete_predefined_task_confirm_message);
//
//                // Add the buttons
//                builder.setPositiveButton(R.string.quick_access_confirm_delete_pre_defined_task, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        ldh.deleteTaskTable(task.getId());
//                        dialog.dismiss();
//                        tasks.remove(task);//delete from local syn
//                        notifyDataSetChanged();
//                    }
//                });
//                builder.setNegativeButton(R.string.quick_access_not_delete_pre_defined_task, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });
//
//                // Get the AlertDialog from create()
//                AlertDialog dialog = builder.create();
//                dialog.show();
//                return false;
//            }
//        });

        return rowView;
    }

}
