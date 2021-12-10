package com.example.retriving_data.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.User;
import com.example.retriving_data.utils.Constants;
import com.example.retriving_data.utils.Extras;
import com.example.retriving_data.utils.GlideLoader;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.VISIBLE;

public class SettingsActivity extends AppCompatActivity {

    ImageView back_btn,user_photo;
    ProgressDialog progressDialog;
    TextView name,gender,email,mobile;
    Button logout, edit;
    private User mUserDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        back_btn = findViewById(R.id.Setting_back);
        user_photo = findViewById(R.id.User_Photo);
        name = findViewById(R.id.Name);
        gender = findViewById(R.id.Gender);
        email = findViewById(R.id.Email);
        mobile = findViewById(R.id.Mobile);
        logout = findViewById(R.id.Logout);
        edit = findViewById(R.id.Edit);

        //back
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(SettingsActivity.this, SignupOrSignin.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        // Edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this, UserProfileActivity.class);
                i.putExtra(Constants.Extra_User_Details,mUserDetails);
                startActivity(i);
            }
        });

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
            progressDialog = new Extras().showProgressBar(this);
            getUserDetails();
        }
        else{
            Toast.makeText(getApplicationContext(), "Unable to load! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserDetails(){
        new FirestoreClass().getUsersDetails(this);

    }

    public void userDetailSuccess(User user){
        mUserDetails = user;
        new GlideLoader(SettingsActivity.this).loadUserPicture(user.image, user_photo);
        name.setText(user.firstName+" "+user.lastName);
        gender.setText(user.gender);
        email.setText(user.email);
        mobile.setText(Long.toString(user.mobile));
        progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }
}