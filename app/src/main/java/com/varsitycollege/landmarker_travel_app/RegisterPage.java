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

public class RegisterPage extends AppCompatActivity {

    private FirebaseAuth registerAuthentication;
    private Button registerBTN;
    private EditText email_R, firstName_R, password_R, confirmPassword_R;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        registerAuthentication = FirebaseAuth.getInstance();

        email_R = findViewById(R.id.emailET_Reg);
        firstName_R = findViewById(R.id.firstNameET_Reg);
        password_R = findViewById(R.id.passwordET_Reg);
        confirmPassword_R = findViewById(R.id.confirmPasswordET_Reg);
        registerBTN = findViewById(R.id.registerButton);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    public void GoToLoginPage(View view)
    {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    public void registerUser() {

        // Take the value of two edit texts in Strings
        String EM, PW, CPW, FN;
        EM = email_R.getText().toString();
        FN = firstName_R.getText().toString();
        PW = password_R.getText().toString();
        CPW = confirmPassword_R.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(EM)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter an email address!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(FN)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter your first name!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(PW)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(CPW) && !(CPW == PW)) {
            Toast.makeText(getApplicationContext(),
                    "Please confirm your password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        try {


            // create new user or register new user
            registerAuthentication
                    .createUserWithEmailAndPassword(EM, PW)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Registration successful!",
                                        Toast.LENGTH_LONG)
                                        .show();

                                // hide the progress bar
                                //progressBar.setVisibility(View.GONE);

                                // if the user created intent to login activity
                                Intent intent
                                        = new Intent(RegisterPage.this,
                                        LoginPage.class);
                                startActivity(intent);
                            } else {

                                // Registration failed
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Registration failed!!"
                                                + " Please try again later",
                                        Toast.LENGTH_LONG)
                                        .show();

                                // hide the progress bar
                                // progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
        catch (Exception e) {

            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }
}