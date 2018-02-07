package com.doooge.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.doooge.timemanager.SettingPage.SettingActivity;

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

    //TODO To be deleted: Facked Calendar (搜索条件)
    private Calendar calendar;
    //================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_task_overview, container, false);

        ldh = new LocalDatabaseHelper(getActivity());

        //Assign button listeners to here
        //Button addBtn = rootView.findViewById(R.id.addBtn);
        ImageView settingBtn = rootView.findViewById(R.id.settingBtn);
        //Button delBtn = rootView.findViewById(R.id.delBtn);
        //addBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        // delBtn.setOnClickListener(this);

        //TODO To be deleted: Facked Calendar (搜索条件)
        calendar = Calendar.getInstance();//faked calendar
        calendar.set(2010, 0, 01);
        specificTasks = ldh.specificTasksSortByStartTime(calendar);//search all specificTasks that start at the faked time
        //================================================


        adapter = new SpecificTaskOverviewAdapter(specificTasks, ldh, getActivity());
        mListView = rootView.findViewById(R.id.taskList);
        mListView.setAdapter(adapter);


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO ====
                return false;
            }
        });




        return rootView;
    }

    @Override
    public void onClick(View view) {


        //TODO To be deleted: Faked Type
        Type type = new Type("Type", "Color");
        type.setId(1);
        ldh.insertToTypeTable(type);//Unnecessary here
        //================================================

        //TODO SAMPLE: ADD AND DELETE FROM DB!
        switch (view.getId()) {

            case R.id.settingBtn:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            //IN CASE ADDING NEW SpecificTasks to Adapter
            //TODO Vison's part:
//            case R.id.addBtn:
//
//                //TODO To be deleted: Faked
//                SpecificTask specificTaskaa = new SpecificTask("Test1", calendar, calendar);
//                specificTaskaa.setType(type);
//                //===========================
//
//                ldh.insertToSpecificTaskTable(specificTaskaa);//Insert new SpecificTask into dB //TODO Change faked to real one
//                ldh.showAllData(getActivity());//TODO To be deleted: Showing dB
//                specificTasks.add(specificTaskaa);//Insert new SpecificTask into local ArrayList //TODO Change faked to real one
//                adapter.updateSpecificTaskOverviewAdapter(specificTasks);//Update
//                break;
//
//            //IN CASE DELETING A Specific SpecificTasks to Adapter
//            case R.id.delBtn:
//                SpecificTask specificTaskToBeDeleted = ldh.specificTasksSortByStartTime(calendar).iterator().next();//TODO To be deleted: Faked
//                int id = specificTaskToBeDeleted.getId();
//                Iterator<SpecificTask> iterator = specificTasks.iterator();
//                boolean notFind = true;
//                while (iterator.hasNext() && notFind) {
//                    SpecificTask specificTask = iterator.next();
//                    if (specificTask.getId() == id) {
//                        specificTasks.remove(specificTask);
//                        ldh.deleteSpecificTaskTable(id);
//                        ldh.showAllData(getActivity());
//                        notFind = false;
//                    }
//                }
//                if (!notFind) {
//                    adapter.updateSpecificTaskOverviewAdapter(specificTasks);
//                } else {
//                    throw new NoSuchElementException();
//                }
//                break;
        }

    }
}
