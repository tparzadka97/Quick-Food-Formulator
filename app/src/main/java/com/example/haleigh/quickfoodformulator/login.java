package com.example.haleigh.quickfoodformulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class login extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, createAccount.class));
                break;
        }
        switch (v.getId()) {
            case R.id.login:
                //        FirebaseSignin(email.getText().toString(), password.getText().toString());
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

}
