package com.matsg.battlegrounds.util;

public class Pair<X, Y> {

    private final X x;
    private final Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X left() {
        return x;
    }

    public Y right() {
        return y;
    }
}
