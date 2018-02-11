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
    private static boolean isOverDay;
    public TimeBarView mView;
    private MyHandler handler;
    private String userName;
    private LocalDatabaseHelper ldh = new LocalDatabaseHelper(this);
    private TimePickerDialog timePicker;
    private TimePickerDialogInterface timePickerDialogInterface;
    private TextView startDate;
    private TextView endDate;
    private static int year;
    private static int month;
    private static int day;
    private static Calendar calStart;
    private static Calendar calEnd;

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

        endDate = findViewById(R.id.endDatePrint);
        startDate = findViewById(R.id.startDatePrint);


        calStart = Calendar.getInstance();
        calEnd = Calendar.getInstance();
        initialDate();

        startDate.setText(year + ":" + month + ":" + day);
        endDate.setText(year + ":" + month + ":" + day);
        timePickerDialogInterface = new TimePickerDialogInterface() {
            @Override
            public void positiveListener(int year, int month, int day) {
                System.out.println(year + "===" + (month + 1) + "====" + day);
                setYear(year);
                setMonth(month + 1);
                setDay(day);
                startDate.setText(year + ":" + (month + 1) + ":" + day);
                updateEnd();

            }

            @Override
            public void negativeListener() {

            }

            @Override
            public void updateEnd() {
                String[] endlist = endTime.split(":");

                calEnd.set(year, month - 1, day, Integer.parseInt(endlist[0]), Integer.parseInt(endlist[1]));

                if (isOverDay) {
                    calEnd.add(Calendar.DATE, 1);

                    endDate.setText(calEnd.get(Calendar.YEAR) + ":" + (calEnd.get(Calendar.MONTH) + 1) + ":" + calEnd.get(Calendar.DATE));
                }
                if (!isOverDay) {
                    endDate.setText(calEnd.get(Calendar.YEAR) + ":" + (calEnd.get(Calendar.MONTH) + 1) + ":" + calEnd.get(Calendar.DATE));
                }


            }
        };

        timePicker = new TimePickerDialog(this, timePickerDialogInterface);
        handler = new MyHandler(this, timePickerDialogInterface);
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
            }
        });


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.showDatePickerDialog();

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

            SpecificTask specificTask = new SpecificTask(userName, calStart, calEnd);
            ldh.insertToSpecificTaskTable(specificTask);
            Type type = new Type("", "");
            ldh.insertToTypeTable(type);

            //Add to Task table if user selected the checkBox

            if ((checkBox.getVisibility() == View.VISIBLE) && checkBox.isChecked()) {
                Task task = specificTask;
                ldh.insertToTaskTable(task);
            }

            Intent intent = new Intent(context, MainPageSlidesAdapter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        }
    }

    /**
     * To create Handler class for receiveing data from TimeBarView class.
     */

    private static class MyHandler extends Handler{
        private Activity activity;
        private TimePickerDialogInterface c;

        private MyHandler(Activity activity, TimePickerDialogInterface c) {
            this.activity= activity;
            this.c = c;
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                String message = (String) msg.obj;
                TextView start = activity.findViewById(R.id.startTimePrint);
                TextView end = activity.findViewById(R.id.endTimePrint);
                TextView endDate = activity.findViewById(R.id.endDatePrint);
                StringTokenizer token = new StringTokenizer(message, "@");
                startTime = token.nextToken();
                endTime = token.nextToken();
                isOverDay = token.nextToken().equals("true");
                start.setText(startTime);
                end.setText(endTime);
                String[] startlist = startTime.split(":");

                calStart.set(year, month - 1, day, Integer.parseInt(startlist[0]), Integer.parseInt(startlist[1]));
                c.updateEnd();


            }
        }

    }

    public void initialDate() {
        setYear(calStart.get(Calendar.YEAR));
        setMonth(calStart.get(Calendar.MONTH) + 1);
        setDay(calStart.get(Calendar.DATE));
    }


    public void setYear(int year) {
        SpecificTaskCreator.year = year;

    }

    public void setMonth(int month) {
        SpecificTaskCreator.month = month;
    }

    public void setDay(int day) {
        SpecificTaskCreator.day = day;
    }


    public interface TimePickerDialogInterface {
        void positiveListener(int year, int month, int day);

        void negativeListener();

        void updateEnd();
    }




}
