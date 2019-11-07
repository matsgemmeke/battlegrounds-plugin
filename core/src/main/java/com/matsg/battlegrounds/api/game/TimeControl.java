package com.matsg.battlegrounds.api.game;

public interface TimeControl extends Runnable {

    /**
     * Gets the amount of passed time in the game.
     *
     * @return the amount of passed time
     */
    int getTime();

    /**
     * Sets the amount of passed time in the game.
     *
     * @param time the amount of passed time
     */
    void setTime(int time);

    /**
     * Formats the time as a readable text value.
     *
     * @return the time formatted in text
     */
    String formatTime();

    /**
     * Adds a repeating task to the time control.
     *
     * @param runnable the task
     * @param seconds the amount of seconds between the executions of the tasks
     */
    void scheduleRepeatingTask(Runnable runnable, int seconds);

    /**
     * Starts the time control.
     */
    void start();

    /**
     * Stops the time control.
     */
    void stop();
}
