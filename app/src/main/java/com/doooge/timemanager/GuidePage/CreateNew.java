package com.doooge.timemanager.GuidePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doooge.timemanager.MainPageSlidesAdapter;
import com.doooge.timemanager.R;

/**
 * Created by diana on 2018-05-23.
 */

public class CreateNew extends AppCompatActivity {
    private Button nextBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_create_new);

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CreateNew.this, MainPageSlidesAdapter.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                CreateNew.this.startActivity(intent);
            }
        });
    }

}
