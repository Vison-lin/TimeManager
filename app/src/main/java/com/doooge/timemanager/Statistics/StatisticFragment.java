package com.doooge.timemanager.Statistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doooge.timemanager.CalendarHelper;
import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;
import com.doooge.timemanager.Type;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class StatisticFragment extends Fragment implements OnChartValueSelectedListener, View.OnClickListener {

    private PieChart pieChart;
    private PieDataSet pieDataSet;
    private final Calendar[] selectedStartCal = {Calendar.getInstance()};
    private final Calendar[] selectedEndCal = {Calendar.getInstance()};
    private LinearLayout linearLayout;
    private LocalDatabaseHelper ldb;
    private Button selectPirChartDisplayDuration;
    private Button selectPieChartDisplayTypes;
    private String selectedPieChartDisplayDurationBtnDisplay;
    private ArrayList<Type> selectedtypes;//init below
    private ArrayList<Type> selectedtypes_Previous;//init below
    private Button endDay;
    private Button startDay;
    private TextView pieChartNoneData;
    private boolean haveCompletedTasks = false;

    private ArrayList<SpecificTask> allSpecificTasks;
    private PieChartHelper pieChartHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.statistic_page, container, false);

        ldb = LocalDatabaseHelper.getInstance(getActivity());
        selectedtypes = ldb.getAllType();//init
        selectedtypes_Previous = new ArrayList<>();

        pieChartHelper = new PieChartHelper(getActivity());

        selectPirChartDisplayDuration = rootView.findViewById(R.id.selectPieChartDIsplayPeriod);
        selectPirChartDisplayDuration.setOnClickListener(this);

        selectPieChartDisplayTypes = rootView.findViewById(R.id.selectPieChartDIsplayTypes);
        selectPieChartDisplayTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerForSelectType();
            }
        });

        pieChartNoneData = rootView.findViewById(R.id.pieChartNoneData);


        /*
        All the charts' data will be refresh only if user add/remove SpecificTask.
        Since add/remove requires users go to new activity, we do not use setUserVisibleHint here!
        All the data will only be refresh at the time when this fragment created.
        We also leave setOffscreenPageLimit() to 1 as default so that statisic page will be refreshed once user goes to the quickAccessTaskFragment.
         */

        //PIECHART
        pieChart = rootView.findViewById(R.id.pieChart);

        allSpecificTasks = ldb.findSpecificTasksByTime(Calendar.getInstance());//By default: show today's tasks
        updateSelectedPieChartDisplayDurationBtnTEXT();
        pieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
        pieDataSet.setSliceSpace(3f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);//Set data label color
        pieChart.setDrawCenterText(true);
        pieChart.setEntryLabelTextSize(17f);
        pieChart.getLegend().setEnabled(false);
        pieChart.getLegend().setWordWrapEnabled(true);

        updateCenterText();
        pieChartNoneData.setText(getString(R.string.StatisticLoading));//first time open app

        pieChart.setCenterTextSize(33f);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(this);

        return rootView;
    }

    /*
    Update the data based on the given selectedStartCal and selectedEndCal date
     */
    private void updatePieChart() {
        haveCompletedTasks = false;
        ArrayList<SpecificTask> specificTasks = ldb.findSpecificTasksByTypesDuringTime(selectedtypes, selectedStartCal[0], selectedEndCal[0]);
        allSpecificTasks.clear();
        allSpecificTasks = specificTasks;
        for (SpecificTask curr : allSpecificTasks) {
            if (curr.isCompletedInBoolean() && selectedtypes.contains(curr.getType())) {
                haveCompletedTasks = true;
                break;
            }
        }

        PieDataSet newPieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
        newPieDataSet.setSliceSpace(3f);
        PieData newPieData = new PieData(newPieDataSet);

        pieChart.setData(newPieData);
        pieChart.notifyDataSetChanged();

        pieChart.invalidate();
        updateCenterText();

    }

    /*
    ######################################################################################
    ###########################        OnClikcListeners        ###########################
    ######################################################################################
     */

    /*
    OnClikcListener for pieChartSlide
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        Type type = (Type) e.getData();
//        TODO Linechart (maybe next version)
    }

    @Override
    public void onNothingSelected() {

    }

    /*
    OnClikcListener for btn: Choose different displaying duration
    */
    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final AlertDialog dialog = builder.create();
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setContentView(R.layout.piechart_display_range_selection);

        final Button submitButton = dialog.findViewById(R.id.submitPieChartTimeRangeChange);
        startDay = dialog.findViewById(R.id.startDayChoosed);
        endDay = dialog.findViewById(R.id.endDayChoosed);

        final ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setStrokeWidth(30);
        final ShapeDrawable shapeDrawableAfterHightlight = new ShapeDrawable();
        shapeDrawableAfterHightlight.getPaint().setStyle(startDay.getPaint().getStyle());
        shapeDrawableAfterHightlight.getPaint().setColor(0);

        btnDisplayModification(submitButton, ViewGroup.LayoutParams.WRAP_CONTENT);
        submitButton.setText(R.string.OKBtn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDayStr = CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10);
                String endDayStr = CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10);
                if (startDayStr.compareTo(endDayStr) > 0) {//if start day is greater than end day
                    Toast.makeText(getContext(), R.string.StatisticPageStartDateAfterTheEndDateRemindar, Toast.LENGTH_SHORT).show();
                } else {
                    updateSelectedPieChartDisplayDurationBtnTEXT();
                    updatePieChart();
                    dialog.dismiss();
                }
            }
        });

        btnDisplayModification(startDay, ViewGroup.LayoutParams.WRAP_CONTENT);

        startDay.setText(
                CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0,10)
        );//by default display today's data only
        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStartCal[0] = displayAndGetSelectedDate(0);
            }
        });

        btnDisplayModification(endDay, ViewGroup.LayoutParams.WRAP_CONTENT);
        endDay.setText(
                CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0,10)
        );//by default display today's data only
        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEndCal[0] = displayAndGetSelectedDate(1);
            }
        });

        //Show by today
        final Button showByToday = dialog.findViewById(R.id.showTodayOnlyPieChartTimeRangeChange);//Shown Today Only (DEFAULT)
        btnDisplayModification(showByToday, 500);
        showByToday.setText(R.string.shownDataInToday);
        showByToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStartCal[0] = Calendar.getInstance();
                selectedEndCal[0] = Calendar.getInstance();
                //update the UI
                startDay.setText(CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10));
                endDay.setText(CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10));


                //------------------animation------

                AlphaAnimation anim_alpha = new AlphaAnimation(0, 1);
                anim_alpha.setDuration(800);//动画时间
                v.startAnimation(anim_alpha);//启动动画
                startDay.startAnimation(anim_alpha);

            }
        });


        // Show by past week
        final Button showByWeek = dialog.findViewById(R.id.showByWeek);
        btnDisplayModification(showByWeek, 500);
        showByWeek.setText(R.string.shownDataInPastWeek);
        showByWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStartCal[0] = Calendar.getInstance();
                selectedStartCal[0].add(Calendar.DAY_OF_YEAR, -6);//6 days since dB include selectedStartCal day and selectedEndCal day!
                selectedEndCal[0] = Calendar.getInstance();
                //update the UI
                startDay.setText(CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10));
                endDay.setText(CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10));

                //------------------animation------

                AlphaAnimation anim_alpha = new AlphaAnimation(0, 1);
                anim_alpha.setDuration(800);//动画时间
                v.startAnimation(anim_alpha);//启动动画
                startDay.startAnimation(anim_alpha);
            }
        });

        //Show by past month
        final Button showByMonth = dialog.findViewById(R.id.showByMonth);
        btnDisplayModification(showByMonth, 500);
        showByMonth.setText(R.string.shownDataInPastMonth);
        showByMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStartCal[0] = Calendar.getInstance();
                selectedStartCal[0].add(Calendar.MONTH, -1);//last month
                selectedStartCal[0].add(Calendar.DAY_OF_YEAR, 1);//but one day further
                selectedEndCal[0] = Calendar.getInstance();
                //update the UI
                startDay.setText(CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10));
                endDay.setText(CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10));


                //------------------animation------

                AlphaAnimation anim_alpha = new AlphaAnimation(0, 1);
                anim_alpha.setDuration(800);//动画时间
                v.startAnimation(anim_alpha);//启动动画
                startDay.startAnimation(anim_alpha);
            }
        });

        //Cancel
        final Button cancel = dialog.findViewById(R.id.cancelPieChartTimeRangeChange);
        btnDisplayModification(cancel, ViewGroup.LayoutParams.WRAP_CONTENT);
        cancel.setText(R.string.CancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void onClickListenerForSelectType() {
        selectedtypes = new ArrayList<>();

        final ArrayList<Type> types = ldb.getAllType();

        selectedtypes.addAll(types);
        if (selectedtypes_Previous.size() != 0) {
            selectedtypes.clear();
            selectedtypes.addAll(selectedtypes_Previous);
        }


        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle(R.string.StatisticPeriodSelectionSelectFinishedTasks);
        StatisticSpinnerAdapter mAdapter = new StatisticSpinnerAdapter(types, selectedtypes, getActivity());
        alertBuilder.setAdapter(mAdapter, null);
        alertBuilder.setPositiveButton(R.string.OKBtn, null);
        alertBuilder.setNegativeButton(R.string.CancelBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedtypes.clear();
                selectedtypes.addAll(selectedtypes_Previous);
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = alertBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.background_color);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (selectedtypes.size() == 0) {//user selected nothing
                            Toast.makeText(getContext(), R.string.StatisticPageNoTypeSelectedRemindar, Toast.LENGTH_SHORT).show();
                        } else {
                            selectedtypes_Previous.clear();
                            selectedtypes_Previous.addAll(selectedtypes);
                            updatePieChart();
                            dialog.dismiss();
                        }

                    }

                });
            }
        });
        alertDialog.show();
    }

    /**
     * @param title 0 for selectedStartCal picker title while 1 for selectedEndCal picker title
     * @return Calendar instance
     */
    private Calendar displayAndGetSelectedDate(final int title) {
        final Calendar[] selectedCalendar = {Calendar.getInstance()};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyTimepicker);
        final DatePicker picker = new DatePicker(new ContextThemeWrapper(getContext(), R.style.MyTimepicker));
        picker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        builder.setView(picker);
        builder.setNegativeButton(R.string.CancelBtn, null);
        builder.setPositiveButton(R.string.SelectBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCalendar[0].set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                if (title == 0) {//start
                    startDay.setText(CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10));


                } else if (title == 1) {//end
                    endDay.setText(CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10));


                } else {
                    throw new InvalidParameterException();
                }
            }
        });
        builder.setNeutralButton(R.string.TodayBtn, null);

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.color.background_color);
        dialog.show();

        final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);//Shown Today Only (DEFAULT)
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                //refresh the view to point users to today
                picker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            }
        });

        return selectedCalendar[0];
    }

    private void updateCenterText() {
        if (haveCompletedTasks) {
            pieChart.setCenterText(getString(R.string.StatisticPageCenterText));
            pieChart.setCenterTextSize(pieChart.getHoleRadius() / 2);
            pieChart.setVisibility(View.VISIBLE);
            pieChartNoneData.setVisibility(View.GONE);
        } else {
            pieChart.setVisibility(View.GONE);
            pieChartNoneData.setVisibility(View.VISIBLE);
            pieChartNoneData.setText(R.string.StatisticNoTaskReminder);
        }
    }

    private void updateSelectedPieChartDisplayDurationBtnTEXT() {
        String startDayStr = CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10);
        String endDayStr = CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10);
        if (startDayStr.compareTo(endDayStr) == 0) {//if shown one day only
            selectedPieChartDisplayDurationBtnDisplay = getString(R.string.StatisticPageTaskFinishedOn) + " " +
                    selectedStartCal[0].get(Calendar.YEAR) + "." +
                    (selectedStartCal[0].get(Calendar.MONTH) + 1) + "." +
                    selectedStartCal[0].get(Calendar.DAY_OF_MONTH);
        } else {
            selectedPieChartDisplayDurationBtnDisplay = getString(R.string.StatisticPageSTasksFinishedDuring) +
                    selectedStartCal[0].get(Calendar.YEAR) + "." +
                    (selectedStartCal[0].get(Calendar.MONTH) + 1) + "." +
                    selectedStartCal[0].get(Calendar.DAY_OF_MONTH)
                    + " ~ " +
                    selectedEndCal[0].get(Calendar.YEAR) + "." +
                    (selectedEndCal[0].get(Calendar.MONTH) + 1) + "." +
                    selectedEndCal[0].get(Calendar.DAY_OF_MONTH);
        }
        System.out.println(selectedPieChartDisplayDurationBtnDisplay);
        selectPirChartDisplayDuration.setText(selectedPieChartDisplayDurationBtnDisplay);
        selectedPieChartDisplayDurationBtnDisplay = "";
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        haveCompletedTasks = false;
        //once user switch to other page, user will ge given today's task(s) after then went back
        if (isVisibleToUser && ldb != null) {

            selectedtypes.clear();
            selectedtypes = ldb.getAllType();
            selectedtypes_Previous.clear();

            updatePieChart();
            updateCenterText();
        }
        if (!isVisibleToUser && ldb != null) {
            pieChart.setVisibility(View.GONE);
            pieChartNoneData.setVisibility(View.VISIBLE);
            pieChartNoneData.setText(R.string.StatisticLoading);
        }
    }

    private void btnDisplayModification(Button button, int layoutParamsWidth) {
        GradientDrawable btnDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{getResources().getColor(R.color.statpage_blue), getResources().getColor(R.color.background_color)});
        btnDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        btnDrawable.setGradientRadius(210.0f);
        btnDrawable.setCornerRadius(50.f);
        button.setBackground(btnDrawable);
        button.setWidth(layoutParamsWidth);
    }

}
