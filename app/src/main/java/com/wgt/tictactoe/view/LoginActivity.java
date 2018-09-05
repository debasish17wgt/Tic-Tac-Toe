package com.wgt.tictactoe.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wgt.tictactoe.R;
import com.wgt.tictactoe.databinding.ActivityLoginBinding;
import com.wgt.tictactoe.preference.UserCredPref;
import com.wgt.tictactoe.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel;
    private UserCredPref userCredPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);

        userCredPref = new UserCredPref(this);

        listenForLogin();
    }

    private void listenForLogin() {
        viewModel.getLoginStatus().observe(this, status -> {
            if (status) {
                Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();

                //save user data
                userCredPref.saveData(viewModel.getUser());

                //go to online player activity
                startActivity(new Intent(LoginActivity.this, OnlinePlayersActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
