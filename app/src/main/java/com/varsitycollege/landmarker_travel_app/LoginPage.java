package com.varsitycollege.landmarker_travel_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void GoToRegPage(View view)
    {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }
}