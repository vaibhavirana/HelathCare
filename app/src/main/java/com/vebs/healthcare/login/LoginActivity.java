package com.vebs.healthcare.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.PrefsUtil;

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
        txtForget = (TextView) findViewById(R.id.txtForget);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);
        actionListener();
    }


    private void actionListener() {
        btnLogin.setOnClickListener(this);
        txtForget.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:
                if(edtEmail.getText().toString().trim().equals("abc") && edtPassword.getText().toString().trim().equals("abc") )
                {
                    PrefsUtil.setLogin(this, true);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
               // presenter.checkLogin(Functions.getValue(edtEmail), Functions.getValue(edtPassword));
                break;

            case R.id.txtForget:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Functions.hideKeyPad(LoginActivity.this, findViewById(android.R.id.content));
    }

}
