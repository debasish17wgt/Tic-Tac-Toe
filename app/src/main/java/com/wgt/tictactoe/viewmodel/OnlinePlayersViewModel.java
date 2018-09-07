package com.wgt.tictactoe.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wgt.tictactoe.model.FGame;
import com.wgt.tictactoe.model.GameRequest;
import com.wgt.tictactoe.model.User;
import com.wgt.tictactoe.preference.UserCredPref;
import com.wgt.tictactoe.util.Constant;
import com.wgt.tictactoe.util.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlinePlayersViewModel extends AndroidViewModel {

    public ObservableBoolean isLoading = new ObservableBoolean();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    private DatabaseReference onlineUsersTree;
    private DatabaseReference requestGetRef;
    //private DatabaseReference requestSendRef;
    private DatabaseReference gameRef;

    private ValueEventListener valueEventListener;
    private ValueEventListener requestGetEventListener;
    //private ValueEventListener requestSendEventListener;
    private ValueEventListener gameEventListener;

    private MutableLiveData<GameRequest> gameRequest = new MutableLiveData<>();
    private MutableLiveData<String> acceptedGame = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLogedOut = new MutableLiveData<>();

    private Context context;

    public OnlinePlayersViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        //initDBRefs();
    }

    /*private void initDBRefs() {

    }*/

    public void prepareRequestGetListener() {
        User user = new UserCredPref(context).getUserDetails();
        String email = user.getEmail();
        if (email != null) {
            //create game request reference
            requestGetRef = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME).child(Constant.DATABASE.REQUESTS_GET + "/" + email);

            //add empty request to check
            //remove this line.. this is for test purpose
            //requestGetRef.setValue(new GameRequest(user.getName(), user.getEmail(), "", "", ""));

            //create request get event listener
            requestGetEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GameRequest request = dataSnapshot.getValue(GameRequest.class);
                    gameRequest.setValue(request);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //databaseError.toException().printStackTrace();
                }
            };

            //add game request listener
            requestGetRef.addValueEventListener(requestGetEventListener);
        }
    }

    public void acceptGameRequest(String gameID, String receiverEmail) {
        //accept particular game request
        DatabaseReference acceptRef = FirebaseDatabase.getInstance()
                .getReference(Constant.DATABASE.DATABASE_NAME)
                .child(Constant.DATABASE.GAMES).child(gameID).child("accepted");
        acceptRef.setValue(true);

        //delete senders's articular request from "request_get"
        // otherwise it will again send that request
        deleteGameRequest(receiverEmail);

    }


    public void deleteGameRequest(String receiverEmail) {
        DatabaseReference requestRef = FirebaseDatabase.getInstance()
                .getReference(Constant.DATABASE.DATABASE_NAME)
                .child(Constant.DATABASE.REQUESTS_GET).child(receiverEmail);
        requestRef.setValue(null);
    }


    public void sendGameRequest(User receiverPlayer) {
        //create game first
        //then set data to sender's "request_get" structure
        User localUser = new UserCredPref(context).getUserDetails();
        String email = localUser.getEmail();
        if (email != null) {
            gameRef = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME).child(Constant.DATABASE.GAMES);
            String gameID = gameRef.push().getKey();
            gameRef.child(gameID)
                    .setValue(new FGame(
                            localUser.getName(),
                            receiverPlayer.getName(),
                            "",
                            "",
                            false,
                            Helper.getEmptyCell(3, 3))
                    );

            DatabaseReference requestSendRef = FirebaseDatabase.getInstance()
                    .getReference(Constant.DATABASE.DATABASE_NAME)
                    .child(Constant.DATABASE.REQUESTS_GET + "/" + receiverPlayer.getEmail());

            requestSendRef.setValue(
                    new GameRequest(
                            localUser.getName(),
                            localUser.getEmail(),
                            receiverPlayer.getName(),
                            receiverPlayer.getEmail(),
                            gameID
                    )
            );
            listenForAcceptedGame(gameID);
        }

    }


    private void listenForAcceptedGame(String gameID) {
        gameRef = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME).child(Constant.DATABASE.GAMES).child(gameID).child("accepted");
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isAccepted = dataSnapshot.getValue(Boolean.class);
                if (isAccepted) {
                    acceptedGame.setValue(gameID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void prepareOnlineUsers() {
        onlineUsersTree = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME).child(Constant.DATABASE.ONLINE_USERS);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collectData((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        onlineUsersTree.addValueEventListener(valueEventListener);

    }

    private void collectData(Map<String, Object> value) {
        if (value == null) {
            return;
        }
        List<User> temp = new ArrayList<>();
        User user = new UserCredPref(context).getUserDetails();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            if (((String) singleUser.get("email")).equals(user.getEmail())) {
                continue;
            }
            temp.add(new User((String) singleUser.get("name"), (String) singleUser.get("email")));
        }
        users.setValue(temp);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        onlineUsersTree.removeEventListener(valueEventListener);

    }

    public LiveData<GameRequest> getGameRequest() {
        return gameRequest;
    }

    public LiveData<List<User>> getUsersLive() {
        return users;
    }

    public LiveData<String> getAcceptedGame() {
        return acceptedGame;
    }

    public LiveData<Boolean> getLogedOut() {
        return isLogedOut;
    }

    public void logout() {
        new UserCredPref(context).logout();
        isLogedOut.setValue(true);
    }
}
