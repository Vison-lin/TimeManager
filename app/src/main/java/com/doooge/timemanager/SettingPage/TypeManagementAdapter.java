package com.doooge.timemanager.SettingPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
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

    TypeManagementAdapter(ArrayList<Type> list, Activity context) {
        super(context, R.layout.type_mainpage, list);
        this.listOfType = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = inflater.inflate(R.layout.type_item, null);
        final Type type = listOfType.get(position);
        Button typeButton = view.findViewById(R.id.typeItem);
        typeButton.setText(type.getName());
        int color = Integer.parseInt(type.getColor());
        typeButton.setBackgroundColor(color);
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
