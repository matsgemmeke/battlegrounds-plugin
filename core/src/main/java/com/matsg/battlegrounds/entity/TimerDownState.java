package com.matsg.battlegrounds.entity;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.DownState;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimerDownState implements DownState {

    @NotNull
    private GamePlayer gamePlayer;
    @Nullable
    private GamePlayer reviver;
    @NotNull
    private Location location;
    private long duration;
    @NotNull
    private TaskRunner taskRunner;

    public TimerDownState(@NotNull GamePlayer gamePlayer, @NotNull Location location, @NotNull TaskRunner taskRunner, long duration) {
        this.gamePlayer = gamePlayer;
        this.location = location;
        this.taskRunner = taskRunner;
        this.duration = duration;
    }

    @NotNull
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    @Nullable
    public GamePlayer getReviver() {
        return reviver;
    }

    public void setReviver(@Nullable GamePlayer reviver) {
        this.reviver = reviver;
    }

    public void run() {

    }
}
