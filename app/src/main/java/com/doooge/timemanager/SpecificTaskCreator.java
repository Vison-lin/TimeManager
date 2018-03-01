package com.doooge.timemanager;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


public class SpecificTaskCreator extends AppCompatActivity {

    private static String startTime;
    private static String endTime;
    private static boolean isOverDay;
    private static int year;
    private static int month;
    private static int day;
    private static Calendar calStart;
    private static Calendar calEnd;
    public TimeBarView mView;
    private MyHandler handler;
    private String userName;
    private LocalDatabaseHelper ldh = new LocalDatabaseHelper(this);
    private TimePickerDialog timePicker;
    private TimePickerDialogInterface timePickerDialogInterface;
    private TextView startDate;
    private TextView endDate;
    private CheckBox checkBox;
    private Context context;
    private Task task;
    private  SpecificTask specificTask;
    private List<Type> typeList;
    private List<String> mList;
    private Spinner mSpinner;
    private ArrayAdapter<String> mAdapter;
    private Type type;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;



        //Going from QuickAccessTask
        task = (Task) getIntent().getSerializableExtra("givenTask");
        //Going from SpecificTaskOverViewAdapter
       specificTask = (SpecificTask) getIntent().getSerializableExtra("givenSpecificTask");
        setContentView(R.layout.taskcreator);

        endDate = findViewById(R.id.endDatePrint);
        startDate = findViewById(R.id.startDatePrint);


        calStart = Calendar.getInstance();
        calEnd = Calendar.getInstance();

        if(specificTask!=null){
            calStart = specificTask.getStartTime();
            calEnd = specificTask.getEndTime();
            String name = specificTask.getTaskName();
            EditText taskName = findViewById(R.id.taskName);
            taskName.setText(name);

        }
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
        if(specificTask!=null){
            int progressStart = (specificTask.getStartTime().get(Calendar.HOUR_OF_DAY)*60)+(specificTask.getStartTime().get(Calendar.MINUTE));

            int progressEnd = (specificTask.getEndTime().get(Calendar.HOUR_OF_DAY)*60)+(specificTask.getEndTime().get(Calendar.MINUTE));
            mView = new TimeBarView(this,progressStart,progressEnd);
        }else {
            mView = new TimeBarView(this);
        }
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

        if (task != null && specificTask == null) {//Users are from QuickAccessTask page
            delete.setVisibility(View.GONE);//GONE: Affect the page format
            checkBox.setVisibility(View.INVISIBLE);
            //TODO Vison: Fill all the related info for that Task
        } else if (specificTask != null && task == null) {//Users are from Main page or TaskManagement page

            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean success = ldh.deleteSpecificTaskTable(specificTask.getId());
                    if (!success) {
                        throw new NoSuchElementException();
                    }
                }
            });

        } else if (specificTask == null && task == null) {//true only if user creates a new SpecificTask
            delete.setVisibility(View.GONE);//GONE: Affect the page format
            checkBox.setVisibility(View.VISIBLE);
        } else {
            throw new IllegalStateException();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 addTask(v);
                //addTaskSecondStyle(v);
            }
        });


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.showDatePickerDialog();

            }
        });

        typeList = ldh.getAllType();
        mList = new ArrayList<String>();
        for(Type i: typeList){
            mList.add(i.getName());

        }
        if(typeList!=null){
            mSpinner = findViewById(R.id.mSpinner);
            mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mList);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(mAdapter);
            mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                /* 将所选mySpinner 的值带入myTextView 中*/
                    type = typeList.get(arg2);
                    TextView typeColor = findViewById(R.id.typeColor);
                    int color = Integer.parseInt(type.getColor());
                    typeColor.getBackground().setColorFilter(new LightingColorFilter(color, color));
                    arg0.setVisibility(View.VISIBLE);
                }
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    // myTextView.setText("NONE");
                    arg0.setVisibility(View.VISIBLE);
                }
            });


        }








    }

    private void addTask(View view) {
        EditText taskName = findViewById(R.id.taskName);
        userName = String.valueOf(taskName.getText());
        if (userName.equals("")) {
            taskName.setError("Enter a name.");
            taskName.setBackground(getResources().getDrawable(R.drawable.back_red));
        } else {

            SpecificTask specificTask = new SpecificTask(userName, calStart, calEnd);
            specificTask.setType(type);
            ldh.insertToSpecificTaskTable(specificTask);

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

    /**
     * To create Handler class for receiveing data from TimeBarView class.
     */

    private static class MyHandler extends Handler {
        private Activity activity;
        private TimePickerDialogInterface c;

        private MyHandler(Activity activity, TimePickerDialogInterface c) {
            this.activity = activity;
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


}