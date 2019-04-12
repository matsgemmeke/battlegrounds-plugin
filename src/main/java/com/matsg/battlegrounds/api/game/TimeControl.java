package com.matsg.battlegrounds.api.game;

public interface TimeControl extends Runnable {

    int getTime();

    void setTime(int time);

    String formatTime();

    void scheduleRepeatingTask(Runnable runnable, int seconds);

    void start();

    void stop();
}
