package com.example.retriving_data.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.Product;
import com.example.retriving_data.utils.Constants;
import com.example.retriving_data.utils.Extras;
import com.example.retriving_data.utils.GlideLoader;

public class AddProductActivity extends AppCompatActivity {
    ImageView back,addProductPhoto, productPhoto;
    public ProgressDialog progressDialog;
    private Uri mSelectedImageFileUri;
    private String mProductImageUri = "";
    public Button submit;
    EditText productTitle, productPrice, productDescription, productQuantity;
    boolean isProductTitle, isProductPrice, isProductDescription, isProductQuantity, isImage, isImageUploded , isDataUploaded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().hide();
        back = findViewById(R.id.Add_Product_back);
        addProductPhoto = findViewById(R.id.Add_product_image);
        productPhoto = findViewById(R.id.Product_Image);
        submit = findViewById(R.id.Submit);
        productTitle = findViewById(R.id.Product_Title);
        productPrice = findViewById(R.id.Product_Price);
        productDescription = findViewById(R.id.Product_Description);
        productQuantity = findViewById(R.id.Product_Quantity);
    
        //backw
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Extras.networkCheck(AddProductActivity.this)) {
                    if (ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        /*Snackbar.make(mainLayout, "You already have the storage permission", Snackbar.LENGTH_LONG).show();*/




                        progressDialog = new Extras().showProgressBar(AddProductActivity.this);
                        Constants.showImageChooser(AddProductActivity.this);


                    } else {
                        ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_STORAGE_PERMISSION_CODE);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Submit BUtton code
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Extras.networkCheck(AddProductActivity.this)) {
                    submit.setEnabled(false);
                    setValidation();
                    progressDialog = new Extras().showProgressBar(AddProductActivity.this);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error! Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void setValidation(){
        if(mSelectedImageFileUri == null){
            Toast.makeText(getApplicationContext(), "Failed! you didn't select the image", Toast.LENGTH_LONG).show();
            isImage = false;
        }
        else{
            isImage = true;
        }

        if (productTitle.getText().toString().isEmpty()) {
            productTitle.setError(getResources().getString(R.string.empty_field_error));
            isProductTitle = false;
        }
       else{
            isProductTitle = true;
        }

       if(productPrice.getText().toString().isEmpty()){
           productPrice.setError(getResources().getString(R.string.empty_field_error));
           isProductPrice = false;
       }
       else{
           isProductPrice = true;
       }

        if(productDescription.getText().toString().isEmpty()){
            productDescription.setError(getResources().getString(R.string.empty_field_error));
            isProductDescription = false;
        }
        else{
            isProductDescription = true;
        }

        if(productQuantity.getText().toString().isEmpty()){
            productQuantity.setError(getResources().getString(R.string.empty_field_error));

            isProductQuantity = false;
        }
        else{
            isProductQuantity = true;
        }

        if(isImage  && isProductTitle && isProductPrice && isProductDescription && isProductQuantity){
            uploadImage(mSelectedImageFileUri);
            }


    }

    private void uploadData(){
        String username = this.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USERNAME, "");
        Product product = new Product(new FirestoreClass().getCurrentUserID(), username, productTitle.getText().toString().trim(), productPrice.getText().toString().trim(), productDescription.getText().toString().trim(), productQuantity.getText().toString().trim(), mProductImageUri);
        new FirestoreClass().uploadProductDetails(this, product);

    }
    public void uploadImage(Uri mSelectedImageFileUri){

        new FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.PRODUCT_IMAGE);

    }
    public void imageUploadSuccess(Uri imageFileURI){
        mProductImageUri = imageFileURI.toString();
        uploadData();
    }

    public void uploadDataSuccess(){
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Data uploaded Successfully!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
            if (data != null) {
                mSelectedImageFileUri = data.getData();
                addProductPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_icon));
                try {
                    new GlideLoader(this).loadProductPicture(mSelectedImageFileUri, productPhoto);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }
}