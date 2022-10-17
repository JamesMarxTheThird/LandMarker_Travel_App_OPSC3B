package com.varsitycollege.landmarker_travel_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPage extends AppCompatActivity {

    private EditText userNameLP;
    private EditText passwordLP;
    private Button loginBTN;
    private FirebaseAuth loginAuthentication;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference LandMarkerDB_Ref = database.getReference("Users");

    //rebased master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        userNameLP = findViewById(R.id.emailET_Reg);
        passwordLP = findViewById(R.id.passwordET_Reg);
        loginBTN = findViewById(R.id.registerButton);

        loginAuthentication = FirebaseAuth.getInstance();

        loginBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    public void GoToRegPage(View view)
    {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    public void loginUser(){

        // Take the value of two edit texts in Strings
        String LP_UN, LP_PW;
        LP_UN = userNameLP.getText().toString();
        LP_PW = passwordLP.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(LP_UN)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(LP_PW)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        loginAuthentication.signInWithEmailAndPassword(LP_UN, LP_PW)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login successful!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    //progressBar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(LoginPage.this,
                                            HomePage.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Login failed!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    //progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }

}