package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.Type;

/**
 * Created by fredpan on 2018/1/26.
 */

public class TypeCreator extends AppCompatActivity implements View.OnClickListener{

    private Button createType;
    private Button blue;
    private Button green;
    private Button yellow;
    private Button violet;
    private Button red;
    private int color ;
    private String name;
    private EditText typeName;
    private LocalDatabaseHelper ldh = new LocalDatabaseHelper(this);
    private Type type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typemanagement);

        type = (Type)getIntent().getSerializableExtra("TypeInfo");
        typeName = findViewById(R.id.typeName);
        color= getResources().getColor(R.color.gray);
        createType = findViewById(R.id.createType);
        createType.setOnClickListener(this);
        Button delete = findViewById(R.id.deleteType);
        delete.setOnClickListener(this);
        delete.setVisibility(View.INVISIBLE);
        if(type!=null){
            name = type.getName();
            typeName.setText(name);
            delete.setVisibility(View.VISIBLE);
            createType.setText("Updated");
            TextView title = findViewById(R.id.titleType);
            title.setText("Updated the Type");
            color = Integer.parseInt(type.getColor());
        }
        blue = findViewById(R.id.blue_type);
        blue.setOnClickListener(this);
        green = findViewById(R.id.green_type);
        green.setOnClickListener(this);
        yellow = findViewById(R.id.yellow_type);
        yellow.setOnClickListener(this);
        violet = findViewById(R.id.violet_type);
        violet.setOnClickListener(this);
        red = findViewById(R.id.red_type);
        red.setOnClickListener(this);



    }



    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.createType:
                name = String.valueOf(typeName.getText());
                if (name.equals("")) {
                    typeName.setError("Enter a name.");
                    typeName.setBackground(getResources().getDrawable(R.drawable.back_red));
                } else {
                    if(type==null) {
                        Type type = new Type(name, color + "");
                        ldh.insertToTypeTable(type);
                        Intent intent = new Intent(TypeCreator.this, TypeManagementActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        this.startActivity(intent);
                    }else{
                        //TODO Fred please write a function for updating the type
                        //ldh.updateTypeTable(newType);
                    }

                }
                break;

            case R.id.blue_type:
                reSetchoose();
                color = getResources().getColor(R.color.blue);
                blue.setBackground(getResources().getDrawable(R.drawable.blue_button_roundedge_choosed));
                Toast.makeText(getApplicationContext(), "choose blue success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.green_type:
                reSetchoose();
                color = getResources().getColor(R.color.green);
                green.setBackground(getResources().getDrawable(R.drawable.green_button_roundedge_choosed));
                Toast.makeText(getApplicationContext(), "choose green success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yellow_type:
                reSetchoose();
                color = getResources().getColor(R.color.yellow);
                yellow.setBackground(getResources().getDrawable(R.drawable.yellow_button_roundedge_choosed));
                Toast.makeText(getApplicationContext(), "choose yellow success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.violet_type:
                reSetchoose();
                color = getResources().getColor(R.color.violet);
                violet.setBackground(getResources().getDrawable(R.drawable.violet_button_roundedge_choosed));
                Toast.makeText(getApplicationContext(), "choose violet success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.red_type:
                reSetchoose();
                color = getResources().getColor(R.color.red);
                red.setBackground(getResources().getDrawable(R.drawable.red_button_roundedge_choosed));
                Toast.makeText(getApplicationContext(), "choose red success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteType:
                //TODO Fred  please check why this is not functional
                System.out.println("!!!!!!!!!!!!!");
                ldh.deleteTypeTable(type.getId());
                ldh.showAllData(this);
//                Intent intent = new Intent(TypeCreator.this, TypeManagementActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                this.startActivity(intent);
                break;
        }
    }

    public void reSetchoose(){
        blue.setBackground(getResources().getDrawable(R.drawable.blue_button_roundedge));
        green.setBackground(getResources().getDrawable(R.drawable.green_button_roundedge));
        yellow.setBackground(getResources().getDrawable(R.drawable.yellow_button_roundedge));
        violet.setBackground(getResources().getDrawable(R.drawable.violet_button_roundedge));
        red.setBackground(getResources().getDrawable(R.drawable.red_button_roundedge));

    }





}