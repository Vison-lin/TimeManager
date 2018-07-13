package com.doooge.timemanager.Statistics;

import android.annotation.SuppressLint;
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
import java.util.concurrent.TimeUnit;

class PieChartHelper {

    private final Activity activity;

    PieChartHelper(Activity activity) {
        this.activity = activity;
    }

    PieDataSet calculatePieChart(ArrayList<SpecificTask> specificTasks) {
        ArrayList<Pair<Pair<Type, Float>, Float>> specificTasksWithPercentage = calPercentage(specificTasks);
        Iterator<Pair<Pair<Type, Float>, Float>> iterator = specificTasksWithPercentage.iterator();

        ArrayList<Integer> colors = new ArrayList<>();
        List<PieEntry> pieEntry = new ArrayList<>();
        while (iterator.hasNext()) {
            Pair<Pair<Type, Float>, Float> next = iterator.next();
            Type temp = next.first.first;
            float percentage = next.first.second;
            float totalTime = next.second;
            pieEntry.add(new PieEntry(percentage, temp.getName() + ": \n" + convertMillisToHours(totalTime), temp));
            colors.add(Integer.parseInt(next.first.first.getColor()));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntry, null);
        //Draw label outside of pieChart
        pieDataSet.setValueLinePart1OffsetPercentage(90.f);
        pieDataSet.setValueLinePart1Length(.5f);
        pieDataSet.setValueLinePart2Length(.2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setValueTextSize(21f);
        pieDataSet.setColors(colors);
        return pieDataSet;
    }

    private ArrayList<Pair<Pair<Type, Float>, Float>> calPercentage(ArrayList<SpecificTask> rawSpecificTasks) {

        ArrayList<Pair<Pair<Type, Float>, Float>> specificTasksWithPercentage = new ArrayList<>();

        Iterator<SpecificTask> iterator = rawSpecificTasks.iterator();
        ArrayList<SpecificTask> specificTasks = new ArrayList<>();
        //Only calculate the completed tasks
        while (iterator.hasNext()){
            SpecificTask curr = iterator.next();
            if (curr.isCompletedInBoolean()) {
                specificTasks.add(curr);
            }
        }
        Iterator<SpecificTask> iterator1 = specificTasks.iterator();
        long totalTimeInMillis = 0;
        //Calculate the total time
        while (iterator1.hasNext()) {
            SpecificTask specificTask = iterator1.next();
            totalTimeInMillis = totalTimeInMillis + CalendarHelper.durationOfStartAndEndTimeInMillis(specificTask.getStartTime(), specificTask.getEndTime());
        }
        //Calculate the total time for each type
        Iterator<SpecificTask> iterator2 = specificTasks.iterator();
        @SuppressLint("UseSparseArrays") HashMap<Integer, Long> specificTaskPercentageGroupByType = new HashMap<>();
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
        for (Map.Entry<Integer, Long> tmp : specificTaskPercentageGroupByType.entrySet()) {
            if (totalTimeInMillis != 0) {
                LocalDatabaseHelper ldb = LocalDatabaseHelper.getInstance(activity);
                Type type = ldb.findTypeByPrimaryKey(tmp.getKey());
                Long totalTimeOfType = tmp.getValue();
                Float percentage = ((float) totalTimeOfType / totalTimeInMillis);
                specificTasksWithPercentage.add(new Pair<>(new Pair<>(type, percentage), (float) totalTimeOfType));
            }

        }


        return specificTasksWithPercentage;

    }

    @SuppressLint("DefaultLocale")
    private String convertMillisToHours(float timeInMillis) {
        return String.format("%02dh %02dm",
                TimeUnit.MILLISECONDS.toHours((long) timeInMillis),//total mills in hours
                TimeUnit.MILLISECONDS.toMinutes((long) timeInMillis) -//total mills in minutes - total (of total mills in hours) in minutes
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) timeInMillis))
        );
    }

}
