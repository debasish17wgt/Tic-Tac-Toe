package com.wgt.tictactoe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wgt.tictactoe.model.User;
import com.wgt.tictactoe.util.Constant;

public class LoginViewModel extends ViewModel {
    private User user;
    private DatabaseReference databaseReference;
    public ObservableBoolean isLoading = new ObservableBoolean();
    private MutableLiveData<Boolean> isLoggedin = new MutableLiveData<>();

    public LoginViewModel() {
        user = new User();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE.DATABASE_NAME);
    }

    public void setName(String name) {
        this.user.setName(name);
    }

    public String getName() {
        return this.user.getName();
    }

    public void setEmail(String email) {
        this.user.setEmail(email);
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public User getUser() {
        return user;
    }

    public void onLoginClicked() {
        isLoading.set(true);
        String key = databaseReference.child(Constant.DATABASE.ONLINE_USERS).push().getKey();
        databaseReference.child(Constant.DATABASE.ONLINE_USERS + "/" + key).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    user.setFdbKey(key);
                    isLoading.set(false);
                    isLoggedin.setValue(true);
                })
                .addOnFailureListener(e -> {
                    isLoading.set(false);
                    isLoggedin.setValue(false);
                });
        /*databaseReference.child(Constant.DATABASE.ONLINE_USERS + "/" + user.getEmail()).setValue(user.getName())
                .addOnSuccessListener(aVoid -> {
                    isLoading.set(false);
                    isLoggedin.setValue(true);
                })
                .addOnFailureListener(e -> {
                    isLoading.set(false);
                    isLoggedin.setValue(false);
                });
*/
    }

    public LiveData<Boolean> getLoginStatus() {
        return isLoggedin;
    }

}
