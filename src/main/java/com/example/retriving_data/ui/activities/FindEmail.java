package com.example.retriving_data.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retriving_data.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FindEmail extends AppCompatActivity {
    EditText emailtxt;
    Button find;
    FirebaseFirestore dbroot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_email);
        emailtxt = (EditText) findViewById(R.id.emailtxt);
        find = (Button) findViewById(R.id.find);
        dbroot = FirebaseFirestore.getInstance();
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertdata();
            }
        });
    }
    public void insertdata(){
        Map<String,String> items = new HashMap<>();
        items.put("name",emailtxt.getText().toString().trim());
        dbroot.collection("students").add(items)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        emailtxt.setText("");
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                    }
                });
    }
}