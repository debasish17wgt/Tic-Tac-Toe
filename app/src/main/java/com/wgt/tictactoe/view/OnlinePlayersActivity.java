package com.wgt.tictactoe.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wgt.tictactoe.R;
import com.wgt.tictactoe.databinding.ActivityOnlinePlayersBinding;
import com.wgt.tictactoe.viewmodel.OnlinePlayersViewModel;

public class OnlinePlayersActivity extends AppCompatActivity {

    private OnlinePlayersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityOnlinePlayersBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_online_players);
        viewModel = ViewModelProviders.of(this).get(OnlinePlayersViewModel.class);
        binding.setViewModel(viewModel);

    }
}
