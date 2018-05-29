package com.doooge.timemanager;

/**
 * Created by fredpan on 2018/1/26.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.doooge.timemanager.GuidePage.IntroPage;
import com.doooge.timemanager.Statistics.StatisticFragment;

import java.util.ArrayList;
import java.util.List;

public class MainPageSlidesAdapter extends FragmentActivity {


    /**
     * The number of pages (3) to show.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        //Force make second page (SpecificTaskOverviewFragment) as the default displayed main page
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mPager.setCurrentItem(1);
            }
        });


        pref = getSharedPreferences("first", Context.MODE_PRIVATE);

        boolean first = pref.getBoolean("first", true);
        if (first) {
            startActivity(new Intent(MainPageSlidesAdapter.this, IntroPage.class));


            //finish();
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("first", false);
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 1) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    /**
     * A simple pager adapter that represents 3 ScreenSlidePageFragment objects, in sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        List<String> fragments = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(StatisticFragment.class.getName());
            fragments.add(SpecificTaskOverviewFragment.class.getName());
            fragments.add(QuickAccessTaskFragment.class.getName());
        }

        @Override
        public Fragment getItem(int position) {

            return Fragment.instantiate(getApplicationContext(), fragments.get(position));
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
