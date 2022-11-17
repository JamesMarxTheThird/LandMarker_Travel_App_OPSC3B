package com.varsitycollege.landmarker_travel_app;

import static android.content.ContentValues.TAG;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.varsitycollege.landmarker_travel_app.databinding.ActivityLandMarkMapPageBinding;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LandMarkMapPage extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap gMap;
    private CameraPosition cameraPosition;
    private ActivityLandMarkMapPageBinding binding;
    boolean LocationPermission;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location lastKnownLocation;

    //FusedLocationProviderClient
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng CapeTown = new LatLng(-33.9803833, 18.4759092);
    private final LatLng sydney = new LatLng(-34, 151);

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleOnOff;
    private NavigationView navigationView;

    private Button findNearbyLocations;
    private double lat, lng;

    private String address;
    private String city;

    private Spinner spType;
    private String spTypeSelected;
    private List<String> typeList;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference LandMarkerRef = database.getReference("Settings");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get last saved location
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        binding = ActivityLandMarkMapPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Places initialize
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);

        //fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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

        findNearbyLocations = findViewById(R.id.findNearbyLocations);

        typeList = new ArrayList<>();
        spType = findViewById(R.id.spnLocationType);

        //ArrayAdapter<String> test = new ArrayAdapter<String>(LandMarkMapPage.this, typeList);
        //spnAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        //spType.setAdapter(spnAdapter);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this, R.array.type_array, android.R.layout.simple_spinner_dropdown_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spType.setAdapter(spnAdapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findNearbyLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateUI();
                spinnerSelectedItem();

                StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

                stringBuilder.append("location=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
                stringBuilder.append("&radius=10000");
                stringBuilder.append("&type=" + spTypeSelected);
                stringBuilder.append("&sensor=true");
                //stringBuilder.append("&keyword=cruise");
                stringBuilder.append("&key=" + getResources().getString(R.string.maps_api_key));

                String url = stringBuilder.toString();
                Object dataFetch[] = new Object[2];
                dataFetch[0] = gMap;
                dataFetch[1] = url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);

                gMap.setInfoWindowAdapter(new InfoWindowAdapter(LandMarkMapPage.this));
            }
        });

        LandMarkerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String mode = snapshot.child("Mode").getValue(String.class);

                if (mode.equals("Day")){
                        gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(LandMarkMapPage.this, R.raw.light_mode_json));

                    } else {
                        gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(LandMarkMapPage.this, R.raw.dark_mode_json));
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LandMarkMapPage.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void spinnerSelectedItem() {

        if (spType.getSelectedItem().toString().equals("Restaurant")){
            spTypeSelected = "restaurant";

        } else if (spType.getSelectedItem().equals("Tourist Attraction")){
            spTypeSelected = "tourist_attraction";

        } else if (spType.getSelectedItem().equals("Point of Interest")) {
            spTypeSelected = "point_of_interest";

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overlay_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.findNearbyLocations) {

            //Toast.makeText(LandMarkMapPage.this, "test", Toast.LENGTH_SHORT).show();

            //showCurrentPlace();
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        getLocationPermission();
        updateUI();
        getDeviceLocation();

        //gMap.setInfoWindowAdapter(new InfoWindowAdapter(LandMarkMapPage.this));
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationPermission = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocationPermission = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationPermission = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateUI();
    }

    private void updateUI() {
        if (gMap == null) {
            return;
        }
        try {
            if (LocationPermission == true) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
                gMap.addMarker(new MarkerOptions().position(CapeTown).title("Marker in Sydney"));
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (LocationPermission) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 15));
                                gMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude())).title("Your Location").snippet("You are here!"));
                                //gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            gMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(CapeTown, 15));
                            gMap.getUiSettings().setMyLocationButtonEnabled(false);

                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void showCurrentPlace() {
        if (gMap == null) {
            return;
        }

        if (LocationPermission) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        LandMarkMapPage.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            gMap.addMarker(new MarkerOptions()
                    .title("Default Marker")
                    .position(sydney)
                    .snippet("Default snippet"));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                gMap.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        15));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nearby Landmarks")
                .setItems(likelyPlaceNames, listener)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {

            case R.id.goToHome:
                Intent intent = new Intent(LandMarkMapPage.this, HomePage.class);
                startActivity(intent);

                break;

            case R.id.goToMaps:
                Intent intent3 = new Intent(LandMarkMapPage.this, LandMarkMapPage.class);
                startActivity(intent3);

                break;

            case R.id.goToSettings:
                Intent intent4 = new Intent(LandMarkMapPage.this, SettingsPage.class);
                startActivity(intent4);

                break;

            case R.id.goToProfilePage:
                Intent intent5 = new Intent(LandMarkMapPage.this, ProfilePage.class);
                startActivity(intent5);

                break;

            case R.id.goToFavorites:
                Intent intent6 = new Intent(LandMarkMapPage.this, FavoritesPage.class);
                startActivity(intent6);

                break;

            case R.id.goToMapRoutes:
                Intent intent7 = new Intent(LandMarkMapPage.this, LandMarkRoutes.class);
                startActivity(intent7);

                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void Geocode(double lat, double lng) {

        //code used from https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}