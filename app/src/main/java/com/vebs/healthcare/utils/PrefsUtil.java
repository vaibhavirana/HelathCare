package com.vebs.healthcare.utils;

import android.content.Context;

/**
 * Created by raghav on 18/5/16.
 */
public class PrefsUtil {

    private static String Login = "Login";
    private static String City = "City";
    private static String City_id = "CityId";


    public static void setLogin(Context context, boolean isLogin) {
        Prefs.with(context).save(Login, isLogin);
    }

    public static boolean getLogin(Context context) {
        return Prefs.with(context).getBoolean(Login, false);
    }

    public static void setCity(Context context, String City) {
        Prefs.with(context).save(City, City);
    }

    public static String getCity(Context context) {
        return Prefs.with(context).getString(City, "");
    }

    public static void setCityID(Context context, int city_id) {
        Prefs.with(context).save(City_id, city_id);
    }

    public static int getCityID(Context context) {
        return Prefs.with(context).getInt(City_id, 0);
    }

}
