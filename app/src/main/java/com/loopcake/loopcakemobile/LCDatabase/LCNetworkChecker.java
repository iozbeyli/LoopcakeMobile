package com.loopcake.loopcakemobile.LCDatabase;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Melih on 28.05.2017.
 */

public class LCNetworkChecker {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
