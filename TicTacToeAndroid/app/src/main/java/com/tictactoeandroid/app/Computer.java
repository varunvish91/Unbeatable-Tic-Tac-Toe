package com.tictactoeandroid.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Collections;
import java.util.Comparator;

//Computer controller, runs in background thread and implements MinMax algorithm to pick its moves
public class Computer extends AsyncTask<GameState, Void, GameTree> {
    private static int MINIMIZER = -10;
    private static int MAXIMIZER = 10;
    private static int SIZE = 3;
    private ComputerPlayerCallBack mCallBack;
    private ProgressDialog mProgressDialog;

    //callback to send to the UI the move that was picked
    public Computer(ComputerPlayerCallBack callback, Context context) {
        this.mCallBack = callback;
        mProgressDialog = new ProgressDialog(context);
    }

    @Override
    protected GameTree doInBackground(GameState... gameState) {
        GameState currentState = gameState[0];
        GameTree compTree = runComputerMaxiMizer(new GameTree(currentState), true);

        //sort the children for the minimizer to select from, pick the one that minimizes the most
        Collections.sort(compTree.getChildren(), new Comparator<GameTree>() {
            @Override
            public int compare(GameTree lhs, GameTree rhs) {
                return lhs.getValue() < rhs.getValue() ? -1 : 1;
            }
        });
        if(compTree.getChildren().isEmpty()) {
            return compTree;
        }
        else {
            return compTree.getChildren().get(0);
        }
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Just a Minute");
        mProgressDialog.setMessage("Computer is Thinking...");
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(GameTree gameTree) {
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mCallBack.computerMove(gameTree);
    }

    private GameTree runComputerMaxiMizer(GameTree root, boolean isComputerTurn) {
        //check if the previous turn was a winning move
        Cell winner = isComputerTurn ? Cell.CROSS : Cell.CIRCLE;
        if(root.getBoardState().isInWinState(winner)) {
            if(winner == Cell.CROSS) {
                root.setValue(MAXIMIZER);
            }
            else {
                root.setValue(MINIMIZER);
            }
            root.setIsGameOver(true);
            return root;
        }
        else if(root.getBoardState().boardFull()) {
            root.setValue(MINIMIZER);
            root.setIsGameOver(true);
            return root;
        }
        else {
            generateMoves(root, isComputerTurn);

            //print the children
            for(GameTree item : root.getChildren()) {
                runComputerMaxiMizer(item, !isComputerTurn);
            }
        }

        int sum = 0;
        for (GameTree item : root.getChildren()) {
            sum += item.getValue();
        }
        root.setValue(sum);
        return root;
    }

    //generates all the moves the minimizer/maximizer can play
    private void generateMoves(GameTree root, boolean isComputerTurn) {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE;j++) {
                if(root.getBoardState().getNode(i, j).type == Cell.NONE) {
                    Cell type = isComputerTurn ? Cell.CIRCLE : Cell.CROSS;
                    GameTree tree = new GameTree(root.getBoardState());
                    tree.setIsGameOver(false);
                    tree.getBoardState().getNode(i, j).type = type;
                    root.getChildren().add(tree);
                }
            }
        }
    }
}
