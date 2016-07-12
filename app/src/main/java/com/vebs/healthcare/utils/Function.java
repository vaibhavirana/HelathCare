package com.vebs.healthcare.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vraj on 7/12/2016.
 */

public class Function {

    public static final String ROOT_URL = "https://arkaihealthcare.com/Reference/app/get_cate_ws.php";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
