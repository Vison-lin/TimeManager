package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doooge.timemanager.MainPageSlidesAdapter;
import com.doooge.timemanager.R;

public class HelpActivityTwo extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help2);
        Button nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivityTwo.this, MainPageSlidesAdapter.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HelpActivityTwo.this.startActivity(intent);
            }
        });
    }
}
