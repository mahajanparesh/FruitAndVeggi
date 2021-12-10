package com.example.retriving_data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    public String user_id = "";
    public String user_name = "";
    public String title = "";
    public String price = "";
    public String description = "";
    public String stock_quantity = "";
    public String image = "";
    public String product_id = "";

    public Product(String uid, String uName, String pTitle, String pPrice, String pDescription, String pQuantity, String pImage) {
        user_id = uid;
        user_name = uName;
        title = pTitle;
         price = pPrice;
        description = pDescription;
        stock_quantity = pQuantity;
        image = pImage;
    }

    public Product(){

    }
    protected Product(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
