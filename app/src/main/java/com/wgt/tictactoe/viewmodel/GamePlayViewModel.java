package com.wgt.tictactoe.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableBoolean;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.wgt.tictactoe.R;
import com.wgt.tictactoe.model.Cell;
import com.wgt.tictactoe.model.Game;
import com.wgt.tictactoe.model.Player;

import static com.wgt.tictactoe.util.StringUtility.stringFromNumbers;

public class GamePlayViewModel extends AndroidViewModel {
    public final ObservableBoolean isWaitingForOpponent = new ObservableBoolean();
    public ObservableArrayMap<String, Drawable> cells = new ObservableArrayMap<>();
    private Game game;
    private Context context;

    public GamePlayViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    /*initialize players*/
    public void init(String player1, String player2) {
        if (game == null) { /*checking for rotation change, if rotates init will call again, but should not be initialized game object*/
            game = new Game(player1, player2);
        }
    }

    /*1st player's name binding*/
    public String getPlayer1Name() {
        return game.player1.name;
    }

    /*2nd player's name binding*/
    public String getPlayer2Name() {
        return game.player2.name;
    }

    public void onClickedCellAt(int row, int column) {
        if (game.cells[row][column] == null) {
            game.cells[row][column] = new Cell(game.currentPlayer);

            //TODO :  remove context.getResources() and replace with BindAdapter
            cells.put(stringFromNumbers(row, column), game.currentPlayer.value.equalsIgnoreCase("x")
                    ? context.getResources().getDrawable(R.drawable.ic_x_l)
                    : context.getResources().getDrawable(R.drawable.ic_o_l));
            if (game.hasGameEnded()) {
                game.reset(); //clear game's data individually
                //resetBoard();
            } else
                game.switchPlayer();
        }
    }

    public LiveData<Player> getWinner() {
        return game.winner;
    }

    /*private void resetBoard() {
        cells.clear(); //clear cells(view)
        game = null;
        game = new Game("Ajit", "Deba");
    }*/


}
