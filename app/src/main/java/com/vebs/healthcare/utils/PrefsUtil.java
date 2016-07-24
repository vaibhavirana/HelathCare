package com.vebs.healthcare.utils;

import android.content.Context;

/**
 * Created by raghav on 18/5/16.
 */
public class PrefsUtil {

    private static String Login = "Login";
    private static String DrID = "Dr_id";
    private static String cAddress = "clinicAddress";
    private static String drName = "Dr_Name";
    private static String mobiles = "Mobile";
    private static String city = "City";
    private static String city_id = "CityId";


    public static String getMobiles(Context context) {
        return mobiles;
    }

    public static void setMobiles(Context context,String Mobiles) {
        Prefs.with(context).save(mobiles, Mobiles);
    }

    public static String getDrName(Context context) {
        return Prefs.with(context).getString(drName, "");
    }

    public static void setDrName(Context context,String DrName) {
        Prefs.with(context).save(drName, DrName);
    }

    public static String getcAddress(Context context) {
        return Prefs.with(context).getString(cAddress, "");
    }

    public static void setcAddress(Context context,String cAddress) {
        Prefs.with(context).save(cAddress, cAddress);
    }

    public static String getDrID(Context context) {
        return Prefs.with(context).getString(DrID, "");
    }

    public static void setDrID(Context context,String drID) {
        Prefs.with(context).save(DrID, drID);
    }


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
