package com.doooge.timemanager;

import android.app.AlertDialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

public class TimePickerDialog {

    private Context mContext;
    private AlertDialog mAlertDialog;
    private int mHour, mMinute;
    private SpecificTaskCreator.TimePickerDialogInterface timePickerDialogInterface;
    private TimePicker mTimePicker;
    private int mTag;
    private DatePicker mDatePicker;

    private int mYear, mDay, mMonth;

    TimePickerDialog(Context context, SpecificTaskCreator.TimePickerDialogInterface c) {
        super();
        mContext = context;

        timePickerDialogInterface = c;
    }


    private View initDatePicker() {

        View inflate = LayoutInflater.from(mContext).inflate(
                R.layout.timepicker, null);
        mDatePicker = inflate
                .findViewById(R.id.datePicker);
        resizePikcer(mDatePicker);
        return inflate;
    }



    private void initDialog(View view) {



        

        Button add  = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        getDatePickerValue();

                        timePickerDialogInterface.positiveListener(mYear, mMonth, mDay);

                        mAlertDialog.dismiss();
            }
        });
        Button cancel  = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();

            }
        });



        mAlertDialog.setView(view);
    }


    /**
     * 显示日期选择器
     */
    public void showDatePickerDialog() {
        mTag = 1;
        View view1 = initDatePicker();
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        initDialog(view1);
        mAlertDialog.show();
    }

    /*
    * 调整numberpicker大小
    */
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }


    /**
     * 获取日期选择的值
     */
    private void getDatePickerValue() {
        mYear = mDatePicker.getYear();
        mMonth = mDatePicker.getMonth();
        mDay = mDatePicker.getDayOfMonth();
    }

}