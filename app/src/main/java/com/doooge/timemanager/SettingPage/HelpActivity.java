package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doooge.timemanager.R;

/**
 * Created by fredpan on 2018/1/26.
 */

public class HelpActivity extends AppCompatActivity {

    private Button nextBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpActivityTwo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HelpActivity.this.startActivity(intent);
            }
        });
    }

}
