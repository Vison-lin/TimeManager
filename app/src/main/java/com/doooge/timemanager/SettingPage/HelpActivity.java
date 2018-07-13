package com.doooge.timemanager.SettingPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doooge.timemanager.R;

public class HelpActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Button nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpActivityTwo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HelpActivity.this.startActivity(intent);
            }
        });
    }
}
