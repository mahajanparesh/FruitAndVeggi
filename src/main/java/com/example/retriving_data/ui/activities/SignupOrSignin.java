package com.example.retriving_data.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.retriving_data.R;

public class SignupOrSignin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_or_signin);
        getSupportActionBar().hide();
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);


        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent loginActivity = new Intent(SignupOrSignin.this, login.class);
                SignupOrSignin.this.startActivity(loginActivity);
            }
        });
        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent signupActivity = new Intent(SignupOrSignin.this, signup.class);
                SignupOrSignin.this.startActivity(signupActivity);
            }
        });

    }
}