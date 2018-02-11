package com.doooge.timemanager;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.StringTokenizer;


public class SpecificTaskCreator extends AppCompatActivity {

    private static String startTime;
    private static String endTime;
    public TimeBarView mView;
    private MyHandler handler;
    private String userName;
    private LocalDatabaseHelper ldh = new LocalDatabaseHelper(this);
    private CheckBox checkBox;
    private Context context;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        //Going from QuickAccessTask
        // Task task = (Task) getIntent().getSerializableExtra("givenTask");
        //Going from SpecificTaskOverViewAdapter
        SpecificTask specificTask = (SpecificTask) getIntent().getSerializableExtra("givenSpecificTask");
        setContentView(R.layout.taskcreator);

        handler = new MyHandler(this);
        mView = new TimeBarView(this);
        mView.Test(new TimeBarView.Callback() {


            @Override
            public Handler execute() {
                return handler;
            }
        });
        Button submit = findViewById(R.id.submitButton);
        Button delete = findViewById(R.id.deleteButton);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setVisibility(View.INVISIBLE);

        if (specificTask == null) {//true only if user creates a new SpecificTask
            delete.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
        } else {//Users are from Main page
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO DELETE
                }
            });
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
                Intent intent = new Intent(context, MainPageSlidesAdapter.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
    }

    private void addTask(View view) {
        EditText taskName = findViewById(R.id.taskName);
        userName = String.valueOf(taskName.getText());
        if (userName.equals("")) {
            taskName.setError("Enter a name.");
            taskName.setBackground(getResources().getDrawable(R.drawable.back_red));
        } else {
            String[] start = startTime.split(":");
            String[] end = endTime.split(":");

            Calendar calStart = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();
            calStart.set(2017, 0, 31, Integer.parseInt(start[0]), Integer.parseInt(start[1]));
            calEnd.set(2017, 0, 31, Integer.parseInt(end[0]), Integer.parseInt(end[1]));
            SpecificTask specificTask = new SpecificTask(userName, calStart, calEnd);
            ldh.insertToSpecificTaskTable(specificTask);
            Type type = new Type("", "");
            ldh.insertToTypeTable(type);

            //Add to Task table if user selected the checkBox

            if ((checkBox.getVisibility() == View.VISIBLE) && checkBox.isChecked()) {
                Task task = specificTask;
                ldh.insertToTaskTable(task);
            }

        }
    }

    /**
     * To create Handler class for receiveing data from TimeBarView class.
     */

    private static class MyHandler extends Handler {
        Activity activity;

        private MyHandler(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                String message = (String) msg.obj;
                TextView start = activity.findViewById(R.id.startTimePrint);
                TextView end = activity.findViewById(R.id.endTimePrint);
                StringTokenizer token = new StringTokenizer(message, "@");
                startTime = token.nextToken();
                endTime = token.nextToken();
                start.setText(startTime);
                end.setText(endTime);
            }
        }

    }

//    public void checkBoxSelection(View view){
//        CheckBox checkBox = (CheckBox) view;
//        if (checkBox.isSelected()){
//
//        }
//    }


}
