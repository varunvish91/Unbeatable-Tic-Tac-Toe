package com.tictactoeandroid.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Controller extends ActionBarActivity implements View.OnClickListener, ComputerPlayerCallBack {
    private static int SIZE = 3;
    private Button[][] mButtons;
    GameTree mRoot = null;
    private HashMap<Button, Node> mButtonMap = new HashMap<Button, Node>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        mRoot = new GameTree(new GameState());
        initGame();

        //display ftue message describing the game to the user
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if(!prefs.getBoolean("ftueMessage", false)) {
            displayMessage(getResources().getString(R.string.welcome), getResources().getString(R.string.welcome_message), false);
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putBoolean("ftueMessage", true);
            editor.commit();
        }

    }

    //initialize our button map and the buttons themselves
    private void initGame() {
        mButtons = new Button[SIZE][SIZE];
        mButtons[0][0] = (Button) findViewById(R.id.one);
        mButtons[0][1] = (Button) findViewById(R.id.two);
        mButtons[0][2] = (Button) findViewById(R.id.three);
        mButtons[1][0] = (Button) findViewById(R.id.four);
        mButtons[1][1] = (Button) findViewById(R.id.five);
        mButtons[1][2] = (Button) findViewById(R.id.six);
        mButtons[2][0] = (Button) findViewById(R.id.seven);
        mButtons[2][1] = (Button) findViewById(R.id.eight);
        mButtons[2][2] = (Button) findViewById(R.id.nine);

        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                mButtonMap.put(mButtons[i][j], mRoot.getBoardState().getNode(i, j));
                mButtons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        //check if the node is taken
        Button clickedButton = (Button) view;
        Node node = mButtonMap.get(clickedButton);
        if(node.type == Cell.NONE) {
            //update the clicked node, to reflect the pressed state
            mRoot.getBoardState().getNode(node.xCoord, node.yCoord).type = Cell.CROSS;
            mButtons[node.xCoord][node.yCoord].setText("X");

            //check if the user just won
            if(mRoot.getBoardState().isInWinState(Cell.CROSS)) {
                mRoot.setIsGameOver(true);
                displayMessage(getResources().getString(R.string.game_over), getResources().getString(R.string.winner), true);
            }
            else {
                //run the computer move
                Computer player = new Computer(this, this);
                player.execute(mRoot.getBoardState());
            }
        }
        else {
            displayMessage(getResources().getString(R.string.error), getResources().getString(R.string.invalid_move), false);
        }
    }

    @Override
    public void computerMove(GameTree computerMove) {
        //check if the computer's selected move won it the game
        if(computerMove.ismIsGameOver()) {
            displayMessage(getResources().getString(R.string.game_over), getResources().getString(R.string.computer_wins), true);
        }

        //update the game state to reflect the computers moves
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                if(computerMove.getBoardState().getNode(i, j).type == Cell.CROSS) {
                    mButtons[i][j].setText(getResources().getString(R.string.cross));
                    mButtonMap.get(mButtons[i][j]).type = Cell.CROSS;
                }
                else if (computerMove.getBoardState().getNode(i, j).type == Cell.CIRCLE) {
                    mButtons[i][j].setText(getResources().getString(R.string.circle));
                    mButtons[i][j].setTextColor(Color.RED);
                    mButtonMap.get(mButtons[i][j]).type = Cell.CIRCLE;

                }
            }
        }

        //reset our root to reflect current state of the game
        mRoot = computerMove;
    }

    //general popup window handling, can display a game over message or just an informational popup, designated by startNewGame being false
    private void displayMessage(String title, String message, boolean startNewGame) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        if(startNewGame) {
            alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(getResources().getString(R.string.new_game), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(Controller.this, Controller.class));
                }
            });
        }
        else {
            alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
