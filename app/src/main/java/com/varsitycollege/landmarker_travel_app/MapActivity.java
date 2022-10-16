package com.varsitycollege.landmarker_travel_app;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.varsitycollege.landmarker_travel_app.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }
}