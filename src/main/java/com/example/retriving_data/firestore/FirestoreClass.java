package com.example.retriving_data.firestore;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.retriving_data.models.Product;
import com.example.retriving_data.ui.activities.AddProductActivity;
import com.example.retriving_data.ui.activities.ProductDisplayActivity;
import com.example.retriving_data.ui.activities.SettingsActivity;
import com.example.retriving_data.ui.activities.UserProfileActivity;
import com.example.retriving_data.ui.activities.login;
import com.example.retriving_data.models.User;
import com.example.retriving_data.ui.activities.signup;
import com.example.retriving_data.ui.fragments.DashboardFragment;
import com.example.retriving_data.ui.fragments.ProductsFragment;
import com.example.retriving_data.utils.Constants;
import com.example.retriving_data.utils.GlideLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class FirestoreClass{
    FirebaseFirestore dbroot = FirebaseFirestore.getInstance();

    public void registerUser(signup activity , User UsersInfo){
        dbroot.collection(Constants.USERS).document(UsersInfo.id).set(UsersInfo,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Done");
                    }
                });
    }

    public String getCurrentUserID(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = "";
        if(currentUser != null){
            currentUserID = currentUser.getUid();
        }
        return currentUserID;
    }

    public void getUsersDetails(Activity activity){
        dbroot.collection(Constants.USERS).document(getCurrentUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.LOGGED_IN_USERNAME, user.firstName +" "+ user.lastName);
                editor.apply();

                if(activity instanceof login){
                    ((login) activity).userLoggedInSuccess(user);
                }
                if(activity instanceof SettingsActivity){
                    ((SettingsActivity)activity).userDetailSuccess(user);
                }
            }
        });
    }

    public void updateUserProfileData(Activity activity, HashMap<String, Object> userHashMap){
        dbroot.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap);
    }

    public void uploadImageToCloudStorage(Activity activity, Uri imageFileURI,String imageType){
        StorageReference sRef = FirebaseStorage.getInstance().getReference().child(imageType + System.currentTimeMillis()+"."+Constants.getFileExtension(activity, imageFileURI));
        sRef.putFile(imageFileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if( activity instanceof UserProfileActivity){
                    ((UserProfileActivity) activity).imageUploadSuccess(imageFileURI);
                }
                if(activity instanceof AddProductActivity){
                    ((AddProductActivity) activity).imageUploadSuccess(imageFileURI);
                }
            }
        }).addOnFailureListener(e -> {


            if(activity instanceof AddProductActivity){
                Toast.makeText(activity, "Error! Unable to add product.", Toast.LENGTH_LONG).show();
                ((AddProductActivity) activity).submit.setEnabled(true);
                ((AddProductActivity) activity).progressDialog.dismiss();
            }
        });
    }

    public void uploadProductDetails(AddProductActivity activity, Product productInfo){
        dbroot.collection(Constants.PRODUCTS).document().set(productInfo, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                activity.uploadDataSuccess();
            }
        }).addOnFailureListener(e -> {

            Toast.makeText(activity, "Error! Unable to add product.", Toast.LENGTH_LONG).show();
            activity.submit.setEnabled(true);
            activity.progressDialog.dismiss();
        });
    }

    public void getProductList(Fragment fragment){
        dbroot.collection(Constants.PRODUCTS).whereEqualTo(Constants.USER_ID, getCurrentUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Product> productsList = new ArrayList();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Product product = documentSnapshot.toObject(Product.class);
                    product.product_id = documentSnapshot.getId();
                    productsList.add(product);
                }
                if(fragment instanceof ProductsFragment){
                    ((ProductsFragment) fragment).successProductsListFromFireStore(productsList);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(fragment.getContext(), "Error! Unable to load Data. Check Your Internet Connection", Toast.LENGTH_LONG).show();
                ((ProductsFragment) fragment).progressDialog.dismiss();
            }
        });
    }
    public void getDashboardItemsList(DashboardFragment fragment){
        dbroot.collection(Constants.PRODUCTS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Product> productsList = new ArrayList();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Product product = documentSnapshot.toObject(Product.class);
                    product.product_id = documentSnapshot.getId();
                    productsList.add(product);
                }
                fragment.successDashboardListFromFireStore(productsList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(fragment.getContext(), "Error! Unable to load Data. Check Your Internet Connection", Toast.LENGTH_LONG).show();
                fragment.progressDialog.dismiss();
            }
        });
    }

    public void deleteProduct(ProductsFragment fragment, String productId){
        dbroot.collection(Constants.PRODUCTS).document(productId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fragment.productDeleteSuccess(productId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fragment.progressDialog.dismiss();
                Toast.makeText(fragment.getContext(), "Failed! Error Occurred While deleting the product", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getProductDetails(ProductDisplayActivity activity, String productId){
        dbroot.collection(Constants.PRODUCTS).document(productId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
            Product product = documentSnapshot.toObject(Product.class);
                activity.productDetailsSuccess(product);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                activity.progressDialog.dismiss();
                Toast.makeText(activity, "Failed! Error Occurred While deleting the product", Toast.LENGTH_LONG).show();
            }
        });
    }
}
