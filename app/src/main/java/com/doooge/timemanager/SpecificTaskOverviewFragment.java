package com.doooge.timemanager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.doooge.timemanager.SettingPage.SettingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by diana on 2018-01-26.
 */

public class SpecificTaskOverviewFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private LocalDatabaseHelper ldh;
    private SpecificTaskOverviewAdapter adapter;
    private ArrayList<SpecificTask> specificTasks;
    private ImageButton calBtn;
    private TextView calMonth;
    private TextView calDay;
    private TextView pageTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_task_overview, container, false);

        ldh = LocalDatabaseHelper.getInstance(getContext());
        //Assign button listeners to here
        ImageView settingBtn = rootView.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(this);
        calBtn = rootView.findViewById(R.id.showCalender);
        calBtn.setOnClickListener(this);


        Calendar today = Calendar.getInstance();
        pageTitle = rootView.findViewById(R.id.activityTitleText);
        specificTasks = ldh.findSpecificTasksByTime(today);//search all specificTasks that start today

        //calBtn init & change text
        calMonth = rootView.findViewById(R.id.calMonth);
        calDay = rootView.findViewById(R.id.calDay);

        updateCalBtnText(today);
        updatePageTitle(today);
        adapter = new SpecificTaskOverviewAdapter(specificTasks, getActivity());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.settingBtn:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.showCalender:
                getSelectedDate();

                break;
        }
    }


    public void getSelectedDate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.MyTimepicker));
        final DatePicker picker = new DatePicker(new ContextThemeWrapper(getContext(), R.style.MyTimepicker));
        builder.setTitle("Select date");
        builder.setView(picker);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                updateView(selectedCalendar);
                //todo upadte statistic page
            }
        });
        builder.setNeutralButton("Go back to today", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar selectedCalendar = Calendar.getInstance();
                updateView(selectedCalendar);
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //once user switch to other page, user will ge given today's task(s) after then went back
        if (isVisibleToUser) {
            if (calBtn != null) {
                updateView(Calendar.getInstance());//switch back to today
            }
        } else {
//            try {
//                FragmentTransaction ftr = getFragmentManager().beginTransaction();
//                Fragment currentFragment = getFragmentManager().findFragmentByTag("StatisticFragment");
//                ftr.detach(currentFragment).attach(currentFragment).commit();
//            } catch (Exception e){
//                System.out.println("!!!!!!!!!!!!!");
//            }


        }
    }

    private void updateCalBtnText(Calendar calendar) {
        SimpleDateFormat getMonth = new SimpleDateFormat("MMMM");
        String month = getMonth.format(calendar.getTime());
        if (month.equals("September")) {
            month = month.substring(0, 4);
        } else {
            month = month.substring(0, 3);
        }
        month = month.toUpperCase();
        String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        calMonth.setText(month);
        calDay.setText(day);
    }

    private void updatePageTitle(Calendar calendar) {
        int numOfSpecificTask = specificTasks.size();
        if (numOfSpecificTask < 0) {
            throw new IllegalArgumentException();
        } else if (numOfSpecificTask < 2 && numOfSpecificTask >= 0) {
            //pageTitle.setBackground(getResources().getDrawable(R.drawable.title1));

            //taskStatus = "Task";

        } else {
            //pageTitle.setBackground(getResources().getDrawable(R.drawable.title2));

            //taskStatus = "Tasks";
        }
        //pageTitle.setText("Daily " + taskStatus);
        //pageTitle.setText(taskStatus + " in " + month + ". " + day + " " + year);
    }

    /**
     * update the whole fragment view
     *
     * @param calendar the day to be shown
     */
    private void updateView(Calendar calendar) {
        specificTasks.clear();
        specificTasks = ldh.findSpecificTasksByTime(calendar);
        adapter.updateSpecificTaskOverviewAdapter(specificTasks);
        updateCalBtnText(calendar);
        updatePageTitle(calendar);
    }


}
