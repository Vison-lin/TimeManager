package com.doooge.timemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;

/**
 * Created by fredpan on 2018/1/31.
 */

public class StatisticFragment  extends Fragment {

    BarChart barChart;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.statistic_page, container, false);
        barChart = rootView.findViewById(R.id.barChart);
        return rootView;
    }

}
