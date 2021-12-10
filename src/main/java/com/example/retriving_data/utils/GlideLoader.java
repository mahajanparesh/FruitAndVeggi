package com.example.retriving_data.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.retriving_data.R;

public class GlideLoader {
    Context context;
    public GlideLoader(Context context){
        super();
        this.context = context;
    }
    public void loadUserPicture(Object image, ImageView imageView){
        try{
            Glide.with(context).load(image)
                    .centerCrop().placeholder(R.drawable.ic_baseline_account_box_24)
                    .into(imageView);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadProductPicture(Object image, ImageView imageView){
        try{
            Glide.with(context).load(image)
                    .placeholder(R.drawable.ic_baseline_account_box_24)
                    .into(imageView);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadProductListPicture(Object image, ImageView imageView){
        try{
            Glide.with(context).load(image)
                    .into(imageView);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
