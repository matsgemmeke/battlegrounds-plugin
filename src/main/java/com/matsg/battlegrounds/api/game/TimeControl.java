package com.matsg.battlegrounds.api.game;

public interface TimeControl extends Runnable {

    int getTime();

    String formatTime();

    void scheduleRepeatingTask(Runnable runnable, int seconds);

    void setTime(int time);

    void stop();
}