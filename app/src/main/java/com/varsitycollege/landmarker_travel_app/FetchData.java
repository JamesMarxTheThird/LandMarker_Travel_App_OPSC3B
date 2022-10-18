package com.varsitycollege.landmarker_travel_app;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchData extends AsyncTask<Object, String, String> {

    String googleNearbyPlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {

        try {

            googleMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googleNearbyPlacesData = downloadUrl.retrieveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  googleNearbyPlacesData;
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject getLocation = jsonObject1.getJSONObject("geometry")
                        .getJSONObject("location");

                String lat = getLocation.getString("lat");
                String lng = getLocation.getString("lng");

                JSONObject getName = jsonArray.getJSONObject(i);
                String name = getName.getString("name");

                JSONObject getRating = jsonArray.getJSONObject(i);
                String rating = getRating.getString("rating");

                String snippet = "Rating: " + rating + "\n"
                        + "Latitude " + lat + "\n"
                        + "Longitude " + lng;

                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name).position(latLng).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
