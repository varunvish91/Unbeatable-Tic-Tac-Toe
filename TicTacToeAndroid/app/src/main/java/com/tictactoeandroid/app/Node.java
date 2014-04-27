package com.tictactoeandroid.app;


import android.widget.Button;

public class Node {
    public Cell type;
    public int xCoord;
    public int yCoord;

    public Node(Cell type, int x, int y) {
        this.type = type;
        this.xCoord = x;
        this.yCoord = y;

    }
}
