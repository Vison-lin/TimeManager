package com.doooge.timemanager;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.StringTokenizer;


public class TaskCreator extends AppCompatActivity {

    public TimeBarView mView;
    private MyHandler handler;
    private static String startTime;
    private static String endTime;
    private String userName;
    private LocalDatabaseHelper ldh = new LocalDatabaseHelper(this);

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taskcreator);

       handler = new MyHandler(this);
        mView = new TimeBarView(this);
        mView.Test(new TimeBarView.Callback() {


            @Override
            public Handler execute() {
                return handler;
            }
        });
    }


    /**
     *  To create Handler class for receiveing data from TimeBarView class.
     */

    private static class MyHandler extends Handler{
        Activity activity;
        private MyHandler(Activity activity){
            this.activity= activity;
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

    public void addTask(View view) {
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
            calStart.set(2017, 0, 31, 5, 5);
            calEnd.set(2017, 0, 31,8, 8);
            System.out.println("StartTime: "+calStart.get(Calendar.HOUR_OF_DAY)+":"+Integer.parseInt(start[1]));
            System.out.println(CalendarHelper.convertCal2UTC(calStart));
            System.out.println("EndTime: "+Integer.parseInt(end[0])+":"+Integer.parseInt(end[1]));
            System.out.println(CalendarHelper.convertCal2UTC(calEnd));
            int h =  Integer.parseInt(end[0]);
            int min = Integer.parseInt(end[1]);

            System.out.println(calEnd.get(Calendar.HOUR_OF_DAY));

            SpecificTask task = new SpecificTask(userName, calStart, calEnd);
            System.out.println("======="+(task==null));
            ldh.insertToSpecificTaskTable((SpecificTask) task);
            ldh.showAllData(this);


        }
    }


}
