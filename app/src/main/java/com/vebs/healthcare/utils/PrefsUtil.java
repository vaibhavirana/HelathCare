package com.vebs.healthcare.utils;

import android.content.Context;

/**
 * Created by raghav on 18/5/16.
 */
public class PrefsUtil {

    private static String Login = "Login";
    private static String city = "City";
    private static String city_id = "CityId";


    public static void setLogin(Context context, boolean isLogin) {
        Prefs.with(context).save(Login, isLogin);
    }

    public static boolean getLogin(Context context) {
        return Prefs.with(context).getBoolean(Login, false);
    }

    public static void setCity(Context context, String City) {
        Prefs.with(context).save(city, City);
    }

    public static String getCity(Context context) {
        return Prefs.with(context).getString(city, "");
    }

    public static void setCityID(Context context, int cityid) {
        Prefs.with(context).save(city_id, cityid);
    }

    public static int getCityID(Context context) {
        return Prefs.with(context).getInt(city_id, 0);
    }

}
