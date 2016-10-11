package com.mike.backend.db;

/**
 * Created by mike on 6/17/2016.
 */
public enum MessageState {
    New(0),
    Error(-1),
    Loaded(1);

    private int value;

    private MessageState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
