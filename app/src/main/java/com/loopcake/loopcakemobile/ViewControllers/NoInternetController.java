package com.loopcake.loopcakemobile.ViewControllers;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.loopcake.loopcakemobile.R;

/**
 * Created by Melih on 29.05.2017.
 */

public class NoInternetController {
    private View noInternetLayout;
    private Activity activity;
    private View rest;
    private static Drawable no_internet_drawable=null;
    public NoInternetController(View noInternetLayout,View rest, Activity activity){
        this.noInternetLayout = noInternetLayout;
        this.rest = rest;
        this.activity=activity;
    }

    public void showNoInternet(Boolean open){
        if(open){
            noInternetLayout.setVisibility(View.VISIBLE);
            rest.setVisibility(View.GONE);
            ImageView iv = (ImageView) noInternetLayout.findViewById(R.id.no_internet_image);
            if(no_internet_drawable==null){
                no_internet_drawable=ContextCompat.getDrawable(activity,R.drawable.no_internet);
                no_internet_drawable.setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            }
            iv.setImageDrawable(no_internet_drawable);
        }else{
            noInternetLayout.setVisibility(View.GONE);
            rest.setVisibility(View.VISIBLE);
        }


    }

}
