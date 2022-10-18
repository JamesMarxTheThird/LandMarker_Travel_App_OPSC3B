package com.varsitycollege.landmarker_travel_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;


public class SettingsPage extends AppCompatActivity {


    ImageView backArrow;
    ImageView SecurityRightBTN;
    ImageView TextSizeRightBTN;
    ImageView LanguageRightBTN;
    ImageView ContactRightBTN;
    ImageView AboutRightBTN;
    ImageView FAQRightBTN;
    ImageView LogoutRightBTN;
    ImageView PLTRightBTN;


    private AppCompatButton editProfileBtn;
    private SwitchCompat metricSwitch;
    private SwitchCompat nightModeSwitch;
    private SwitchCompat notificationSwitch;


    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        backArrow = findViewById(R.id.backArrow);
        SecurityRightBTN = findViewById(R.id.SecurityRightBTN);
        TextSizeRightBTN = findViewById(R.id.TextSizeRightBTN);
        LanguageRightBTN = findViewById(R.id.LanguageRightBTN);
        ContactRightBTN = findViewById(R.id.ContactRightBTN);
        AboutRightBTN = findViewById(R.id.AboutRightBTN);
        FAQRightBTN = findViewById(R.id.FAQRightBTN);
        LogoutRightBTN = findViewById(R.id.LogoutRightBTN);
        PLTRightBTN = findViewById(R.id.PLTRightBTN);

        editProfileBtn = findViewById(R.id.editProfileBtn);
        metricSwitch = findViewById(R.id.metricSwitch);
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        notificationSwitch = findViewById(R.id.notificationSwitch);


        //------------------------------------------------------------------------------------------
        //NIGHT MODE SWITCH

        //WE USED SHAREDPREFERENCES TO SAVE CHANGES AFTER APP HAS RESTARTED ON EXIT
        //sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        //nightMode = sharedPreferences.getBoolean("night",false); //LIGHT MODE - SET DEFAULT
        //if(nightMode){
            //nightModeSwitch.setChecked(true);
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        //}

        nightModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nightModeSwitch.isChecked())
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    //editor.putBoolean("night",false);
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    //editor.putBoolean("night",true);
                }
                editor.apply();
            }
        });
        //------------------------------------------------------------------------------------------




        //------------------------------------------------------------------------------------------
        //EXIT SETTINGS
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                Intent intent = new Intent(SettingsPage.this, LandMarkMapPage.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------


        //------------------------------------------------------------------------------------------
        //NAVIGATE TO PREFERED LANDMARK TYPE
        PLTRightBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                Intent intent = new Intent(SettingsPage.this, PreferedLandmarkType.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------


        //------------------------------------------------------------------------------------------
        //GO TO EDIT USER PROFILE PAGE
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                Intent intent = new Intent(SettingsPage.this, ProfilePage.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------


        //------------------------------------------------------------------------------------------
        //LOGGING OUT OF FIREBASE
        LogoutRightBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut(); //LOGOUT
                startActivity(new Intent(getApplicationContext(),LoginPage.class));
                finish();

            }
        });
        //------------------------------------------------------------------------------------------

    }

}