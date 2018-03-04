package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.Type;

import java.util.ArrayList;

/**
 * Created by fredpan on 2018/1/26.
 */

public class TypeManagementActivity extends AppCompatActivity {
    private LocalDatabaseHelper ldh;
    private ArrayList<Type> typeList;
    private TypeManagementAdapter typeAdpter;
    private ListView mList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_mainpage);
        ldh = new LocalDatabaseHelper(this);
        try {
            typeList = ldh.getAllType();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "No type now !!", Toast.LENGTH_SHORT).show();
        }
        if(typeList!=null) {
            typeAdpter = new TypeManagementAdapter(typeList, this);

            mList = findViewById(R.id.controlPanelListView);
            mList.setAdapter(typeAdpter);
        }



        Button addtype = findViewById(R.id.addType);
        addtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeManagementActivity.this,TypeCreator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
