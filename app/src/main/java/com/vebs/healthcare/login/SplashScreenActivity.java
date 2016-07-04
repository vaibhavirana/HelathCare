package com.vebs.healthcare.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.UIUtil;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtil.setActivityToFullScreen(this);
        setContentView(R.layout.activity_splash_screen);

        // txtAppName.setTypeface(Functions.getBoldFont(this));

        new CountDownTimer(2500, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                if (!PrefsUtil.getLogin(SplashScreenActivity.this)) {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                 //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    //Functions.fireIntent(SplashScreenActivity.this, MainPageActivity.class);
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Functions.hideKeyPad(SplashScreenActivity.this, findViewById(android.R.id.content));
    }
}
