package com.vebs.healthcare.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private View mRootView;
    private Button btnLogin;
    private MaterialEditText edtEmail, edtPassword;
    private ProgressDialog progressDialog;
    private TextView txtForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        init();

    }


    private void init() {
        mRootView = findViewById(android.R.id.content);
        edtEmail = (MaterialEditText) findViewById(R.id.edtEmail);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        //txtForget = (TextView) findViewById(R.id.txtForget);
        Function.setRegularFont(this, edtEmail);
        Function.setRegularFont(this, edtPassword);
        Function.setRegularFont(this, btnLogin);
        
        progressDialog = new ProgressDialog(this);
        actionListener();
    }


    private void actionListener() {
        btnLogin.setOnClickListener(this);
        // txtForget.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:
                attempLogin();

                // presenter.checkLogin(Functions.getValue(edtEmail), Functions.getValue(edtPassword));
                break;

        }
    }

    private void attempLogin() {
        if (edtEmail.getText().length() == 0) {
            edtEmail.setError(getResources().getString(R.string.enter_valid_email));
        } else if (edtPassword.getText().length() == 0) {
            edtPassword.setError(getResources().getString(R.string.enter_valid_password));
        } else {
            if (Function.isConnected(this)) {
                final boolean[] flag = {false};
                final ProgressDialog[] progressDialog = new ProgressDialog[1];
                final RestClient client = new RestClient(Function.LOGIN_URL);
                client.AddParam("username", edtEmail.getText().toString().trim());
                client.AddParam("password", edtPassword.getText().toString().trim());
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog[0] = ProgressDialog.show(LoginActivity.this, "Please wait...", " ", false, false);
                        // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            //client.AddParam("id", String.valueOf(diagid));
                            //client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                            client.Execute("get");

                            JSONArray ja = new JSONArray(client.getResponse());
                            //for (int i = 0; i < ja.length(); i++) {
                            JSONObject object = ja.getJSONObject(0);
                            if (object.has("flag")) {
                                if (object.getString("flag").equals("true")) {
                                    flag[0] = true;
                                    if (ja.getJSONObject(1).has("data")) {
                                        JSONObject object1 = ja.getJSONObject(1).getJSONObject("data");
                                        PrefsUtil.setDrID(LoginActivity.this, object1.getString("id"));
                                        PrefsUtil.setDrName(LoginActivity.this, object1.getString("drName"));
                                        PrefsUtil.setMobiles(LoginActivity.this, object1.getString("mobiles"));
                                        PrefsUtil.setcAddress(LoginActivity.this, object1.getString("cAddress"));
                                        PrefsUtil.setLogin(LoginActivity.this, true);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Some Error Occured in Connection. Please try again later", Toast.LENGTH_LONG).show();
                                    }
                                    //Log.e("data res", ja.getJSONObject(i+1).toString());
                                } else {
                                    flag[0] = false;
                                    //Log.e(" no res", ja.getJSONObject(i+1).toString());
                                }
                            }
                            //}

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
                        if (!flag[0]) {
                            new MaterialDialog.Builder(LoginActivity.this)
                                    .title(LoginActivity.this.getString(R.string.login_fail))
                                    //.typeface(Functions.getBoldFont(context), Functions.getRegularFont(context))
                                    .positiveText(android.R.string.ok)
                                    .show();

                        }
                    }
                }.execute();

            } else {
                Function.showInternetPopup(LoginActivity.this);
            }

           /* PrefsUtil.setLogin(this, true);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Functions.hideKeyPad(LoginActivity.this, findViewById(android.R.id.content));
    }

}
