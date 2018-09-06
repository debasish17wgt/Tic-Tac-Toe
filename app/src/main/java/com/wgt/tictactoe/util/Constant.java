package com.wgt.tictactoe.util;

public class Constant {
    public interface DATABASE {
        String DATABASE_NAME = "tic_tac_toe";
        String ONLINE_USERS = "online_users";
        String REQUESTS_GET = "requests_get";
        String REQUESTS_SEND = "requests_send";
        String GAMES = "games";
    }

    public interface PREFERENCE {
        String USER_FILE = "user_data";

        String USER_NAME = "user_name";
        String USER_EMAIL = "user_email";
        String USER_FDB_KEY = "user_fdb_key";
    }

    public interface INTENT {
        String GAME_ID = "game_id";
        String IS_FIRST_PLAYER = "is_first_player";
    }
}
