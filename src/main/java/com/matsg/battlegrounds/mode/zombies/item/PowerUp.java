package com.matsg.battlegrounds.mode.zombies.item;

import com.matsg.battlegrounds.api.item.Droppable;
import com.matsg.battlegrounds.api.item.Item;

public interface PowerUp extends Item, Droppable {

    PowerUpEffect getEffect();

    void setEffect(PowerUpEffect powerUpEffect);

    String getName();

    boolean isActive();

    void setActive(boolean active);

    PowerUp clone();
}
