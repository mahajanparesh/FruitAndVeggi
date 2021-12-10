package com.example.retriving_data.firestore

import com.example.retriving_data.models.User
import com.example.retriving_data.utils.Constants
import com.google.protobuf.Any
import java.util.*


class test(){
    private lateinit var userDetails: User;
    var userHashMap = HashMap<String, Any>()
    fun test(){
        userHashMap[Constants.MOBILE] = "mobileNumber"
    }
}

private operator fun <K, V> HashMap<K, V>.set(mobile: K, value: K) {

}
