package com.varsitycollege.landmarker_travel_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Map;

public class MapPermissions extends AppCompatActivity {

    private Button grantPermBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_permissions);

        grantPermBTN = findViewById(R.id.btnGrantPerm);

        if(ContextCompat.checkSelfPermission(MapPermissions.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(MapPermissions.this, MapActivity.class));
            finish();
            return;
        }

        grantPermBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(MapPermissions.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                startActivity(new Intent(MapPermissions.this, MapActivity.class));
                                finish();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if(permissionDeniedResponse.isPermanentlyDenied()){
                                    AlertDialog.Builder blder = new AlertDialog.Builder(MapPermissions.this);
                                    blder.setTitle("Permission Denied")
                                            .setMessage("Can't access your device location. Go to your settings to allow the permission")
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent();
                                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    i.setData((Uri.fromParts("package", getPackageName(), null)));
                                                }
                                            })
                                            .show();
                                } else {
                                    Toast.makeText(MapPermissions.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });

    }
}