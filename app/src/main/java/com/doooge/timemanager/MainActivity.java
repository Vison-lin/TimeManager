package com.doooge.timemanager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sample code for DB
        LocalDatabaseHelper ldb = new LocalDatabaseHelper(this);
        ldb.insertToSpecificTaskTable("SpecificTaskTable", 1, "2017", "2018", 21);
        ldb.insertToTaskTable("TaskTable", 22);
        ldb.insertToTypeTable("TypeTable", "ColorInGreen");
        ldb.showAllData(this);


    }
}
