package com.doooge.timemanager.GuidePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doooge.timemanager.R;

public class TypeControl extends AppCompatActivity {
    private Button nextBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_type_cont);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TypeControl.this, CreateNew.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                TypeControl.this.startActivity(intent);
            }
        });
    }
}
