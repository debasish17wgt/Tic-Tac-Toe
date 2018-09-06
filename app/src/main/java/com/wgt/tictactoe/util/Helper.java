package com.wgt.tictactoe.util;

import java.util.HashMap;
import java.util.Map;

public class Helper {
    public static Map<String, String> getEmptyCell(int row, int col) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map.put("" + i + j, "");
            }
        }
        return map;
    }
}
