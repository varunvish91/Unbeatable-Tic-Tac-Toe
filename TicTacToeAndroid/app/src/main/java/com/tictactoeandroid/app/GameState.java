package com.tictactoeandroid.app;
import com.tictactoeandroid.app.Cell;
import com.tictactoeandroid.app.Node;


public class GameState {
    private static int SIZE = 3;
    private Node[][] board;

    public GameState() {
        board = new Node[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new Node(Cell.NONE, i, j);
            }
        }
    }

    public Node getNode(int i, int j) {
        return board[i][j];
    }

    public boolean boardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].type == Cell.NONE) {
                    return false;
                }
            }
        }
        return true;
    }

    //checks if the given Cell type has won
    public boolean isInWinState(Cell type) {

        //check horizontal
        int winCount;
        for (int i = 0; i < SIZE; i++) {
            winCount = 0;
            for (int j = 0; j < SIZE; j++) {
                //check horizontal
                if (board[i][j].type == type) {
                    winCount++;
                }
            }
            if (winCount == 3) {
                return true;
            }
        }

        //check vertical
        for (int i = 0; i < SIZE; i++) {
            winCount = 0;
            for (int j = 0; j < SIZE; j++) {
                if (board[j][i].type == type) {
                    winCount++;
                }
            }
            if (winCount == 3) {
                return true;
            }
        }

        int i = 0;
        int j = SIZE - 1;
        winCount = 0;
        while (i < SIZE && j >= 0) {
            if (board[i][j].type == type) {
                winCount++;
            }
            if (winCount == 3) {
                return true;
            }
            i++;
            j--;
        }

        //check the other diagonal
        i = 0;
        j = 0;
        winCount = 0;
        while (i < SIZE && j < SIZE) {
            if (board[i][j].type == type) {
                winCount++;
            }
            if (winCount == 3) {
                return true;
            }
            i++;
            j++;
        }
        return false;
    }

    //
}
