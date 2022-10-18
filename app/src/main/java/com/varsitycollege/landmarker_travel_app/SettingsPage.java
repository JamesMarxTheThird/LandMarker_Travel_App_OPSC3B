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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


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
    private Button saveBTN;
    private ImageButton preferrredBTN;
    private Spinner prefspinner;
    private SpinnerAdapter spinAdapt;


    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseDatabase DBREF = FirebaseDatabase.getInstance();
    DatabaseReference LandMarkerRef = DBREF.getReference("Settings");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        prefspinner = findViewById(R.id.prefSpinner);
        //preferrredBTN = findViewById(R.id.prefLandBTN);
        saveBTN = findViewById(R.id.saveButton);
        backArrow = findViewById(R.id.backArrow);
        SecurityRightBTN = findViewById(R.id.SecurityRightBTN);
        TextSizeRightBTN = findViewById(R.id.TextSizeRightBTN);
        LanguageRightBTN = findViewById(R.id.LanguageRightBTN);
        ContactRightBTN = findViewById(R.id.ContactRightBTN);
        AboutRightBTN = findViewById(R.id.AboutRightBTN);
        FAQRightBTN = findViewById(R.id.FAQRightBTN);
        LogoutRightBTN = findViewById(R.id.LogoutRightBTN);
        //PLTRightBTN = findViewById(R.id.PLTRightBTN);

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
        List<String> branchList = new ArrayList<>();
        branchList.add("Ancient");
        branchList.add("Modern");
        branchList.add("Most viewed");

        ArrayAdapter<String> branchListAdapter = new ArrayAdapter<>(SettingsPage.this,
                android.R.layout.simple_spinner_item, branchList);
        branchListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefspinner.setAdapter(branchListAdapter);


        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefspinner.getSelectedItem().equals("Ancient"))
                {
                    try{
                        LandMarkerRef.child("PreferredType").setValue("Ancient");
                    }

                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                    //prefspinner
                }
                else if (prefspinner.getSelectedItem().equals("Modern"))
                {
                    try{
                        LandMarkerRef.child("PreferredType").setValue("Modern");
                    }

                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (prefspinner.getSelectedItem().equals("Most viewed"))
                {
                    try{
                        LandMarkerRef.child("PreferredType").setValue("Most viewed");
                    }

                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


/*
        prefspinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (prefspinner.getSelectedItem().equals("Ancient"))
                {
                    try{
                        LandMarkerRef.child("PreferredType").setValue("Ancient");
                    }

                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                }
                if (prefspinner.getSelectedItem().equals("Modern"))
                {
                    try{
                        LandMarkerRef.child("PreferredType").setValue("Modern");
                    }

                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                }
                if (prefspinner.getSelectedItem().equals("Most viewed"))
                {
                    try{
                        LandMarkerRef.child("PreferredType").setValue("Most viewed");
                    }

                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
*/
/*
        preferrredBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(SettingsPage.this, PreferedLandmarkType.class);
                startActivity(intent2);

            }
        });
*/
        nightModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nightModeSwitch.isChecked())
                {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    //editor = sharedPreferences.edit();
                    //editor.putBoolean("night",false);
                    try {

                        //Changes the 'DisplaysIn' DB field to the string at the end.
                        LandMarkerRef.child("Mode").setValue("Night");
                    }
                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //editor = sharedPreferences.edit();
                    //editor.putBoolean("night",true);

                    try {

                        //Changes the 'DisplaysIn' DB field to the string at the end.
                        LandMarkerRef.child("Mode").setValue("Day");
                    }
                    catch (Exception ex){
                        Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                    }
                }
                //editor.apply();
            }
        });
        //------------------------------------------------------------------------------------------


        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(SettingsPage.this, ProfilePage.class);
                startActivity(intent);

                 */
                //Toast.makeText(SettingsPage.this, "Feature coming soon!", Toast.LENGTH_SHORT).show();

            }
        });

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

        metricSwitch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(metricSwitch.isChecked())
            {
                try {

                    //Changes the 'DisplaysIn' DB field to the string at the end.
                    LandMarkerRef.child("DisplaysIn").setValue("MI");
                }
                catch (Exception ex){
                    Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                try {

                    //Changes the 'DisplaysIn' DB field to the string at the end.
                    LandMarkerRef.child("DisplaysIn").setValue("KM");
                }
                catch (Exception ex){
                    Toast.makeText(SettingsPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                }
            }


        }
        });


        //------------------------------------------------------------------------------------------
        //NAVIGATE TO PREFERED LANDMARK TYPE
        /*
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
        */

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