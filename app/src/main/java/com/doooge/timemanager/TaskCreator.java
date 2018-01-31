package com.doooge.timemanager;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.StringTokenizer;


public class TaskCreator extends AppCompatActivity {

    public TimeBarView mView;
    private MyHandler handler;

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
                start.setText(token.nextToken());
                String endTime = token.nextToken();
                end.setText(endTime);
            }
        }

    }







}
