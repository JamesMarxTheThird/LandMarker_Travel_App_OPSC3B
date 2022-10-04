package com.varsitycollege.landmarker_travel_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);


    }

    public void GoToLoginPage(View view)
    {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}