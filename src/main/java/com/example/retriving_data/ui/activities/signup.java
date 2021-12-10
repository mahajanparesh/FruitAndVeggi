package com.example.retriving_data.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import static android.view.View.*;

 public class signup extends AppCompatActivity {
    EditText fname, lname, email, phone, password, cpassword;
    Button signup_btn, back_btn;
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    CheckBox showPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        back_btn = (Button) findViewById(R.id.back_btn);
        signup_btn = (Button) findViewById(R.id.done_btn);
        fname = (EditText) findViewById(R.id.fname_txt);
        lname = (EditText) findViewById(R.id.lname_txt);
        email = (EditText) findViewById(R.id.email_txt);
        phone = (EditText) findViewById(R.id.phone_txt);
        password = (EditText) findViewById(R.id.password_txt);
        cpassword = (EditText) findViewById(R.id.cpassword_txt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showPassword = (CheckBox) findViewById(R.id.showPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        showPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showPassword.isChecked()){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        back_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
        signup_btn.setOnClickListener(new OnClickListener() {
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
                    setValidation();
                    progressBar.setVisibility(VISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Authentication Failed! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setValidation() {
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern digitCase = Pattern.compile("[0-9]");
        if (fname.getText().toString().trim().isEmpty()) {
            fname.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        }
        if (lname.getText().toString().trim().isEmpty()) {
            lname.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        }
        if(!fname.getText().toString().trim().isEmpty() && !lname.getText().toString().trim().isEmpty()) {
            isNameValid = true;
        }

        if (email.getText().toString().trim().isEmpty()) {
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }

        if (phone.getText().toString().trim().isEmpty()) {
            phone.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        } else if (phone.getText().toString().trim().length() != 10) {
            phone.setError(getResources().getString(R.string.error_invalid_phone));
            isPhoneValid = false;
        } else {
            isPhoneValid = true;
        }

        if (password.getText().toString().trim().isEmpty()) {
            password.setError(getResources().getString(R.string.error_Empty_Password));
            isPasswordValid = false;
        } else {
            if (!lowerCase.matcher(password.getText().toString().trim()).find()) {
                password.setError(getResources().getString(R.string.error_LowerCase_Password));
                isPasswordValid = false;
            } else if (!digitCase.matcher(password.getText().toString().trim()).find()) {
                password.setError(getResources().getString(R.string.error_digitCase_Password));
                isPasswordValid = false;
            } else if (password.getText().toString().trim().length() < 8) {
                password.setError(getResources().getString(R.string.length_error));
                isPasswordValid = false;
            }
        }
        if (cpassword.getText().toString().trim().isEmpty()) {
            cpassword.setError(getResources().getString(R.string.error_Empty_CPassword));
            isPasswordValid = false;
        } else if (!password.getText().toString().trim().equals(cpassword.getText().toString())) {
            cpassword.setError(getResources().getString(R.string.error_Cpassword_Match));
            isPasswordValid = false;
        } else if (password.getText().toString().trim().equals(cpassword.getText().toString())) {
            isPasswordValid = true;
        }
        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid) {
            String email_txt = email.getText().toString().trim();
            String password_txt = password.getText().toString().trim();
            final Handler handler = new Handler();

            firebaseAuth.createUserWithEmailAndPassword(email_txt, password_txt)
                    .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(GONE);

                            if (task.isSuccessful()) {
                                User userInfo = new User(firebaseAuth.getUid(), fname.getText().toString().trim(), lname.getText().toString().trim(),email_txt, phone.getText().toString().trim());
                                new FirestoreClass().registerUser(signup.this, userInfo);
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            firebaseAuth.signOut();
                                            Intent signinActivity = new Intent(signup.this, login.class);
                                            signup.this.startActivity(signinActivity);
                                            finish();
                                            for (int i=0; i < 3; i++)
                                            {
                                                Toast.makeText(getApplicationContext(), "Email Send! Please check your Email Address to get Verified.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Failed! Unable to send Email.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                            else{
                                    Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                }

                        }
                    });
        }
    }

}

