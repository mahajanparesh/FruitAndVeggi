 package com.example.retriving_data.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.User;
import com.example.retriving_data.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class login extends AppCompatActivity {
    EditText email, password;
    Button login_btn, back_btn;
    boolean isEmailValid, isPasswordValid;
    CheckBox showPassword;
    ProgressBar progressBar;
    TextView forget;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        back_btn = (Button) findViewById(R.id.back_btn);
        login_btn = (Button) findViewById(R.id.done_btn);
        email = (EditText) findViewById(R.id.email_txt);
        password = (EditText) findViewById(R.id.password_txt);
        showPassword = (CheckBox) findViewById(R.id.show_Password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        forget = (TextView) findViewById(R.id.forget_txt);
        firebaseAuth = FirebaseAuth.getInstance();
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgetActivity = new Intent(login.this, ForgetPasswordActivity.class);
                login.this.startActivity(forgetActivity);
            }
        });
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPassword.isChecked()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onBackPressed();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
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
                    SetValidation();
                    progressBar.setVisibility(VISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login Failed! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
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

        // Check for a valid password. 
        if (password.getText().toString().isEmpty()) {
            password.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }


        if (isEmailValid && isPasswordValid) {
            String email_txt = email.getText().toString().trim();
            String password_txt = password.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email_txt, password_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(GONE);
                    if(task.isSuccessful()){
                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                            try {
                                Thread.sleep(2000);
                            }
                            catch (Exception e){
                                System.out.println(e);
                            }
                            new FirestoreClass().getUsersDetails(login.this);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Login Failed! Your Email ID is not Verified, Check your Email to get verified.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Login Failed! Check your Email ID or Password.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }
    public void userLoggedInSuccess(User user){
        if(user.profileCompleted == 0){
            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent doneActivity = new Intent(login.this, UserProfileActivity.class);
            doneActivity.putExtra(Constants.Extra_User_Details, user);
            login.this.startActivity(doneActivity);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent doneActivity = new Intent(login.this, DashboardActivity.class);
            login.this.startActivity(doneActivity);
            finish();
        }
    }
}