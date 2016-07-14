package com.vebs.healthcare.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.vebs.healthcare.MainActivity;

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

/**
 * Created by vraj on 7/12/2016.
 */

public class Function {

    public static final String ROOT_URL = "https://arkaihealthcare.com/Reference/app/";
    public static final String CATEGORy_URL = ROOT_URL+"get_cate_ws.php";
    public static final String CITY_URL = ROOT_URL+"fetch_city.php";
    public static final String REFER_DOCTOR_URL = ROOT_URL+"refer_doctor.php";
    public static final String REFER_LAB_URL = ROOT_URL+"refer_lab.php";
    public static final String REFER_DIAG_URL = ROOT_URL+"refer_daignostic.php";
    public static ArrayList<String> city_list;
    public static ArrayList<Integer> city_list_id;
    private static InputStream is;
    private static String line;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void fetch_city()
    {
            new AsyncTask<Void, Void, Void>() {

                StringBuilder sb = new StringBuilder();

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
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(CITY_URL);
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        is.close();

                        String result = sb.toString();
                        Log.e("result", result.toString());
                        JSONArray ja = new JSONArray(result);
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
                            //setMainJSONArray(ja);

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
}
