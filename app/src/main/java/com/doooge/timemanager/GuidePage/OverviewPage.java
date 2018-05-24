package com.doooge.timemanager.GuidePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doooge.timemanager.R;

/**
 * Created by diana on 2018-05-23.
 */

public class OverviewPage extends AppCompatActivity {
    private Button nextBtn;
    private SharedPreferences pref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_overview_page);
        //isfirststart();


        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OverviewPage.this, TaskMag.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                OverviewPage.this.startActivity(intent);
            }
        });
    }

    private void isfirststart() {
        pref = getSharedPreferences("first", Context.MODE_PRIVATE);

        boolean first = pref.getBoolean("first", true);

        if (first) {
            startActivity(new Intent(OverviewPage.this, TaskMag.class));
        }

        // finish();
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("first", false);
        editor.commit();


    }
}
