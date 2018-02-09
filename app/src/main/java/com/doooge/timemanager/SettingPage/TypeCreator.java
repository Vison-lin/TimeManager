package com.doooge.timemanager.SettingPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.doooge.timemanager.R;

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
    private int color;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typemanagement);

        createType = findViewById(R.id.createType);
        createType.setOnClickListener(this);

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
