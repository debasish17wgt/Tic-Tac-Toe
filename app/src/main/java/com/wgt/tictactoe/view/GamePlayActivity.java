package com.wgt.tictactoe.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wgt.tictactoe.R;
import com.wgt.tictactoe.databinding.ActivityGamePlayBinding;
import com.wgt.tictactoe.model.Player;
import com.wgt.tictactoe.viewmodel.GamePlayViewModel;

public class GamePlayActivity extends AppCompatActivity {

    private GamePlayViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);
        viewModel.init("Deba", "Ajit");

        ActivityGamePlayBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game_play);
        binding.setViewModel(viewModel);

        setupWinnerObserver();

    }

    private void setupWinnerObserver() {
        viewModel.getWinner().observe(this, player -> {
            Toast.makeText(this, player.name + " wins", Toast.LENGTH_SHORT).show();
        });
    }
}
