package com.example.haleigh.quickfoodformulator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText email;         //login fields
    private EditText password;
    private FirebaseAuth auth;      //authenticating login

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //starting activity

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);        //on login page created in layout

        findViewById(R.id.register).setOnClickListener(this);   //various buttons on login page
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.forgotpassword).setOnClickListener(this);

        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        auth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {       //seeing which button they click on the login page
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, createAccount.class));  //go to createAccount if they click on register instead of login
                break;
        }
        switch (v.getId()) {
            case R.id.login:
                FirebaseSignin(email.getText().toString(), password.getText().toString());  //signing in w/ email and password
                break;
        }
        switch (v.getId()) {
            case R.id.forgotpassword:
                startActivity(new Intent(this, forgotPassword.class));      //go to forgotPassword if they click on forgot password
                break;
        }
    }

    private void FirebaseSignin(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // showProgressDialog();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            System.out.println("LOGIN SUCCESS!");
                            Intent myIntent = new Intent(login.this, MainActivity.class);   //once they're logged in, start main activity
                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            System.out.println("LOGIN FAILED!");
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String newEmail = email.getText().toString();
        if (TextUtils.isEmpty(newEmail)) {      //if they didn't enter an email
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String newPassword = password.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {       //if they didn't enter a password
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

}
