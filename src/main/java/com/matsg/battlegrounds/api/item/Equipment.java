package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.entity.Item;

public interface Equipment extends Weapon, DamageSource, Droppable {

    int getAmount();

    void setAmount(int amount);

    Item getFirstDroppedItem();

    Sound[] getIgnitionSound();

    int getIgnitionTime();

    int getMaxAmount();

    double getVelocity();

    void setVelocity(double velocity);

    boolean isBeingThrown();

    Equipment clone();

    void deployEquipment();

    void deployEquipment(double velocity);

    void ignite(Item item);
}
