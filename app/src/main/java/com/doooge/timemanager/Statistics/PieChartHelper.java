package com.doooge.timemanager.Statistics;

import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;

import com.doooge.timemanager.CalendarHelper;
import com.doooge.timemanager.LocalDatabaseHelper;
import com.doooge.timemanager.SpecificTask;
import com.doooge.timemanager.Type;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by fredpan on 2018-02-08.
 */

class PieChartHelper {

    private final Activity activity;
    private PieDataSet pieDataSet;
    private ArrayList<SpecificTask> specificTasks;

    PieChartHelper(Activity activity) {
        this.activity = activity;
    }

    PieDataSet calculatePieChart(ArrayList<SpecificTask> specificTasks) {
        ArrayList<Pair<Type, Float>> specificTasksWithPercentage = calPercentage(specificTasks);
        Iterator<Pair<Type, Float>> iterator = specificTasksWithPercentage.iterator();

        ArrayList<Integer> colors = new ArrayList<>();
        List<PieEntry> pieEntry = new ArrayList<>();
        while (iterator.hasNext()) {
            Pair<Type, Float> next = iterator.next();
            Type temp = next.first;
            float percentage = next.second;
            pieEntry.add(new PieEntry(percentage, temp.getName(), temp));
            colors.add(Integer.parseInt(next.first.getColor()));
        }
        pieDataSet = new PieDataSet(pieEntry, null);
        //Draw label outside of pieChart
        pieDataSet.setValueLinePart1OffsetPercentage(90.f);
        pieDataSet.setValueLinePart1Length(.5f);
        pieDataSet.setValueLinePart2Length(.2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setValueTextSize(21f);
        pieDataSet.setColors(colors);
        return pieDataSet;
    }

    private ArrayList<Pair<Type, Float>> calPercentage(ArrayList<SpecificTask> rawSpecificTasks) {

        Iterator<SpecificTask> iterator = rawSpecificTasks.iterator();
        specificTasks = new ArrayList<>();
        //Only calculate the completed tasks
        while (iterator.hasNext()){
            SpecificTask curr = iterator.next();
            if (curr.isCompletedInBoolean()) {
                specificTasks.add(curr);
            }
        }
        Iterator<SpecificTask> iterator1 = specificTasks.iterator();
        ArrayList<Pair<Type, Float>> specificTasksWithPercentage = new ArrayList<>();
        long totalTimeInMillis = 0;
        //Calculate the total time
        while (iterator1.hasNext()) {
            SpecificTask specificTask = iterator1.next();
            totalTimeInMillis = totalTimeInMillis + CalendarHelper.durationOfStartAndEndTimeInMillis(specificTask.getStartTime(), specificTask.getEndTime());
        }
        //Calculate the total time for each type
        Iterator<SpecificTask> iterator2 = specificTasks.iterator();
        HashMap<Integer, Long> specificTaskPercentageGroupByType = new HashMap<>();
        while (iterator2.hasNext()) {
            SpecificTask specificTask = iterator2.next();
            Type type = specificTask.getType();
            if (specificTaskPercentageGroupByType.containsKey(type.getId())) {//if already selectedStartCal to count that type
                Long newTotalTime = specificTaskPercentageGroupByType.get(type.getId()) + CalendarHelper.durationOfStartAndEndTimeInMillis(specificTask.getStartTime(), specificTask.getEndTime());
                specificTaskPercentageGroupByType.remove(type.getId());
                specificTaskPercentageGroupByType.put(type.getId(), newTotalTime);
            } else {
                specificTaskPercentageGroupByType.put(type.getId(), CalendarHelper.durationOfStartAndEndTimeInMillis(specificTask.getStartTime(), specificTask.getEndTime()));
            }
        }
        //Calculate the percentage for each type
        Iterator<Map.Entry<Integer, Long>> iterator3 = specificTaskPercentageGroupByType.entrySet().iterator();
        while (iterator3.hasNext()) {
            Map.Entry<Integer, Long> tmp = iterator3.next();
            if (totalTimeInMillis != 0) {
                LocalDatabaseHelper ldb = LocalDatabaseHelper.getInstance(activity);
                Type type = ldb.findTypeByPrimaryKey(tmp.getKey());
                Long totalTimeOfType = tmp.getValue();
                Float percentage = ((float) totalTimeOfType / totalTimeInMillis);
                specificTasksWithPercentage.add(new Pair<Type, Float>(type, percentage));
            }

        }


        return specificTasksWithPercentage;

    }

}
