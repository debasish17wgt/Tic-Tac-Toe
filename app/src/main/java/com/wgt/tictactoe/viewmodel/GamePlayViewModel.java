package com.wgt.tictactoe.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wgt.tictactoe.R;
import com.wgt.tictactoe.model.Cell;
import com.wgt.tictactoe.model.FGame;
import com.wgt.tictactoe.model.Game;
import com.wgt.tictactoe.model.Player;
import com.wgt.tictactoe.util.Constant;

import java.util.Map;

import static com.wgt.tictactoe.util.StringUtility.stringFromNumbers;

public class GamePlayViewModel extends AndroidViewModel {
    public final ObservableBoolean isWaitingForGameInit = new ObservableBoolean();
    public final ObservableBoolean isMyTurn = new ObservableBoolean();
    public ObservableArrayMap<String, Drawable> cells = new ObservableArrayMap<>();

    public final ObservableField<String> player1Name = new ObservableField<>();
    public final ObservableField<String> player2Name = new ObservableField<>();

    public final MutableLiveData<Boolean> isGameReady = new MutableLiveData<>();

    private Game game;
    private Context context;

    private DatabaseReference gameRootRef;
    private DatabaseReference gameCellRef;
    private DatabaseReference gameCurrentPlayerRef;

    public GamePlayViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    /*initialize players*/
    public void init(String gameID, boolean isFirstPlayer) {
        isWaitingForGameInit.set(true);
        game = new Game("", "");
        player1Name.set("");
        player2Name.set("");
        initGame(gameID, isFirstPlayer);
    }

    private void initGame(String gameID, boolean isFirstPlayer) {
        gameRootRef = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME).child(Constant.DATABASE.GAMES).child(gameID);
        gameCellRef = gameRootRef.child("cell");
        gameCurrentPlayerRef = gameRootRef.child("currentPlayer");


        //get FGame object to get all player details
        gameRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    FGame fGame = dataSnapshot.getValue(FGame.class);

                    //init game object with player names
                    game = new Game(fGame.getPlayer1(), fGame.getPlayer2());

                    //plaer name setter
                    player1Name.set(fGame.getPlayer1());
                    player2Name.set(fGame.getPlayer2());

                    //init whos turn
                    initWhosTurn(isFirstPlayer);

                    //init BoardUI
                    initGameBoard();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initWhosTurn(boolean isFirstPlayer) {
        //if i'm FirstPlayer set that to firebase for other to get that
        if (isFirstPlayer) {
            gameCurrentPlayerRef.setValue(game.player1.name);
        }


        //listen for currentPlayer changes
        gameCurrentPlayerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //set isMyTurn
                if (dataSnapshot != null) {
                    String currentPlayerName = dataSnapshot.getValue(String.class);
                    if (currentPlayerName == null || currentPlayerName.equals("")) {
                        return;
                    }
                    if (isFirstPlayer && currentPlayerName.equals(game.player1.name)) {
                        isMyTurn.set(true);
                        switchToMyTurn(isFirstPlayer);
                    } else if (!isFirstPlayer && currentPlayerName.equals(game.player2.name)) {
                        isMyTurn.set(true);
                        switchToMyTurn(isFirstPlayer);
                    } else {
                        isMyTurn.set(false);
                        //switchToMyTurn(isFirstPlayer);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void switchToMyTurn(boolean isFirstPlayer) {
        if (isFirstPlayer) {
            game.currentPlayer = game.player1;
        } else {
            game.currentPlayer = game.player2;
        }
    }

    private void initGameBoard() { //it handles only UI part
        isWaitingForGameInit.set(false);
        isGameReady.setValue(true);

        gameCellRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> cellMap = (Map<String, String>) dataSnapshot.getValue();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        String move = cellMap.get("" + i + j);
                        if (move != null && !move.equals("") && game.cells[i][j] == null) {
                            game.cells[i][j] = new Cell(getPlayerFromName(move));

                            cells.put(stringFromNumbers(i, j), getPlayerFromName(move).value.equalsIgnoreCase("x")
                                    ? context.getResources().getDrawable(R.drawable.ic_x_l)
                                    : context.getResources().getDrawable(R.drawable.ic_o_l));
                        }
                    }
                }

                /*String aa = cellMap.get("00");
                String ab = cellMap.get("01");
                String ac = cellMap.get("02");

                String ba = cellMap.get("10");
                String bb = cellMap.get("11");
                String bc = cellMap.get("12");

                String ca = cellMap.get("20");
                String cb = cellMap.get("21");
                String cc = cellMap.get("22");*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private Player getPlayerFromName(String name) {
        if (game.player1.name.equals(name)) {
            return game.player1;
        } else {
            return game.player2;
        }
    }


    public void onClickedCellAt(int row, int column) {
        if (game.cells[row][column] == null) {
            game.cells[row][column] = new Cell(game.currentPlayer);

            cells.put(stringFromNumbers(row, column), game.currentPlayer.value.equalsIgnoreCase("x")
                    ? context.getResources().getDrawable(R.drawable.ic_x_l)
                    : context.getResources().getDrawable(R.drawable.ic_o_l));

            //send that value to firebase
            gameCellRef.child("" + row + column).setValue(game.currentPlayer.name);

            if (game.hasGameEnded()) {
                //game.reset(); //clear game's data individually
                //resetBoard();
            } else {
                game.switchPlayer();

                //switch current player to firebase
                gameCurrentPlayerRef.setValue(game.currentPlayer.name);
            }
        }
    }

    public LiveData<Player> getWinner() {
        return game.winner;
    }


    /*

     */
    /*1st player's name binding*//*

    public String getPlayer1Name() {
        return game.player1.name;
    }

    */
    /*2nd player's name binding*//*

    public String getPlayer2Name() {
        return game.player2.name;
    }
*/

    /*private void resetBoard() {
        cells.clear(); //clear cells(view)
        game = null;
        game = new Game("Ajit", "Deba");
    }*/


}
