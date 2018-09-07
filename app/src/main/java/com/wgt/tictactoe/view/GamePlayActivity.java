package com.wgt.tictactoe.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wgt.tictactoe.R;
import com.wgt.tictactoe.databinding.ActivityGamePlayBinding;
import com.wgt.tictactoe.util.Constant;
import com.wgt.tictactoe.viewmodel.GamePlayViewModel;

public class GamePlayActivity extends AppCompatActivity {

    private GamePlayViewModel viewModel;
    private DatabaseReference gameWinnerRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constant.INTENT.GAME_ID)) {
            String gameID = bundle.getString(Constant.INTENT.GAME_ID);
            boolean isFirstPlayer = bundle.getBoolean(Constant.INTENT.IS_FIRST_PLAYER);

            viewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);
            viewModel.init(gameID, isFirstPlayer);

            ActivityGamePlayBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game_play);
            binding.setViewModel(viewModel);

            setupWinnerObserver(gameID);
        } else {
            Toast.makeText(this, "No gameID found. Please go back.", Toast.LENGTH_LONG).show();
        }
    }

    private void setupWinnerObserver(String gameID) {

        gameWinnerRef = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME).child(Constant.DATABASE.GAMES).child(gameID).child("winner");
        gameWinnerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String winner = dataSnapshot.getValue(String.class);

                if (winner != null && !winner.equals("")) {

                    String winnerMsg = winner.equals("com.debd.kgp.tictactoe") ? "No one wins!!" : "The winner is " + winner.toUpperCase();

                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(GamePlayActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Game Winner!!")
                            .setMessage(winnerMsg)
                            .setPositiveButton("OK", (dialog, which) -> {
                                startActivity(new Intent(GamePlayActivity.this, OnlinePlayersActivity.class));
                                finish();
                            })
                            .setIcon(android.R.drawable.ic_media_play)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewModel.isGameReady.observe(this, isReady -> {
            if (isReady) {
                viewModel.getWinner().observe(this, player -> {
                    //Toast.makeText(this, (player == null ? "No one" : player.name) + " wins", Toast.LENGTH_SHORT).show();
                    if (player == null) {
                        gameWinnerRef.setValue("com.debd.kgp.tictactoe");
                    } else {
                        gameWinnerRef.setValue(player.name);
                    }
                });
            }
        });


    }
}
