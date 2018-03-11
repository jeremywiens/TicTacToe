package com.wiensbeans.jeremy.tictactoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by jeremy on 17/02/18.
 *
 * This is the class to get the cpu's move. This AI will not take over the world.
 * However, it can destroy any nube at tic tac toe.
 *
 * Should have created a function to copy a board...
 */

public class CPUMove {

    // actualField must always represent the given field after it is initialized.
    // thisRound < 10
    private static String[][] actualField = new String[3][3];
    private static int thisRound = 0;
    private static String move = "";

    /**
     * getMove is called to either preform the minimax algorithm if the CPU is on
     * difficult, or to find a random move (with the exception of one move ahead offense and
     * defense) if on easy.
     *
     * @param field is the current state of the board
     * @param round is the current round number
     * @param easy is the cpuDifficulty
     * @return a String which represents the move of which to take. The first digit
     * is the index of the which column and the second digit is the index of which row.
     */
    public static String getMove(String[][] field, int round, boolean easy) {

        if(round == 0)
            return firstMove();

        //copy board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                actualField[i][j] = field[i][j];
            }
        }
        if(easy){
            return getEasyMove(field);
        }

        thisRound = round;
        HashMap<String, Integer> possibleMoves = new HashMap<>();
        String possibleMove = "";

        //Determine the possible moves and if they will win, draw, or lose.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //copy board
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        field[x][y] = actualField[x][y];
                    }
                }
                if (field[i][j] == "") {
                    field[i][j] = "O";
                    possibleMove = Integer.toString(i) + Integer.toString(j);
                    possibleMoves.put(possibleMove, miniMax(field, false, round + 1));
                }
            }
        }
        String bestMove = "";
        int best = -10;
        for (String s : possibleMoves.keySet()) {
            if (possibleMoves.get(s) > best) {
                best = possibleMoves.get(s);
                bestMove = s;
            }
        }
        List<String> randomChoice = new ArrayList<>();
        for (String s : possibleMoves.keySet()){
            if (possibleMoves.get(s) == best){
                randomChoice.add(s);
            }}
        // If multiple and good options, it will return a random move in order
        // to avoid repetition.
        if(randomChoice.size() > 1){
            Random random = new Random();
            int index = random.nextInt(randomChoice.size()) + 0;
            return randomChoice.get(index);
        }

        return bestMove;

    }

    // Return a random but strategic first move to save time.
    private static String firstMove(){

        Random rand = new Random();

        int  index = rand.nextInt(5) + 0;
        String[] corners = {"00","02","20", "22", "11"};

        return corners[index];
    }

    //Easy move will take a win if its right there. And will defend
    //against a loss if the opponent can win on the next turn. Otherwise
    //it will make a random move.
    private static String getEasyMove(String[][] field){
        String[][] thisField = new String[3][3];
        List<String> offense = new ArrayList<>();
        List<String> defense = new ArrayList<>();
        List<String> available = new ArrayList<>();
        String move = "";
        //copy board
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                thisField[x][y] = field[x][y];
            }
        }
        //copy board and find move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        thisField[x][y] = field[x][y];
                    }
                }
                if (thisField[i][j] == "") {
                    move = Integer.toString(i) + Integer.toString(j);
                    available.add(move);
                    thisField[i][j] = "O";
                    if(checkForOWin(thisField)){
                        offense.add(move);
                    }
                    thisField[i][j] = "X";
                    if(checkForXWin(thisField)){
                        defense.add(move);
                    }
                }
            }
        }
        Random rand = new Random();
        if(!offense.isEmpty()){
            int index = rand.nextInt(offense.size()) + 0;
            return offense.get(index);
        }
        else if(!defense.isEmpty()){
            int index = rand.nextInt(defense.size()) + 0;
            return defense.get(index);
        }
        else{
            int index = rand.nextInt(available.size()) + 0;
            return available.get(index);
        }
    }
    //Recursively gets called and returns an int based on how the game finished.
    //Every possible game is played out.
    private static int miniMax(String[][] field, boolean playerOTurn, int round) {

        if (checkForOWin(field)) {
            return 5;
        }
        if (checkForXWin(field)) {
            return -5;
        }
        if (round == 9) {
            return 0;
        }
        round++;
        if (playerOTurn) {
            return getMoveCPU(field, round);
        } else {
            return getMoveHuman(field, round);
        }

    }
    // The goal is to choose the move with the highest score.
    private static int getMoveCPU(String[][] field, int round) {
        String[][] thisField = new String[3][3];
        int score = 11;
        int best = -10;
        //copy board
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                thisField[x][y] = field[x][y];
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //copy board
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        thisField[x][y] = field[x][y];
                    }
                }
                if (thisField[i][j] == "") {
                    thisField[i][j] = "O";
                    score = miniMax(thisField, false, round);
                    if (score > best) {
                        best = score;
                        move = Integer.toString(i) + Integer.toString(j);
                    }
                }

            }
        }
        int i = move.charAt(0) - '0';
        int j = move.charAt(1) - '0';
        field[i][j] = "O";
        return best;
    }
    //The goal is to chose to move with the lowest score.
    private static int getMoveHuman(String[][] field, int round) {
        String[][] thisField = new String[3][3];
        int score = -11;
        int best = 10;
        //copy board
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                thisField[x][y] = field[x][y];
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //copy board
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        thisField[x][y] = field[x][y];
                    }
                }
                if (thisField[i][j] == "") {
                    thisField[i][j] = "X";
                    score = miniMax(thisField, true, round);
                    if (score < best) {
                        best = score;
                        move = Integer.toString(i) + Integer.toString(j);
                    }
                }

            }
        }
        int i = move.charAt(0) - '0';
        int j = move.charAt(1) - '0';
        field[i][j] = "X";
        return best;
    }

    private static boolean checkForXWin(String[][] field) {

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && field[i][0].equals("X")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && field[0][i].equals("X")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && field[0][0].equals("X")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && field[0][2].equals("X")) {
            return true;
        }

        return false;
    }

    private static boolean checkForOWin(String[][] field) {

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && field[i][0].equals("O")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && field[0][i].equals("O")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && field[0][0].equals("O")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && field[0][2].equals("O")) {
            return true;
        }

        return false;
    }


}
