package com.example.retriving_data.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.Fragment;

import com.example.retriving_data.R;

public class Extras {
    public ProgressDialog showProgressBar(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return progressDialog;
    }

    public ProgressDialog showProgressBar(Fragment fragment) {
        ProgressDialog progressDialog = new ProgressDialog(fragment.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return progressDialog;
    }
    public static boolean networkCheck(Activity activity) {
        boolean have_WIFI = false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
            return true;
        }
        else{
            return false;
        }
    }
}

