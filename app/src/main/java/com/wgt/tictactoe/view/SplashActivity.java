package com.wgt.tictactoe.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wgt.tictactoe.R;
import com.wgt.tictactoe.model.User;
import com.wgt.tictactoe.preference.UserCredPref;

public class SplashActivity extends AppCompatActivity {

    private boolean isBackPressed = false;
    private int SPLASH_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        decideNextPage();
    }

    private void decideNextPage() {
        new Handler().postDelayed(() -> {
            if (!isBackPressed) {
                User user = new UserCredPref(this).getUserDetails();
                Intent intent = null;
                if (user.isValid()) {
                    intent = new Intent(SplashActivity.this, OnlinePlayersActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }

    @Override
    public void onBackPressed() {
        isBackPressed = true;
        super.onBackPressed();
    }
}
