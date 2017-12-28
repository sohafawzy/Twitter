package com.xwady.twitterclient.views.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xwady.twitterclient.R;
import com.xwady.twitterclient.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);
        new Handler().postDelayed(() -> {
            if (sessionManager.isLoggedIn()) { //if user is logged in before
                startActivity(new Intent(SplashActivity.this, MainActivity
                        .class));
            } else { //user logged out
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, 1500);
    }
}
