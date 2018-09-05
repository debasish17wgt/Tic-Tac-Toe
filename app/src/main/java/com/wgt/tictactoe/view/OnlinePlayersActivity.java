package com.wgt.tictactoe.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wgt.tictactoe.R;
import com.wgt.tictactoe.adapter.OnlineUsersAdapter;
import com.wgt.tictactoe.databinding.ActivityOnlinePlayersBinding;
import com.wgt.tictactoe.model.GameRequest;
import com.wgt.tictactoe.model.User;
import com.wgt.tictactoe.preference.UserCredPref;
import com.wgt.tictactoe.util.Constant;
import com.wgt.tictactoe.viewmodel.OnlinePlayersViewModel;

import java.util.ArrayList;
import java.util.List;

public class OnlinePlayersActivity extends AppCompatActivity implements OnlineUsersAdapter.OnlineUsersClickListener {

    private OnlinePlayersViewModel viewModel;
    private ActivityOnlinePlayersBinding binding;

    private RecyclerView recyclerView;
    private OnlineUsersAdapter adapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_online_players);
        viewModel = ViewModelProviders.of(this).get(OnlinePlayersViewModel.class);
        binding.setViewModel(viewModel);

        initOnlineUsersList();
        listenOnlineUsersLiveChanges();

        listenForGameRequest();
        listenForAcceptedGame();
    }

    private void listenForAcceptedGame() {
        viewModel.getAcceptedGame().observe(this, s -> {
            //TODO: handle this (open gamePlayActivity)
            Toast.makeText(this, "Game: " + s + " accepted", Toast.LENGTH_SHORT).show();
            openGameActivity(s, true);
        });
    }


    private void listenForGameRequest() {
        viewModel.prepareRequestGetListener();
        viewModel.getGameRequest().observe(this, gameRequest -> {
            //TODO : open Game request dialog
            Toast.makeText(this, "Request from " + gameRequest.getSenderEmail(), Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Game Request")
                    .setMessage("From " + gameRequest.getSenderName() + "\nPlay?")
                    .setPositiveButton("Play", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.acceptGameRequest(gameRequest.getGameID());
                            openGameActivity(gameRequest.getGameID(), false);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_media_play)
                    .show();

        });
    }

    private void openGameActivity(String gameID, boolean isFirstPlayer) {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra(Constant.INTENT.GAME_ID, gameID);
        intent.putExtra(Constant.INTENT.IS_FIRST_PLAYER, isFirstPlayer);

        startActivity(intent);
        finish();

    }

    private void initOnlineUsersList() {
        recyclerView = binding.recyclerOnlinePlayers;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new OnlineUsersAdapter(this, users);
        recyclerView.setAdapter(adapter);
    }

    private void listenOnlineUsersLiveChanges() {
        viewModel.prepareOnlineUsers();
        viewModel.getUsersLive().observe(this, users -> {
            this.users.clear();
            this.users.addAll(users);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onOnlineUserClick(User user) {
        Toast.makeText(this, "Sending request to " + user.getName(), Toast.LENGTH_SHORT).show();
        viewModel.sendGameRequest(user);
    }

    @Override
    protected void onStop() {
        super.onStop();

        User user = new UserCredPref(this).getUserDetails();

        //delete user from online users data tree
        DatabaseReference onlineUsersTree = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME).child(Constant.DATABASE.ONLINE_USERS);
        String key = user.getFdbKey();
        if (key != null) {
            onlineUsersTree.child(key).setValue(null);
        }
    }
}
