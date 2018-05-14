package com.doooge.timemanager.Statistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by fredpan on 2018/1/31.
 */

public class StatisticFragment extends Fragment implements OnChartValueSelectedListener, OnChartGestureListener, View.OnClickListener {

    private static PieChart pieChart;
    private static PieDataSet pieDataSet;
    private final Calendar[] selectedStartCal = {Calendar.getInstance()};
    private final Calendar[] selectedEndCal = {Calendar.getInstance()};
    private LinearLayout linearLayout;
    private LocalDatabaseHelper ldb;
    private Button selectPirChartDisplayDuration;
    private String selectedPieChartDisplayDurationBtnDisplay;
    private ArrayList<Type> selectedtypes;//init below
    private Button endDay;
    private Button startDay;
    private TextView pieChartNoneData;
    private boolean haveCompletedTasks = false;

    private ArrayList<SpecificTask> allSpecificTasks;
    private PieChartHelper pieChartHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.statistic_page, container, false);

        ldb = LocalDatabaseHelper.getInstance(getActivity());
        selectedtypes = ldb.getAllType();//init

        pieChartHelper = new PieChartHelper(getActivity());

        selectPirChartDisplayDuration = rootView.findViewById(R.id.selectPieChartDIsplayPeriod);
        selectPirChartDisplayDuration.setOnClickListener(this);

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
        updateCenterText();
        pieChart.setCenterTextSize(33f);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.setOnChartGestureListener(this);

        return rootView;
    }

    /*
    Update the data based on the given selectedStartCal and selectedEndCal date
     */
    private void updatePieChart() {

        ArrayList<SpecificTask> specificTasks = ldb.findSpecificTasksByTypesDuringTime(selectedtypes, selectedStartCal[0], selectedEndCal[0]);
        allSpecificTasks.clear();
        allSpecificTasks = specificTasks;
        Iterator<SpecificTask> iterator = allSpecificTasks.iterator();
        while (iterator.hasNext()) {
            SpecificTask curr = iterator.next();
            if (curr.isCompletedInBoolean()) {
                haveCompletedTasks = true;
                break;
            }
        }

        updateCenterText();

        PieDataSet newPieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
        newPieDataSet.setSliceSpace(3f);
        PieData newPieData = new PieData(newPieDataSet);
        pieChart.setData(newPieData);
        pieChart.notifyDataSetChanged();

        pieChart.invalidate();
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
        Type type = (Type) e.getData();
        //TODO Linechart (maybe next version)

    }

    @Override
    public void onNothingSelected() {

    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }

    /*
    OnClikcListener for long press: Choose different types
    */
    @Override
    public void onChartLongPressed(MotionEvent me) {//safely enough to implemented as a button

        selectedtypes = new ArrayList<>();
        // Instantiate an AlertDialog.Builder with its constructor
        final ArrayAdapter<Type> arrayAdapter = new ArrayAdapter<Type>(this.getContext(), android.R.layout.select_dialog_multichoice);

        final ArrayList<Type> types = ldb.getAllType();

        Iterator<Type> iterator = types.iterator();
        while (iterator.hasNext()) {
            Type type = iterator.next();
            arrayAdapter.add(type);
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Please select the types you want to see:");
        alertBuilder.setAdapter(arrayAdapter, null);
        alertBuilder.setPositiveButton("Ok", null);
        alertBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = alertBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (selectedtypes.size() == 0) {//user selected nothing
                            Toast.makeText(getContext(), "Please choose at least one type!", Toast.LENGTH_SHORT).show();
                        } else {
                            updatePieChart();
                            dialog.dismiss();
                        }

                    }

                });
            }
        });

        alertDialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //todo pre-select all the shown type
        alertDialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // check the checkbox state
                CheckedTextView checkedTextView = ((CheckedTextView) view);
                boolean checked = checkedTextView.isChecked();
                Type type = (Type) parent.getItemAtPosition(position);
                if (checked) {
                    selectedtypes.add(type);
                }
                if (!checked) {
                    boolean foundAndRemoved = selectedtypes.remove(type);
                    if (!foundAndRemoved) {
                        throw new IllegalStateException();
                    }


                }


            }
        });


        alertDialog.show();
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
    }

    /*
    OnClikcListener for btn: Choose different displaying duration
    */
    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.piechart_display_range_selection);

        final Button submitButton = dialog.findViewById(R.id.submitPieChartTimeRangeChange);
        submitButton.setText("OK");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDayStr = CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10);
                String endDayStr = CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10);
                if (startDayStr.compareTo(endDayStr) > 0) {//if start day is greater than end day
                    Toast.makeText(getContext(), "Start Date cannot after than the End Date !", Toast.LENGTH_SHORT).show();
                } else {
                    updateSelectedPieChartDisplayDurationBtnTEXT();
                    updatePieChart();
                    dialog.dismiss();
                }
            }
        });

        startDay = dialog.findViewById(R.id.startDayChoosed);
        startDay.setText(CalendarHelper.convertCal2UTC(Calendar.getInstance()).substring(0, 10));//by default display today's data only
        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStartCal[0] = displayAndGetSelectedDate(0);
            }
        });

        endDay = dialog.findViewById(R.id.endDayChoosed);
        endDay.setText(CalendarHelper.convertCal2UTC(Calendar.getInstance()).substring(0, 10));//by default display today's data only
        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEndCal[0] = displayAndGetSelectedDate(1);
            }
        });

        //Show by today
        final Button neutralButton = dialog.findViewById(R.id.showTodayOnlyPieChartTimeRangeChange);//Shown Today Only (DEFAULT)
        neutralButton.setText(R.string.shownDataInToday);
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStartCal[0] = Calendar.getInstance();
                selectedEndCal[0] = Calendar.getInstance();
                //update the UI
                startDay.setText(CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10));
                endDay.setText(CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10));
            }
        });

        // Show by past week
        final Button showByWeek = dialog.findViewById(R.id.showByWeek);
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
            }
        });

        //Show by past month
        final Button showByMonth = dialog.findViewById(R.id.showByMonth);
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
            }
        });

        //Cancel
        final Button cancel = dialog.findViewById(R.id.cancelPieChartTimeRangeChange);
        cancel.setText("Cancel");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * @param title 0 for selectedStartCal picker title while 1 for selectedEndCal picker title
     * @return Calendar instance
     */
    private Calendar displayAndGetSelectedDate(final int title) {
        final Calendar[] selectedCalendar = {Calendar.getInstance()};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final DatePicker picker = new DatePicker(getContext());
        picker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        if (title == 0) {//start
            builder.setTitle(R.string.durationStartPickerTitle);
        } else if (title == 1) {//end
            builder.setTitle(R.string.durationEndPickerTitle);
        } else {
            throw new InvalidParameterException();
        }

        builder.setView(picker);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
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
        builder.setNeutralButton("Go back to today", null);

        AlertDialog dialog = builder.create();
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
            pieChart.setCenterText("Time distribution");
            pieChart.setVisibility(View.VISIBLE);
            pieChartNoneData.setVisibility(View.GONE);
        } else {
            pieChart.setVisibility(View.GONE);
            pieChartNoneData.setVisibility(View.VISIBLE);
            pieChartNoneData.setText("NO TASK HAS BEEN DONE BETWEEN SELECTED PERIOD");
        }
    }

    private void updateSelectedPieChartDisplayDurationBtnTEXT() {
        String startDayStr = CalendarHelper.convertCal2UTC(selectedStartCal[0]).substring(0, 10);
        String endDayStr = CalendarHelper.convertCal2UTC(selectedEndCal[0]).substring(0, 10);
        if (startDayStr.compareTo(endDayStr) == 0) {//if shown one day only
            selectedPieChartDisplayDurationBtnDisplay = "Tasks finished on date " +
                    selectedStartCal[0].get(Calendar.YEAR) + "." +
                    (selectedStartCal[0].get(Calendar.MONTH) + 1) + "." +
                    selectedStartCal[0].get(Calendar.DAY_OF_MONTH);
        } else {
            selectedPieChartDisplayDurationBtnDisplay = "Tasks finished during " +
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
        //once user switch to other page, user will ge given today's task(s) after then went back
        if (isVisibleToUser) {
            haveCompletedTasks = false;//refresh the boolean condition
//            FragmentTransaction ftr = getFragmentManager().beginTransaction();
//            ftr.detach(this).attach(this).commit();
            updatePieChart();
            updateCenterText();
        }
    }

}
