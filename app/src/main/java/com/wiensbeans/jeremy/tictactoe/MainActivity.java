package com.wiensbeans.jeremy.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.os.Handler;

/**
 * This is the only activity for this tic tac toe app. This is the source code for the
 * tic tac toe app released by Wiens Beans. For the GUI, the youtube tutorial by Coding in Flow
 * on youtube was a great help: https://www.youtube.com/watch?v=apDL78MFR3o&t=264s
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;
    private boolean cpuOn = false;
    private boolean easy = true;

    private int roundCount;
    private int gameCount = 0;

    private int player1Points;
    private int player2Points;
    private ToggleButton buttonCPU;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private Switch CPUDifficulty;

    /**
     * Initializing method, starts with the CPU turned off, and the difficulty
     * set to easy.
     * @param savedInstanceState what is this?
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        buttonCPU = findViewById(R.id.button_cpu);
        cpuOn = buttonCPU.isChecked();
        buttonCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCPU();
            }
        });

        CPUDifficulty = findViewById(R.id.CPU_dif);
        easy = !CPUDifficulty.isChecked();
        CPUDifficulty.setVisibility(View.GONE);
        CPUDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDifficulty();
            }});

    }

    /**
     * Handles when the game is being played. Setting the appropriate
     * boxes to the proper letters according to the state of the app.
     *
     * @param v what is this?
     */
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (!player1Turn && cpuOn)
            return;

        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
            if (player1Turn) {
                textViewPlayer1.setText("Player X: " + player1Points + " **");
                if (!cpuOn)
                    textViewPlayer2.setText("Player O: " + player2Points);
                else {
                    textViewPlayer2.setText("CPU: " + player2Points);
                    cpuMove();
                }
            } else {
                textViewPlayer1.setText("Player X: " + player1Points);
                if (!cpuOn)
                    textViewPlayer2.setText("Player O: " + player2Points + " **");
                else {
                    textViewPlayer2.setText("CPU: " + player2Points + " **");
                    cpuMove();
                }
            }
        }

    }
    //Defines and gathers the move the CPU will make if turned on.
    private void cpuMove() {
        int y = 0;
        int x = 0;
        String[][] field  = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        String move =  CPUMove.getMove(field, roundCount, easy);
        x = move.charAt(0) - '0';
        y = move.charAt(1) - '0';

        buttons[x][y].setText("O");
        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
            if (player1Turn) {
                textViewPlayer1.setText("Player X: " + player1Points + " **");
                if (!cpuOn)
                    textViewPlayer2.setText("Player O: " + player2Points);
                else
                    textViewPlayer2.setText("CPU: " + player2Points);
            } else {
                textViewPlayer1.setText("Player X: " + player1Points);
                if (!cpuOn)
                    textViewPlayer2.setText("Player O: " + player2Points + " **");
                else {
                    textViewPlayer2.setText("CPU: " + player2Points + " **");
                    cpuMove();
                }
            }
        }
    }
    //Check if either x or o wins.
    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, "Player X wins!", Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 1000);
    }

    private String player2Wins() {
        player2Points++;
        if (!cpuOn)
            Toast.makeText(this, "Player O wins!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "CPU wins!", Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 1000);


        return "";
    }

    private String draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }

        }, 1000);
        return "";
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        gameCount++;
        if ((gameCount) % 2 == 0) {
            player1Turn = true;
            textViewPlayer1.setText("Player X: " + player1Points + " **");
            if (!cpuOn)
                textViewPlayer2.setText("Player O: " + player2Points);
            else
                textViewPlayer2.setText("CPU: " + player2Points);
        } else {
            player1Turn = false;
            textViewPlayer1.setText("Player X: " + player1Points);
            if (!cpuOn)
                textViewPlayer2.setText("Player O: " + player2Points + " **");
            else {
                textViewPlayer2.setText("CPU: " + player2Points + " **");
                cpuMove();
            }
        }
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        resetBoard();
    }

    private void toggleDifficulty() {
        easy = !CPUDifficulty.isChecked();
    }

    private void toggleCPU() {
        cpuOn = buttonCPU.isChecked();
        if (cpuOn) {
            resetGame();
            textViewPlayer1.setText("Player X: " + player1Points + " **");
            player1Turn = true;
            textViewPlayer2.setText("CPU: " + player2Points);
            CPUDifficulty.setVisibility(View.VISIBLE);
        } else {
            resetGame();
            CPUDifficulty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
        outState.putBoolean("cpuOn", cpuOn);
        outState.putBoolean("easy", easy);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        cpuOn = savedInstanceState.getBoolean("cpuOn");
        easy = savedInstanceState.getBoolean("easy");
    }
}
