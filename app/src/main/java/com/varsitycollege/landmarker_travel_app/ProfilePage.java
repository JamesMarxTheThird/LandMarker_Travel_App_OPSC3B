package com.varsitycollege.landmarker_travel_app;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


import java.net.URI;

public class ProfilePage extends AppCompatActivity {

    ImageView imageView;

    private ImageView previewPPImage;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private URI imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // initialize imageView
        // with method findViewById()
        imageView = findViewById(R.id.backArrow);

        // Apply OnClickListener  to imageView to
        // switch from one activity to another
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                Intent intent = new Intent(ProfilePage.this, SettingsPage.class);
                startActivity(intent);
            }
        });

    }




}