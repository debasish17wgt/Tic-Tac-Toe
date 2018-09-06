package com.wgt.tictactoe.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.wgt.tictactoe.model.User;
import com.wgt.tictactoe.util.Constant;

public class UserCredPref {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public UserCredPref(Context context) {
        preferences = context.getSharedPreferences(Constant.PREFERENCE.USER_FILE, context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveData(User user) {
        editor.putString(Constant.PREFERENCE.USER_NAME, user.getName());
        editor.putString(Constant.PREFERENCE.USER_EMAIL, user.getEmail());
        editor.putString(Constant.PREFERENCE.USER_FDB_KEY, user.getFdbKey());
        editor.apply();
    }

    public User getUserDetails() {
        return new User(
                preferences.getString(Constant.PREFERENCE.USER_NAME, null),
                preferences.getString(Constant.PREFERENCE.USER_EMAIL, null),
                preferences.getString(Constant.PREFERENCE.USER_FDB_KEY, null)
        );
    }

    public void logout() {
        editor.putString(Constant.PREFERENCE.USER_NAME, null);
        editor.putString(Constant.PREFERENCE.USER_EMAIL, null);
        editor.putString(Constant.PREFERENCE.USER_FDB_KEY, null);
        editor.apply();
    }
}
