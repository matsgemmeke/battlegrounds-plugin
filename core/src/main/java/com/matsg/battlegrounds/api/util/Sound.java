package com.matsg.battlegrounds.api.util;

import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface Sound {

    org.bukkit.Sound getBukkitSound();

    long getDelay();

    void setDelay(long delay);

    float getPitch();

    void setPitch(float pitch);

    float getVolume();

    void setVolume(float volume);

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    void play(Game game);

    void play(Game game, Entity entity);

    void play(Game game, Location location);

    void play(Game game, Location location, float pitch);
}
