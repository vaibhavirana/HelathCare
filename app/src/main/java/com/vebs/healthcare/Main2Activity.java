package com.vebs.healthcare;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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


public class Main2Activity extends AppCompatActivity {

    public String ROOT_URL = "https://arkaihealthcare.com/Reference/app/get_cate_ws.php";
    InputStream is = null;
    String line = null;
    String result = null;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        callApi();
    }

    private void callApi() {
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...", false, false);

       /* //Creating a rest adapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();

        Callback cb = new Callback() {

            @Override
            public void success(Object o, Response response) {
                Log.e("Resp",response.getBody().toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("fail",error.getBody().toString());
            }
        };*/

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(ROOT_URL);
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();

                    while((line = reader.readLine()) != null) {

                        sb.append(line + "\n");
                    }

                    is.close();
                    String result = sb.toString();
                   // Log.e("result", result.toString());
                    JSONArray ja = new JSONArray(result);
                    JSONObject jo_cat = null;
                    JSONObject jo_doctor = null;
                    JSONObject jo_diagnostic = null;
                    JSONObject jo_lab = null;

                 //   list = new ArrayList<String>();

                    for(int i=0; i<ja.length(); i++) {

                        jo_cat = ja.getJSONObject(i).getJSONObject("category");
                      //  jo_doctor = ja.getJSONObject(i).getJSONObject("doctors");
                     //   jo_diagnostic = ja.getJSONObject(i).getJSONObject("diagnostic");
                       // jo_lab = ja.getJSONObject(i).getJSONObject("lab");

                        Log.e("result", jo_cat.toString());
                        /*Log.e("result", jo_diagnostic.toString());
                        Log.e("result", jo_doctor.toString());
                        Log.e("result", jo_lab.toString());*/
                        //list.add(jo.getString("uname"));
                    }
                }
                catch (Exception e) {
                    Log.e("Webservice 1", e.toString());
                }

            }
        });

        thread.start();

    }
}
