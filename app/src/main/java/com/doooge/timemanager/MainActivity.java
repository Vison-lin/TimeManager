package com.doooge.timemanager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private MusicProgressBar mView;
    private int startTime;
    private Handler handler;

    @SuppressLint("WrongConstant")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taskcreator);


       handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if(msg.what==0) {
                    String message = (String) msg.obj;
                    TextView view = findViewById(R.id.textView1);
                    view.setText(message);
                }
            }
        };


        mView =new MusicProgressBar(this);
                mView.Test(new MusicProgressBar.Callback() {


                    @Override
                    public Handler execute() {
                        // TODO Auto-generated method



                        System.out.println("实现回调");


                        return handler;
                    }
                });






        //Sample code for DB
        LocalDatabaseHelper ldb = new LocalDatabaseHelper(this);
        ldb.insertToSpecificTaskTable("SpecificTaskTable", 1, "2017", "2018", 21);
        ldb.insertToTaskTable("TaskTable", 22);
        ldb.insertToTypeTable("TypeTable", "ColorInGreen");
        ldb.showAllData(this);


    }


    public void changeView(String time){
//      String time = ""+view.getProgress();
        EditText view = findViewById(R.id.time1);
        view.setText(time);


    }




}
