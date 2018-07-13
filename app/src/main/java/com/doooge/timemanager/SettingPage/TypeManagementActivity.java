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

public class TypeManagementActivity extends AppCompatActivity {
    private ArrayList<Type> typeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_mainpage);
        LocalDatabaseHelper ldh = LocalDatabaseHelper.getInstance(this);
        Button addtype = findViewById(R.id.addType);

        try {
            typeList = ldh.getAllType();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "No type.", Toast.LENGTH_SHORT).show();
        }

        if(typeList!=null) {
            TypeManagementAdapter typeAdpter = new TypeManagementAdapter(typeList, this);
            ListView mList = findViewById(R.id.controlPanelListView);
            mList.setAdapter(typeAdpter);
        }

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
