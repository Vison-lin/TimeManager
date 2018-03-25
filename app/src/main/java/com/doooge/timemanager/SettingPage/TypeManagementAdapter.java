package com.doooge.timemanager.SettingPage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.doooge.timemanager.R;
import com.doooge.timemanager.Type;

import java.util.ArrayList;



public class TypeManagementAdapter extends ArrayAdapter<Type> {
    private ArrayList<Type> listOfType;
    private Activity context;

    public TypeManagementAdapter(ArrayList<Type> list, Activity context) {
        super(context, R.layout.type_mainpage, list);
        this.listOfType = list;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.type_item, null);


        //Handle buttons and add onClickListeners
        final Type type = listOfType.get(position);
        Button typeButton = view.findViewById(R.id.typeItem);
        typeButton.setText(type.getName());
        int color = Integer.parseInt(type.getColor());
        if(color==parent.getResources().getColor(R.color.violet)){
            typeButton.setBackground(parent.getResources().getDrawable(R.drawable.btn_bkgd_purple));
        }else if(color==-1){
            typeButton.setBackground(parent.getResources().getDrawable(R.drawable.btn_bkgd_default));
        }else if(color==parent.getResources().getColor(R.color.green)){
            typeButton.setBackground(parent.getResources().getDrawable(R.drawable.btn_bkgd_green));
        }else if(color==parent.getResources().getColor(R.color.blue)){
            typeButton.setBackground(parent.getResources().getDrawable(R.drawable.btn_bkgd_blue));
        }else if(color==parent.getResources().getColor(R.color.red)){
            typeButton.setBackground(parent.getResources().getDrawable(R.drawable.btn_bkgd_red));
        }else if(color==parent.getResources().getColor(R.color.yellow)){
            typeButton.setBackground(parent.getResources().getDrawable(R.drawable.btn_bkgd_yellow));
        }
        else {
            typeButton.getBackground().setColorFilter(new LightingColorFilter(color, color));
        }

        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context,TaskManagementActivity.class);
                intent.putExtra("TypeInfo",type);
                context.startActivity(intent);
            }
        });


        typeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                Intent intent = new Intent(context, TypeCreator.class);
                intent.putExtra("TypeInfo", type);
                context.startActivity(intent);


                return false;
            }
        });



        return view;
    }
}
