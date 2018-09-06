package com.wgt.tictactoe.util;


public class StringUtility {

    static class Board {
        public int row;
        public int column;

        public Board(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public static String stringFromNumbers(int... numbers) {
        StringBuilder sNumbers = new StringBuilder();
        for (int number : numbers)
            sNumbers.append(number);
        return sNumbers.toString();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static Board getBoardFromStrRC(String rc) {
        Board board = null;
        if (rc.length() != 2) {
            return null;
        }
        String row = "" + rc.charAt(0);
        String column = "" + rc.charAt(1);
        try {
            board = new Board(
                    Integer.parseInt(row),
                    Integer.parseInt(column)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return board;
    }
}
