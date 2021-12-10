package com.example.retriving_data.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.User;
import com.example.retriving_data.utils.Constants;
import com.example.retriving_data.utils.Extras;
import com.example.retriving_data.utils.GlideLoader;
import com.example.retriving_data.utils.RadioButton;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {
    ImageView back_btn, done2, userPhoto;
    EditText firstname, lastname, emailid, mobile;
    RadioButton rb_male,rb_female;
    Button done;
    ConstraintLayout mainLayout;
    Boolean isNameValid, isPhoneValid;
    ProgressDialog dialog;
    private String userProfileImageURL = "";
    private Uri mSelectedImageFileUri = null;
    private  User userDetails = new User();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();
        back_btn = (ImageView) findViewById(R.id.EditProfile_back);
        firstname = (EditText) findViewById(R.id.EditFirstName);
        lastname = (EditText) findViewById(R.id.EditLastName);
        emailid = (EditText) findViewById(R.id.EditEmail);
        mobile = (EditText) findViewById(R.id.EditMobileNo);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        done = (Button) findViewById(R.id.Edit_Done_Btn);
        done2 = (ImageView) findViewById(R.id.Edit_Done_Btn2);
        userPhoto = (ImageView) findViewById(R.id.Edit_User_Photo);
        mainLayout = findViewById(R.id.Edit_mainLayout);
        if (getIntent().hasExtra(Constants.Extra_User_Details)) {
            userDetails = getIntent().getParcelableExtra(Constants.Extra_User_Details);
        }

        firstname.setText(userDetails.firstName);

        lastname.setText(userDetails.lastName);

        emailid.setEnabled(false);
        emailid.setText(userDetails.email);
        new GlideLoader(this).loadUserPicture(userDetails.image, userPhoto);
        if(userDetails.gender != null){
            System.out.println(userDetails.gender);
            if(userDetails.gender.equals("female")){
                rb_female.setChecked(true);
            }

        }



        mobile.setText(String.valueOf(userDetails.mobile));

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean have_WIFI = false;
                boolean have_MobileData = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                for (NetworkInfo info : networkInfos) {
                    if (info.getTypeName().equalsIgnoreCase("WIFI"))
                        if (info.isConnected())
                            have_WIFI = true;
                    if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (info.isConnected())
                            have_MobileData = true;
                }
                if (have_MobileData || have_WIFI) {
                    if (ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        /*Snackbar.make(mainLayout, "You already have the storage permission", Snackbar.LENGTH_LONG).show();*/


                        progressDialog = new Extras().showProgressBar(UserProfileActivity.this);

                        Constants.showImageChooser(UserProfileActivity.this);


                    } else {
                        ActivityCompat.requestPermissions(UserProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_STORAGE_PERMISSION_CODE);
                    }
                    
                } else {
                    Toast.makeText(getApplicationContext(), "Error! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean have_WIFI = false;
                boolean have_MobileData = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                for (NetworkInfo info : networkInfos) {
                    if (info.getTypeName().equalsIgnoreCase("WIFI"))
                        if (info.isConnected())
                            have_WIFI = true;
                    if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (info.isConnected())
                            have_MobileData = true;
                }
                if (have_MobileData || have_WIFI) {
                    dialog = new ProgressDialog(UserProfileActivity.this);
                    dialog.show();
                    dialog.setContentView(R.layout.dialog_view);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    setValidations();

                } else {
                    Toast.makeText(getApplicationContext(), "Error! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        done2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean have_WIFI = false;
                boolean have_MobileData = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                for (NetworkInfo info : networkInfos) {
                    if (info.getTypeName().equalsIgnoreCase("WIFI"))
                        if (info.isConnected())
                            have_WIFI = true;
                    if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (info.isConnected())
                            have_MobileData = true;
                }
                if (have_MobileData || have_WIFI) {
                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    dialog = new ProgressDialog(UserProfileActivity.this);
                    dialog.show();
                    dialog.setContentView(R.layout.dialog_view);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                    setValidations();
                } else {
                    Toast.makeText(getApplicationContext(), "Error! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void setValidations(){
        if (firstname.getText().toString().trim().isEmpty()) {
            firstname.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
            dialog.dismiss();
        }
        if (lastname.getText().toString().trim().isEmpty()) {
            lastname.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
            dialog.dismiss();
        }
        if(!firstname.getText().toString().trim().isEmpty() && !lastname.getText().toString().trim().isEmpty()) {
            isNameValid = true;
        }
        if (mobile.getText().toString().trim().isEmpty()) {
            mobile.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
            dialog.dismiss();
        } else if (mobile.getText().toString().trim().length() != 10) {
            mobile.setError(getResources().getString(R.string.error_invalid_phone));
            isPhoneValid = false;
            dialog.dismiss();
        } else {
            isPhoneValid = true;
        }


        if(isNameValid && isPhoneValid == true){
            HashMap<String, Object> userHashMap = new HashMap<>();

            String fname = firstname.getText().toString().trim();
            String lname = lastname.getText().toString().trim();
            String mobileNo = mobile.getText().toString().trim();
            Long mobileNumber = Long.parseLong(mobileNo);
            String gender;
            if(rb_male.isChecked()){
                gender = Constants.MALE;
            }
            else{
                gender = Constants.FEMALE;
            }
            if(userProfileImageURL.equals("")){
                userHashMap.put(Constants.MOBILE, mobileNumber);
                userHashMap.put(Constants.GENDER, gender);
                userHashMap.put(Constants.FIRST_NAME, fname);
                userHashMap.put(Constants.LAST_NAME, lname);
                userHashMap.put(Constants.COMPLETE_PROFILE, 1);
                new FirestoreClass().updateUserProfileData(UserProfileActivity.this, userHashMap);
                dialog.dismiss();
                userProfileUpdateSuccess();
            }
            else{
                userHashMap.put(Constants.MOBILE, mobileNumber);
                userHashMap.put(Constants.GENDER, gender);
                userHashMap.put(Constants.FIRST_NAME, fname);
                userHashMap.put(Constants.LAST_NAME, lname);
                userHashMap.put(Constants.IMAGE, userProfileImageURL);
                userHashMap.put(Constants.COMPLETE_PROFILE, 1);
                new FirestoreClass().updateUserProfileData(UserProfileActivity.this, userHashMap);
                dialog.dismiss();
                userProfileUpdateSuccess();
            }
        }

    }

    protected void userProfileUpdateSuccess(){
        Toast.makeText(UserProfileActivity.this, "Data Updated SuccessFully",Toast.LENGTH_LONG).show();
        Intent doneActivity = new Intent(UserProfileActivity.this, DashboardActivity.class);
        UserProfileActivity.this.startActivity(doneActivity);
        finish();
    }

    public void imageUploadSuccess(Uri imageFileURI){
        userProfileImageURL = imageFileURI.toString();

        progressDialog.dismiss();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE ){
                if(data != null){

                    mSelectedImageFileUri = data.getData();
                    new GlideLoader(UserProfileActivity.this).loadUserPicture(mSelectedImageFileUri, userPhoto);
                    new FirestoreClass().uploadImageToCloudStorage(UserProfileActivity.this, mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE);

                }
            }
    }


}
