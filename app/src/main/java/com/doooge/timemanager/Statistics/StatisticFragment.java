package com.doooge.timemanager.Statistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
import java.util.Iterator;

/**
 * Created by fredpan on 2018/1/31.
 */

public class StatisticFragment extends Fragment implements OnChartValueSelectedListener, OnChartGestureListener {

    private static PieChart pieChart;
    private static PieDataSet pieDataSet;
    private LinearLayout linearLayout;
    private LocalDatabaseHelper ldb;
    private ImageButton selectPirChartModel;
    private float holeRadius;
    //private TextView pieChartModelSelectionDisplay;
    //Stores all the SpecificTasks with key by type ID.
    private ArrayList<SpecificTask> allSpecificTasks;
    private PieChartHelper pieChartHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.statistic_page, container, false);

        ldb = LocalDatabaseHelper.getInstance(getActivity());
        linearLayout = rootView.findViewById(R.id.typeList);
        pieChartHelper = new PieChartHelper(getActivity());


        /*
        All the charts' data will be refresh only if user add/remove SpecificTask.
        Since add/remove requires users go to new activity, we do not use setUserVisibleHint here!
        All the data will only be refresh at the time when this fragment created.
        We also leave setOffscreenPageLimit() to 1 as default so that statisic page will be refreshed once user goes to the quickAccessTaskFragment.
         */

        //PIECHART
        pieChart = rootView.findViewById(R.id.pieChart);
        //Show all types' percentages
        allSpecificTasks = ldb.getAllSpecificTask();//todo Maybe changed to default: show by month then enable users to choose different range
        pieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
        pieDataSet.setSliceSpace(3f);

        //Draw label outside of pieChart
        pieDataSet.setValueLinePart1OffsetPercentage(90.f);
        pieDataSet.setValueLinePart1Length(.5f);
        pieDataSet.setValueLinePart2Length(.2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setValueTextSize(21f);
        //pieDataSet.setColors(R.color.gray, R.color.yellow); //TODO SetColor

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);//Set data label color
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Time distribution \nBy Month");

        holeRadius = pieChart.getHoleRadius();//size of button should be

        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.setOnChartGestureListener(this);
        pieChartCreation();

        //selectPirChartModel = rootView.findViewById(R.id.selectPieChartModel);
        //selectPirChartModel.setOnClickListener(this);
//        int width= Math.round(holeRadius)*2;
//        int heigth= Math.round(holeRadius)*2;
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, heigth);
//        selectPirChartModel.setLayoutParams(layoutParams);

        //pieChartModelSelectionDisplay = rootView.findViewById(R.id.pieChartModelSelectionDisplay);

        return rootView;
    }

    //PIECHART
    private void pieChartCreation() {

        ArrayList<Type> types = ldb.getAllType();
        Iterator<Type> iterator = types.iterator();

        while (iterator.hasNext()) {
            final Type type = iterator.next();
            final CheckBox ch = new CheckBox(getActivity());
            ch.setChecked(true);

            ch.setText(type.getName());
            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {                                     //if users select a specific type
                        ArrayList<SpecificTask> specificTasks = ldb.findSpecificTasksByType(type);
                        allSpecificTasks.addAll(specificTasks);
                        PieDataSet newPieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
                        newPieDataSet.setSliceSpace(3f);
                        PieData newPieData = new PieData(newPieDataSet);
                        pieChart.setData(newPieData);
                        pieChart.notifyDataSetChanged();
                        pieChart.invalidate();
                    }
                    if (!isChecked) {                                    //if users cancelled the selection of a specific type
                        ArrayList<SpecificTask> newSpecificTasksList = new ArrayList<>();
                        Iterator<SpecificTask> subIterator = allSpecificTasks.iterator();
                        while (subIterator.hasNext()) {
                            SpecificTask specificTaskToBeAdded = subIterator.next();
                            Type typeToBeRemoved = specificTaskToBeAdded.getType();
                            if (typeToBeRemoved.getId() != type.getId()) {// if is not the one to be removed
                                newSpecificTasksList.add(specificTaskToBeAdded);
                            }
                        }
                        allSpecificTasks = null;
                        allSpecificTasks = newSpecificTasksList;
                        pieDataSet = pieChartHelper.calculatePieChart(allSpecificTasks);
                        pieDataSet.setSliceSpace(3f);
                        PieData newPieData = new PieData(pieDataSet);
                        pieChart.setData(newPieData);
                        pieChart.notifyDataSetChanged();
                        pieChart.invalidate();
                    }
                }
            });

            linearLayout.addView(ch);
        }
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

    /*
    OnClikcListener for pieChartSlide
     */
    @Override
    public void onNothingSelected() {

    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        System.out.println("1111");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        System.out.println("2222");
    }

    /*
    OnClikcListener for center button
    */
    @Override
    public void onChartLongPressed(MotionEvent me) {//safely enough to implemented as a button
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.select_shown_data_range_message);

        // Add the buttons
        builder.setPositiveButton(R.string.shownDataByYear, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pieChart.setCenterText("Time distribution \nBy Year");
                pieChart.invalidate();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.shownDataByMonth, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pieChart.setCenterText("Time distribution \nBy Month");
                pieChart.invalidate();
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(R.string.shownDataByDay, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pieChart.setCenterText("Time distribution \nBy Day");
                pieChart.invalidate();
                dialog.dismiss();
            }
        });

        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

        //set positions for three btns:
        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLL);

        final Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLL.gravity = Gravity.CENTER;
        negativeButton.setLayoutParams(negativeButtonLL);

        final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
        neutralButtonLL.gravity = Gravity.CENTER;
        neutralButton.setLayoutParams(neutralButtonLL);
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        System.out.println("4444");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        System.out.println("5555");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        System.out.println("666");
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        System.out.println("777");
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        System.out.println("888");
    }
}
