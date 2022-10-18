package com.varsitycollege.landmarker_travel_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PreferedLandmarkType extends AppCompatActivity {

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefered_landmark_type);

        backArrow = findViewById(R.id.backArrow);


        //------------------------------------------------------------------------------------------
        //EXIT PREFERED LANDMARK TYPE
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                Intent intent = new Intent(PreferedLandmarkType.this, SettingsPage.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------

    }
}