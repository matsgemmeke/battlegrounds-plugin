package com.matsg.battlegrounds.api.game;

public interface TimeControl {

    String getFormatTime();

    int getTime();

    void setTime(int time);

    void stop();
}