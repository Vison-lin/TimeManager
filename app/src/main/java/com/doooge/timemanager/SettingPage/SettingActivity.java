package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.doooge.timemanager.R;

/**
 * Created by fredpan on 2018/1/26.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
    }

    /**
     * Implementing all onClick actions here with the presented format.
     * @param view ignore
     */
    @Override
    public void onClick(View view) {
        Class intentClass = this.getClass();
        switch (view.getId()) {
            case R.id.helpPageBtn:
                intentClass = HelpActivity.class;
                break;
            case R.id.typesManagementBtn:
                intentClass = TypeManagementActivity.class;
                //intentClass = TypeCreator.class;
                break;
            case R.id.tasksManagementBtn:
                intentClass = TaskManagementActivity.class;
                break;
        }

        Intent intent = new Intent(this, intentClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

}
