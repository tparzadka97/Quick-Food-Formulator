package com.example.haleigh.quickfoodformulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;

public class createAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private FirebaseAuth auth;
    private EditText password;
    private EditText confirmPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firstName = findViewById(R.id.FirstNameField);
        lastName = findViewById(R.id.LastNameField);
        email = findViewById(R.id.EmailField);
        password = findViewById(R.id.PasswordField);
        confirmPass = findViewById(R.id.ConfirmPasswordField);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.button2).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                registerUser();
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

        if (!password.equals(confirmPassRegister)) {
            confirmPass.setError("Passwords do not match.");
            confirmPass.requestFocus();
            return;
        }
    }

}
