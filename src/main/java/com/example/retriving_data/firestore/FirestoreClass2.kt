package com.example.retriving_data.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.retriving_data.ui.activities.login
import com.example.retriving_data.utils.Constants
import com.example.retriving_data.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.retriving_data.models.User as User

@Suppress("UNREACHABLE_CODE")
class FirestoreClass2 {
    var dbroot = FirebaseFirestore.getInstance()
    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID

    }
    fun getUserDetails(activity: Activity){
        dbroot.collection(Constants.USERS).document(getCurrentUserID()).get().addOnSuccessListener { document ->


        }
        when (activity){
            is login -> {
                val user = User()
                val sharedPreferences = activity.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstName},${user.lastName}")
            }
        }
        }
    }
