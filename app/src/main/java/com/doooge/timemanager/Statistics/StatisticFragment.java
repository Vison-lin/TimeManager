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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.R;
import com.doooge.timemanager.SpecificTask;
import com.doooge.timemanager.Type;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by fredpan on 2018/1/31.
 */

public class StatisticFragment extends Fragment implements OnChartValueSelectedListener, OnChartGestureListener, View.OnClickListener {

    private static PieChart pieChart;
    private static PieDataSet pieDataSet;
    private LinearLayout linearLayout;
    private LocalDatabaseHelper ldb;
    private Button selectPirChartDisplayDuration;
    private float holeRadius;
    //private Context context;
    //private TextView pieChartModelSelectionDisplay;
    //Stores all the SpecificTasks with key by type ID.
    private ArrayList<SpecificTask> allSpecificTasks;
    private PieChartHelper pieChartHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.statistic_page, container, false);

        ldb = LocalDatabaseHelper.getInstance(getActivity());
        //linearLayout = rootView.findViewById(R.id.typeList);
        pieChartHelper = new PieChartHelper(getActivity());

        selectPirChartDisplayDuration = rootView.findViewById(R.id.selectPieChartDIsplayPeriod);
        selectPirChartDisplayDuration.setOnClickListener(this);

        /*
        All the charts' data will be refresh only if user add/remove SpecificTask.
        Since add/remove requires users go to new activity, we do not use setUserVisibleHint here!
        All the data will only be refresh at the time when this fragment created.
        We also leave setOffscreenPageLimit() to 1 as default so that statisic page will be refreshed once user goes to the quickAccessTaskFragment.
         */

        //PIECHART
        pieChart = rootView.findViewById(R.id.pieChart);
        //Show all types' percentages
        //TODO select by Time Period
        allSpecificTasks = ldb.getAllSpecificTask();//todo Maybe changed to default: show by month then enable users to choose different range
        pieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
        pieDataSet.setSliceSpace(3f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);//Set data label color
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Time distribution \nBy Month");
        pieChart.setCenterTextSize(33f);

        holeRadius = pieChart.getHoleRadius();//size of button should be

        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.setOnChartGestureListener(this);

        return rootView;
    }

    /*
    Update the data based on the given start and end date
     *///TODO
    private void updatePieChart(Calendar start, Calendar end) {
//        ArrayList<SpecificTask> specificTasks = ldb.findSpecificTasksByTypes();
//        allSpecificTasks.clear();
//        allSpecificTasks = specificTasks;
//        PieDataSet newPieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
//        newPieDataSet.setSliceSpace(3f);
//        PieData newPieData = new PieData(newPieDataSet);
//        pieChart.setData(newPieData);
//        pieChart.notifyDataSetChanged();
//        pieChart.invalidate();
        ArrayList<SpecificTask> a = ldb.findSpecificTasksByTime(start, end);
        System.out.println(a.size());
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

        final ArrayList<Type> selectedtypes = new ArrayList<>();// Instantiate an AlertDialog.Builder with its constructor
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
                            ArrayList<SpecificTask> specificTasks = ldb.findSpecificTasksByTypes(selectedtypes);
                            allSpecificTasks.clear();
                            allSpecificTasks = specificTasks;
                            PieDataSet newPieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
                            newPieDataSet.setSliceSpace(3f);
                            PieData newPieData = new PieData(newPieDataSet);
                            pieChart.setData(newPieData);
                            pieChart.notifyDataSetChanged();
                            pieChart.invalidate();
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
        builder.setView(inflater.inflate(R.layout.piechart_display_range_selection, null))
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatePieChart(Calendar.getInstance(), Calendar.getInstance());//todo change to real one
                    }
                })
                .setNegativeButton("Cancel", null)
                .setNeutralButton(R.string.shownDataInToday, null);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Show by past week todo
        final Button showByWeek = dialog.findViewById(R.id.showByWeek);
        showByWeek.setText(R.string.shownDataInPastWeek);
        showByWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Year");
            }
        });

        //Show by past Month todo
        final Button showByMonth = dialog.findViewById(R.id.showByMonth);
        showByMonth.setText(R.string.shownDataInPastMonth);
        showByMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Month");
            }
        });

        //Show by past week todo
        final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);//Shown Today Only (DEFAULT)
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
