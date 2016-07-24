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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vraj on 7/12/2016.
 */

public class Function {

    public static final String ROOT_URL = "https://arkaihealthcare.com/Reference/app/";
    public static final String LOGIN_URL = ROOT_URL + "login.php";
    public static final String CATEGORy_URL = ROOT_URL + "fetch_category.php";
    public static final String CITY_URL = ROOT_URL + "fetch_city.php";
    public static final String DOCTOR_URL = ROOT_URL + "fetch_pdoc.php";
    public static final String LAB_URL = ROOT_URL + "fetch_lab.php";
    public static final String LAB_TEST_URL = ROOT_URL + "fetch_lab_test.php";
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
    // doctor
   // public static ArrayList<HashMap<String,Object>> doc_list;
   /* public static ArrayList<Integer> doc_list_id;
    public static ArrayList<String> doc_name_list;
    public static ArrayList<String> doc_hosp_list;
    public static ArrayList<String> doc_email_list;
    public static ArrayList<String> doc_mobile_list;
    public static ArrayList<String> doc_landline_list;
    public static ArrayList<String> doc_addr_list;
    public static ArrayList<String> doc_time_list;
    public static ArrayList<String> doc_fees_list;
    public static ArrayList<String> doc_offer_list;*/

    // lab
    public static ArrayList<String> lab_list;
    public static ArrayList<Integer> lab_list_id;
    // lab test
    public static ArrayList<String> lab_test_list;
    public static ArrayList<Integer> lab_test_price;
    public static ArrayList<Integer> lab_test_type  ;
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
            final RestClient client = new RestClient(Function.CATEGORy_URL);
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

    /*public static void fetch_doctor(final Context mContext, final int catId) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            final RestClient client = new RestClient(Function.DOCTOR_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    doc_list= new ArrayList<>();
                   *//* doc_list_id = new ArrayList<>();
                    doc_name_list = new ArrayList<>();
                    doc_email_list = new ArrayList<>();
                    doc_addr_list = new ArrayList<>();
                    doc_hosp_list = new ArrayList<>();
                    doc_mobile_list = new ArrayList<>();
                    doc_landline_list = new ArrayList<>();
                    doc_time_list = new ArrayList<>();
                    doc_fees_list = new ArrayList<>();
                    doc_offer_list = new ArrayList<>();*//*

                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Doctor", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.AddParam("catId", String.valueOf(catId));
                        client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        //Log.e("res")
                        if (client.getResponse() != null) {
                            JSONArray ja = new JSONArray(client.getResponse());
                            JSONObject jo_doctor = null;
                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject object = ja.getJSONObject(i);
                                if (object.has("doctors")) {
                                    jo_doctor = ja.getJSONObject(i).getJSONObject("doctors");
                                    if (jo_doctor != null) {
                                        HashMap<String, Object> doc = new HashMap<String, Object>();
                                        doc.put("id", jo_doctor.getInt("id"));
                                        doc.put("name", jo_doctor.getString("Doc Name"));
                                        doc.put("email", jo_doctor.getString("email"));
                                        doc.put("address", jo_doctor.getString("address"));
                                        doc.put("hosp_name", jo_doctor.getString("Hospotal name"));
                                        doc.put("mobile", jo_doctor.getString("mobile"));
                                        doc.put("landline", jo_doctor.getString("LDLine Number"));
                                        doc.put("time", jo_doctor.getString("time"));
                                        doc.put("fees", jo_doctor.getString("fees"));
                                        doc.put("offer", jo_doctor.getString("offere"));
                                        doc_list.add(doc);
                                   *//* doc_list_id.add(jo_doctor.getInt("id"));
                                    doc_name_list.add(jo_doctor.getString("Doc Name"));
                                    doc_email_list.add(jo_doctor.getString("email"));
                                    doc_addr_list.add(jo_doctor.getString("address"));
                                    doc_hosp_list.add(jo_doctor.getString("Hospotal name"));
                                    doc_mobile_list.add(jo_doctor.getString("mobile"));
                                    doc_landline_list.add(jo_doctor.getString("LDLine Number"));
                                    doc_time_list.add(jo_doctor.getString("time"));
                                    doc_fees_list.add(jo_doctor.getString("fees"));
                                    doc_offer_list.add(jo_doctor.getString("offere"));*//*


                                    }

                                }
                            }
                            }
                            Log.e("doctor", doc_list.toString());
                        }catch(Exception e){
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

        }
        else
        {
            showInternetPopup(mContext);
        }
    }*/

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
                        client.Execute("get");
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_city = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("lab")) {
                                jo_city = ja.getJSONObject(i).getJSONObject("lab");
                                if (jo_city != null) {
                                    lab_list.add(jo_city.getString("labName"));
                                    lab_list_id.add(jo_city.getInt("labId"));
                                    Log.e("resp", lab_list.get(i) + " || " + lab_list_id.get(i));
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

    public static void fetch_lab_test(final Context mContext, final int labid) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            final RestClient client = new RestClient(Function.LAB_TEST_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    lab_test_list = new ArrayList<>();
                    lab_test_price = new ArrayList<>();
                    lab_test_type = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Lab Test", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.AddParam("id", String.valueOf(labid));
                        client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        //Log.e("res")
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_test = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("lab")) {
                                jo_test = ja.getJSONObject(i).getJSONObject("lab");
                                if (jo_test != null) {
                                    lab_test_list.add(jo_test.getString("test"));
                                    lab_test_price.add(jo_test.getInt("price"));
                                    lab_test_type.add(jo_test.getInt("ctype"));
                                    Log.e("lab test", lab_test_list.get(i));
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
