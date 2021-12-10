package com.example.retriving_data.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.retriving_data.R;
import com.example.retriving_data.utils.Constants;

public class UserDetails extends AppCompatActivity {
    TextView tv_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        tv_main = (TextView) findViewById(R.id.tv_main);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "");
        tv_main.setText(username);
        
    }
}