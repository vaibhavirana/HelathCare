package com.vebs.healthcare.utils;

import android.content.Context;

/**
 * Created by raghav on 18/5/16.
 */
public class PrefsUtil {

    private static String Login = "Login";

    public static void setLogin(Context context, boolean isLogin) {
        Prefs.with(context).save(Login, isLogin);
    }

    public static boolean getLogin(Context context) {
        return Prefs.with(context).getBoolean(Login, false);
    }

}
