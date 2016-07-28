package com.vebs.healthcare.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vraj on 7/12/2016.
 */

public class Function {


    public static final String ROOT_URL = "https://arkaihealthcare.com/Reference/app/";
    public static final String LOGIN_URL = ROOT_URL + "login.php";
    public static final String CATEGORY_URL = ROOT_URL + "fetch_category.php";
    public static final String CITY_URL = ROOT_URL + "fetch_city.php";
    public static final String DOCTOR_URL = ROOT_URL + "fetch_pdoc.php";
    public static final String LAB_URL = ROOT_URL + "fetch_lab.php";
    public static final String LAB_TEST_URL = ROOT_URL + "fetch_plab.php";
    public static final String DIAG_CATEGORY_URL = ROOT_URL + "fetch_diag.php";
    public static final String DIAG_URL = ROOT_URL + "fetch_diagnostic.php";
    public static final String DIAG_TEST_URL = ROOT_URL + "fetch_diagnostic_test.php";
    public static final String REFER_DOCTOR_URL = ROOT_URL + "refer_doctor.php";
    public static final String REFER_LAB_URL = ROOT_URL + "refer_lab.php";
    public static final String REFER_DIAG_URL = ROOT_URL + "refer_daignostic.php";

    public static final int NO_DOCTOR = 1;
    public static final int NO_LAB = 2;
    public static final int NO_DIAG = 3;
    public static final int NO_PATIENT = 4;

    public static final String MALE = "MALE";
    public static final String FEMALE = "FEMALE";

    // City
    public static ArrayList<String> city_list;
    public static ArrayList<Integer> city_list_id;
    // category
    public static ArrayList<String> cat_list;
    public static ArrayList<Integer> cat_list_id;

    // lab
    public static ArrayList<String> lab_list;
    public static ArrayList<Integer> lab_list_id;

    //diag_cat
    public static ArrayList<String> diag_cat_list;
    public static ArrayList<Integer> diag_cat_list_id;
    // diag
    public static ArrayList<String> diag_list=new ArrayList<>();
    public static ArrayList<Integer> diag_list_id=new ArrayList<>();

    // diag test
    public static ArrayList<String> diag_test_list;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void showInternetPopup(final Context context)
    {
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.no_internet_msg))
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        System.exit(0);
                        //((Activity)context).finish();
                    }
                })
                .show();
    }
    public static void fetch_city(Context mContext) {
            // Function.fetch_city();
            final RestClient client = new RestClient(Function.CITY_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    city_list = new ArrayList<>();
                    city_list_id = new ArrayList<>();
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.Execute("get");
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_city = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("cityname")) {
                                jo_city = ja.getJSONObject(i).getJSONObject("cityname");
                                if (jo_city != null) {
                                    city_list.add(jo_city.getString("cityname"));
                                    city_list_id.add(jo_city.getInt("cityID"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    // progressDialog.dismiss();
                }
            }.execute();
    }

    public static void fetch_category(final Context mContext) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            // Function.fetch_city();
            final RestClient client = new RestClient(Function.CATEGORY_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    cat_list = new ArrayList<>();
                    cat_list_id = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Category", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.Execute("get");
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_city = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("categoryname")) {
                                jo_city = ja.getJSONObject(i).getJSONObject("categoryname");
                                if (jo_city != null) {
                                    cat_list.add(jo_city.getString("catName"));
                                    cat_list_id.add(jo_city.getInt("catId"));
                                    Log.e("resp", cat_list.get(i) + " || " + cat_list_id.get(i));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog[0].dismiss();
                }
            }.execute();

        }else
        {
            showInternetPopup(mContext);
        }
    }

    public static void fetch_lab(final Context mContext) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            // Function.fetch_city();
            final RestClient client = new RestClient(Function.LAB_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    lab_list = new ArrayList<>();
                    lab_list_id = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Labs", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                       // client.AddParam("catId", String.valueOf(0));
                        //client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_lab = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("lab")) {
                                jo_lab = ja.getJSONObject(i).getJSONObject("lab");
                                if (jo_lab != null) {
                                    lab_list.add(jo_lab.getString("labName"));
                                    lab_list_id.add(jo_lab.getInt("labId"));
                                    Log.e("resp", lab_list.get(i) + " || " + lab_list_id.get(i));
                                    //Log.e("resp", jo_lab.toString());
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog[0].dismiss();
                }
            }.execute();

        }else
        {
            showInternetPopup(mContext);
        }
    }

    public static void fetch_diag_category(final Context mContext) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            // Function.fetch_city();
            final RestClient client = new RestClient(Function.DIAG_CATEGORY_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    diag_cat_list = new ArrayList<>();
                    diag_cat_list_id = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Diagnostic Category", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.Execute("get");
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_cat = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("diagnostics")) {
                                jo_cat = ja.getJSONObject(i).getJSONObject("diagnostics");
                                if (jo_cat != null) {
                                    diag_cat_list.add(jo_cat.getString("catName"));
                                    diag_cat_list_id.add(jo_cat.getInt("catId"));
                                    //Log.e("resp", diag_cat_list.get(i) + " || " + diag_cat_list_id.get(i));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog[0].dismiss();
                }
            }.execute();

        }else
        {
            showInternetPopup(mContext);
        }
    }

    public static void fetch_diag(final Context mContext) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            // Function.fetch_city();
            final RestClient client = new RestClient(Function.DIAG_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    diag_list = new ArrayList<>();
                    diag_list_id = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Diagnostics", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.Execute("get");
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_city = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("diagnostics")) {
                                jo_city = ja.getJSONObject(i).getJSONObject("diagnostics");
                                if (jo_city != null) {
                                    diag_list.add(jo_city.getString("diagName"));
                                    diag_list_id.add(jo_city.getInt("diagId"));
                                    Log.e("resp", diag_list.get(i) + " || " + diag_list_id.get(i));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog[0].dismiss();
                }
            }.execute();

        }else
        {
            showInternetPopup(mContext);
        }
    }

    public static void fetch_diag_test(final Context mContext, final int diagid) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            final RestClient client = new RestClient(Function.DIAG_TEST_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    diag_test_list = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Diagnostic Test", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.AddParam("id", String.valueOf(diagid));
                        client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        //Log.e("res")
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_test = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("test")) {
                                jo_test = ja.getJSONObject(i).getJSONObject("test");
                                if (jo_test != null) {
                                    diag_test_list.add(jo_test.getString("test"));

                                    Log.e("lab test", diag_test_list.get(i));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog[0].dismiss();
                }
            }.execute();

        }else
        {
            showInternetPopup(mContext);
        }
    }
}
