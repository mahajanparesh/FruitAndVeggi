package com.example.retriving_data.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.retriving_data.R;

public class SnackBar extends AppCompatActivity {
    Button click;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_bar);
        click = (Button) findViewById(R.id.button);


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(SnackBar.this);
                dialog.show();
                dialog.setContentView(R.layout.dialog_view);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        });
    }


}