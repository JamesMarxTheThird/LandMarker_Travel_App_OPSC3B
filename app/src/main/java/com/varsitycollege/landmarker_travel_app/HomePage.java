package com.varsitycollege.landmarker_travel_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.goToMaps:
                Intent intent = new Intent(HomePage.this, LandMarkMapPage.class);
                startActivity(intent);
                break;

            case R.id.goToSettings:
                Intent intent2 = new Intent(HomePage.this, SettingsPage.class);
                startActivity(intent2);
                break;

            case R.id.goToFavorites:
                Intent intent3 = new Intent(HomePage.this, FavoritesPage.class);
                startActivity(intent3);
                break;

            case R.id.goToMapRoutes:
                Intent intenti = new Intent(HomePage.this, LandMarkRoutes.class);
                startActivity(intenti);

                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    private Button viewLandBTN;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleOnOff;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggleOnOff = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggleOnOff);
        toggleOnOff.syncState();

        navigationView = findViewById(R.id.nav_view);
        viewLandBTN = findViewById(R.id.viewNarbyBTN);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);


        viewLandBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(HomePage.this, LandMarkMapPage.class);
                startActivity(intent4);
            }
        });
    }


}