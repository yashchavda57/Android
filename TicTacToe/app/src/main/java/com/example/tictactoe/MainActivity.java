package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button [] buttons = new Button[9];
    private Button resetGame;

    private int playerOneScoreCount, playerTwoScoreCount, roundCount;
    boolean activePlayer;

    //p1 ==> 0
    //p2 ==> 1
    //empty ==>2

    int [] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

    int [][] winningPositions = {

            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},//rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},//columns
            {0, 4, 8}, {2, 4, 6}//cross
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);

        for(int i = 0; i < buttons.length; i++){
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }

        roundCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;
    }

    @Override
    public void onClick(View view) {

        if(!((Button)view).getText().toString().equals("")){
            return;
        }
        String buttonID = view.getResources().getResourceEntryName(view.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length()));

        if (activePlayer){
            ((Button) view).setText("X");
            ((Button) view).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;

        }
        else{
            ((Button) view).setText("0");
            ((Button) view).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;

        }
        roundCount++;


        if (!checkWinner()) {
            if(roundCount == 9){
                playAgain();
                Toast toast = Toast.makeText(this, "No Winner!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                activePlayer = !activePlayer;
            }
        } else {
            if(activePlayer) {

                playerOneScoreCount++;
                updatePlayerScore();
                buttonAction(false);
                Toast toast = Toast.makeText(this, "Player 1 Won!", Toast.LENGTH_LONG);
                toast.show();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playAgain();
                        buttonAction(true);
                    }
                }, 2000);
            }
            else{

                playerTwoScoreCount++;
                updatePlayerScore();
                buttonAction(false);
                Toast toast = Toast.makeText(this, "Player 2 Won!", Toast.LENGTH_LONG);
                toast.show();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playAgain();
                        buttonAction(true);
                    }
                }, 120000);
            }
        }

        if(playerOneScoreCount > playerTwoScoreCount){
            playerStatus.setText("Player 1 is winning!");
        }
        else if(playerOneScoreCount < playerTwoScoreCount){
            playerStatus.setText("Player 2 is winning");
        }
        else{
            playerStatus.setText("");
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
            }
        });
    }

    public boolean checkWinner(){

        boolean winnerResult = false;

        for(int[] winningPosition : winningPositions){
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2){
                winnerResult = true;
            }
        }
        return winnerResult;
    }

    public void updatePlayerScore(){

        playerOneScore.setText(Integer.toString((playerOneScoreCount)));
        playerTwoScore.setText(Integer.toString((playerTwoScoreCount)));
    }

    public void playAgain(){

        roundCount = 0;
        activePlayer = true;

        for(int i = 0; i < buttons.length; i++){
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }

    public void buttonAction(boolean action){
        for(int i=0;i<buttons.length;i++)
        buttons[i].setEnabled(action);
    }
}