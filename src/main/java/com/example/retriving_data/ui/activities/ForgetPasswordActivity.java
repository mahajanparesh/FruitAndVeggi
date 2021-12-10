package com.example.retriving_data.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retriving_data.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ForgetPasswordActivity extends AppCompatActivity {
    Button back, done;
    ProgressBar progressBar;
    EditText email;
    boolean isEmailValid;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();
        back = (Button) findViewById(R.id.back_btn);
        done = (Button) findViewById(R.id.done_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        email = (EditText) findViewById(R.id.email_txt2);
        firebaseAuth = FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean have_WIFI = false;
                boolean have_MobileData = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                for(NetworkInfo info:networkInfos){
                    if(info.getTypeName().equalsIgnoreCase("WIFI"))
                        if(info.isConnected())
                            have_WIFI = true;

                    if(info.getTypeName().equalsIgnoreCase("MOBILE"))
                        if(info.isConnected())
                            have_MobileData = true;
                }
                if(have_MobileData || have_WIFI){
                    progressBar.setVisibility(VISIBLE);
                    SetValidation();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void SetValidation() {
        if (email.getText().toString().isEmpty()) {
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        if (isEmailValid) {
            String email_txt = email.getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(email_txt).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(GONE);
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Email sent! To reset your Password.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Failed! You are not registered with this Email ID", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}