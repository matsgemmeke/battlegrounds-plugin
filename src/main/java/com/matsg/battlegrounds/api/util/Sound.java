package com.matsg.battlegrounds.api.util;

import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface Sound {

    long getDelay();

    org.bukkit.Sound getEnumSound();

    float getPitch();

    float getVolume();

    boolean isCancelled();

    void play(Game game);

    void play(Game game, Entity entity);

    void play(Game game, Location location);

    void play(Game game, Location location, float pitch);

    void setCancelled(boolean cancelled);

    void setDelay(long delay);

    void setPitch(float pitch);
}