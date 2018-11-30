package com.example.haleigh.quickfoodformulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText email;     //forgotPassword field
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //starting activity

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);        //on forgot password page created in layout

        email = findViewById(R.id.emailField);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.reset_button).setOnClickListener(this);   //reset password button

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_button:  //button 2 = register button
                sendEmail();     //when they click it, registerUser()
                break;
        }
    }

    public void sendEmail() {

    }
}
