package com.matsg.battlegrounds.api.game;

public interface TimeControl {

    int getTime();

    String formatTime();

    void setTime(int time);

    void stop();
}