package com.tictactoeandroid.app;
import java.util.ArrayList;
import java.util.List;


public class GameTree {
    private static int SIZE = 3;
    private GameState mBoardState;
    private List<GameTree> mChildren;
    private boolean mIsGameOver;
    private int mValue;

    public GameTree() {
        init();
    }

    public GameTree(GameState state) {
        mBoardState = new GameState();
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                mBoardState.getNode(i, j).type = state.getNode(i, j).type;
                mBoardState.getNode(i, j).xCoord = i;
                mBoardState.getNode(i, j).yCoord = j;
            }
        }
        init();
    }
    
    private void init() {
        this.mChildren = new ArrayList<GameTree>();
        this.mValue = 0;
        this.mIsGameOver = false;
    }

    public boolean ismIsGameOver() {
        return mIsGameOver;
    }

    public void setIsGameOver(boolean mIsGameOver) {
        this.mIsGameOver = mIsGameOver;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int mValue) {
        this.mValue = mValue;
    }

    public GameState getBoardState() {
        return mBoardState;
    }

    public List<GameTree> getChildren() {
        return mChildren;
    }

    
}
