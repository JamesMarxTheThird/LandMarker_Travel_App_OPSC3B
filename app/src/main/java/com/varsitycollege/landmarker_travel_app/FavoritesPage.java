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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPage extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleOnOff;
    private NavigationView navigationView;
    private Button addToFavorties;
    private EditText newMonumnet;
    private String newFav;
    public String otherFav;
    private ListView favList;
    public List<String> favouritesArrayList;
    public ArrayAdapter<String> favALAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference LandMarkerRef = database.getReference("Favorites");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_page);

        favouritesArrayList = new ArrayList<>();

        addToFavorties = findViewById(R.id.favoritesBTN);
        newMonumnet = findViewById(R.id.landmarkName);
        favList = findViewById(R.id.favsLV);

        toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggleOnOff = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggleOnOff);
        toggleOnOff.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);


        addToFavorties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newFav = newMonumnet.getText().toString();
                try {

                    LandMarkerRef.push().setValue(newFav);
                    //myRef.setValue("Hello, World!");

                } catch (Exception ex) {
                    Toast.makeText(FavoritesPage.this, "Could not add landmark", Toast.LENGTH_SHORT).show();
                }

            }
        });


        LandMarkerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    favouritesArrayList.add(ds.getValue(String.class));
                    Toast.makeText(FavoritesPage.this, otherFav, Toast.LENGTH_SHORT).show();
                }

                favALAdapter = new ArrayAdapter<String>(FavoritesPage.this, android.R.layout.simple_list_item_1, favouritesArrayList);
                favList.setAdapter(favALAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoritesPage.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });
        //Supposed to add tb to LV aswell but is buggy

    }

    /*
//Above on create
   FirebaseDatabase DBREF = FirebaseDatabase.getInstance();
    DatabaseReference LandMarkerRef = DBREF.getReference("Settings");

//In on create

            addToFavorties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        //Textbox
                newFav = newMonumnet.getText().toString();
                try {

                    //Changes the 'DisplaysIn' DB field to the string at the end.
                    LandMarkerRef.child("DisplaysIn").setValue("MI");
                }
                catch (Exception ex){
                    Toast.makeText(FavoritesPage.this, "didnt add value to DB", Toast.LENGTH_SHORT).show();
                }

            }
        });

     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.goToHome:
                Intent intent = new Intent(FavoritesPage.this, HomePage.class);
                startActivity(intent);

                break;

            case R.id.goToMaps:
                Intent intent3 = new Intent(FavoritesPage.this, LandMarkMapPage.class);
                startActivity(intent3);

                break;

            case R.id.goToSettings:
                Intent intent4 = new Intent(FavoritesPage.this, SettingsPage.class);
                startActivity(intent4);

                break;

            case R.id.goToProfilePage:
                Intent intent5 = new Intent(FavoritesPage.this, ProfilePage.class);
                startActivity(intent5);

                break;

            case R.id.goToFavorites:
                Intent intent6 = new Intent(FavoritesPage.this, FavoritesPage.class);
                startActivity(intent6);

                break;



        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}
