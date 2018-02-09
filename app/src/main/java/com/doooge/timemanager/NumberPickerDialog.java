package com.doooge.timemanager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.NumberPicker;

/**
 * Created by fredpan on 2018/2/2.
 */

public class NumberPickerDialog extends DialogFragment {


    private NumberPicker.OnValueChangeListener valueChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final NumberPicker numberPicker = new NumberPicker(getActivity());

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(24);
        String[] display = {
                getString(R.string.DELETE_180_MINS),//0
                getString(R.string.DELETE_165_MINS),//1
                getString(R.string.DELETE_150_MINS),//2
                getString(R.string.DELETE_135_MINS),//3
                getString(R.string.DELETE_120_MINS),//4
                getString(R.string.DELETE_105_MINS),//5
                getString(R.string.DELETE_90_MINS),//6
                getString(R.string.DELETE_75_MINS),//7
                getString(R.string.DELETE_60_MINS),//8
                getString(R.string.DELETE_45_MINS),//9
                getString(R.string.DELETE_30_MINS),//10
                getString(R.string.DELETE_15_MINS),//11
                getString(R.string.DEFAULT_MIN),//12 <- MIDDLE
                getString(R.string.ADD_15_MINS),//13
                getString(R.string.ADD_30_MINS),//14
                getString(R.string.ADD_45_MINS),//15
                getString(R.string.ADD_60_MINS),//16
                getString(R.string.ADD_75_MINS),//17
                getString(R.string.ADD_90_MINS),//18
                getString(R.string.ADD_105_MINS),//19
                getString(R.string.ADD_120_MINS),//20
                getString(R.string.ADD_135_MINS),//21
                getString(R.string.ADD_150_MINS),//22
                getString(R.string.ADD_165_MINS),//23
                getString(R.string.ADD_180_MINS),//24
        };
        numberPicker.setDisplayedValues(display);
        numberPicker.setValue(12);//set to middle
        numberPicker.setWrapSelectorWheel(false);//disable wrap selection

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Value");
        builder.setMessage("Choose a number :");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNeutralButton("Edit In Detail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), SpecificTaskCreator.class);
                startActivity(intent);//TODO Pass SpecificTask into SpecificTaskCreator!
            }
        });
        builder.setView(numberPicker);
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}