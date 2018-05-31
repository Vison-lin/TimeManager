package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private Drawable color1;
    private int id;
    private String name;
    private EditText typeName;
    private LocalDatabaseHelper ldh = LocalDatabaseHelper.getInstance(this);
    private Type type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typemanagement);

        type = (Type)getIntent().getSerializableExtra("TypeInfo");
        typeName = findViewById(R.id.typeName);
        color = getResources().getColor(R.color.btn_bkgd_def);
        createType = findViewById(R.id.createType);
        createType.setOnClickListener(this);
        Button delete = findViewById(R.id.deleteType);
        delete.setOnClickListener(this);
        delete.setVisibility(View.INVISIBLE);
        if(type!=null){
            name = type.getName();
            typeName.setText(name);
            if(type.getId()!=-999) {
                delete.setVisibility(View.VISIBLE);
            }
            createType.setBackground(getDrawable(R.drawable.update));
            TextView title = findViewById(R.id.titleType);
            title.setText("Update the Type");
            color = Integer.parseInt(type.getColor());
            id = type.getId();
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
                } else if (color == (getResources().getColor(R.color.btn_bkgd_def))) {
                    TextView warn = findViewById(R.id.warn_type);

                    warn.setError("Choose a color.");


                } else {
                    if(type==null) {
                        Type type = new Type(name, color + "");
                        ldh.insertToTypeTable(type);
                        Intent intent = new Intent(TypeCreator.this, TypeManagementActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        this.startActivity(intent);
                    }else{
                        type.setName(name);
                        type.setColor(color + "");
                        ldh.updateTypeTable(type);

                        Intent intent = new Intent(TypeCreator.this, TypeManagementActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        this.startActivity(intent);
                    }

                }
                break;

            case R.id.blue_type:
                reSetchoose();
                color = getResources().getColor(R.color.btn_bkgd_blue);
                blue.setBackground(getResources().getDrawable(R.drawable.typecolor_blue));
                break;
            case R.id.green_type:
                reSetchoose();

                color = getResources().getColor(R.color.btn_bkgd_green);
                green.setBackground(getResources().getDrawable(R.drawable.typecolor_green));
                break;
            case R.id.yellow_type:
                reSetchoose();
                color = getResources().getColor(R.color.btn_bkgd_yellow);
                yellow.setBackground(getResources().getDrawable(R.drawable.typecolor_yellow));
                break;
            case R.id.violet_type:
                reSetchoose();
                color = getResources().getColor(R.color.btn_bkgd_purple);
                violet.setBackground(getResources().getDrawable(R.drawable.typecolor_violet));
                break;
            case R.id.red_type:
                reSetchoose();
                color = getResources().getColor(R.color.btn_bkgd_red);
                red.setBackground(getResources().getDrawable(R.drawable.typecolor_red));
                break;
            case R.id.deleteType:
                ldh.deleteTypeTable(type.getId());

                Intent intent = new Intent(TypeCreator.this, TypeManagementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
        }
    }

    public void reSetchoose(){
        blue.setBackgroundColor(getResources().getColor(R.color.btn_bkgd_blue));
        green.setBackgroundColor(getResources().getColor(R.color.btn_bkgd_green));
        yellow.setBackgroundColor(getResources().getColor(R.color.btn_bkgd_yellow));
        violet.setBackgroundColor(getResources().getColor(R.color.btn_bkgd_purple));
        red.setBackgroundColor(getResources().getColor(R.color.btn_bkgd_red));

    }





}
