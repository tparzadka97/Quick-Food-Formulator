package com.example.haleigh.quickfoodformulator;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class createAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText firstName;     //createAccount fields
    private EditText lastName;
    private EditText email;
    private FirebaseAuth auth;      //authentication
    private EditText password;
    private EditText confirmPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {        //starting activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firstName = findViewById(R.id.FirstNameField);
        lastName = findViewById(R.id.LastNameField);
        email = findViewById(R.id.EmailField);
        password = findViewById(R.id.PasswordField);
        confirmPass = findViewById(R.id.ConfirmPasswordField);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.button2).setOnClickListener(this);        //button 2 = register button
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:  //button 2 = register button
                registerUser();     //when they click it, registerUser()
                break;
        }
    }

    public void registerUser() {
        final String emailRegister = email.getText().toString();
        final String firstNameRegister = firstName.getText().toString();
        final String lastNameRegister = lastName.getText().toString();
        final String passwordRegister = password.getText().toString();
        final String confirmPassRegister = confirmPass.getText().toString();

        if (emailRegister.isEmpty()) {
            email.setError("Enter a valid email.");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailRegister).matches()) {
            email.setError("Invalid email.");
            email.requestFocus();
            return;
        }

        if (firstNameRegister.isEmpty()) {
            firstName.setError("Enter a valid first name.");
            firstName.requestFocus();
            return;
        }

        if (lastNameRegister.isEmpty()) {
            lastName.setError("Enter a valid last name.");
            lastName.requestFocus();
            return;
        }


        if (passwordRegister.isEmpty()) {
            password.setError("Enter a valid password.");
            password.requestFocus();
            return;
        }

        if (passwordRegister.length() < 6) {
            password.setError("Insufficient password.");
            password.requestFocus();
            return;
        }

        if (confirmPassRegister.isEmpty()) {
            confirmPass.setError("Confirm your password.");
            confirmPass.requestFocus();
            return;
        }

        if (!passwordRegister.equals(confirmPassRegister)) {
            confirmPass.setError("Passwords do not match.");
            confirmPass.requestFocus();
            return;
        }

//Check for email in Database (NOT FUNCTIONING IN ORDER DUE TO FIREBASE THREADING)
        Query emailCheck = FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(emailRegister);
        emailCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {       //contains data from Firebase
                if (dataSnapshot.exists()) {
                    email.setError("Email already in use");
                    email.requestFocus();
                    return;
                } else {
                    final ArrayList<String> foodList = new ArrayList<>();    //if they successfully registered, create their new food list
                    auth.createUserWithEmailAndPassword(emailRegister, passwordRegister)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //updateUsername();
                                        //Create user object for database
                                        User user = new User(firstNameRegister, lastNameRegister, emailRegister, foodList);     //actually registering the user with their new food list
                                        Log.d("Haleigh = ", user.first);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(createAccount.this, "User successfully created.", Toast.LENGTH_LONG).show();     //Toast: this pops up when the user is created
                                                    try {
                                                        Thread.sleep(500);
                                                    } catch (InterruptedException ex) {
                                                        android.util.Log.d("YourApplicationName", ex.toString());
                                                    }
                                                    startActivity(new Intent(createAccount.this, login.class));
                                                } else {        //unable to create user (task.isSuccessful == false)
                                                    Toast.makeText(createAccount.this, "Could not create user.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(createAccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
