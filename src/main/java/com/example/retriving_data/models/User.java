package com.example.retriving_data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringDef;


import com.google.android.gms.common.internal.safeparcel.SafeParcelable;


public class User implements Parcelable {
    public String id = "";
    public String firstName = "";
    public String lastName = "";
    public String email = "";
    public String image = "";
    public long mobile = 0;
    public String gender = "";
    public int profileCompleted = 0;

    public User(String uid, String fName, String lName, String mail, String mobileNo) {
        id = uid;
        firstName = fName;
        lastName = lName;
        email = mail;
        mobile = Long.parseLong(mobileNo);
    }
    public User(){

    }

    protected User(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        image = in.readString();
        mobile = in.readLong();
        gender = in.readString();
        profileCompleted = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeLong(mobile);
        dest.writeString(gender);
        dest.writeInt(profileCompleted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
