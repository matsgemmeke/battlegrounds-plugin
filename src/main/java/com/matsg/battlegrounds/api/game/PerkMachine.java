package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.Perk;
import org.bukkit.block.Sign;

public interface PerkMachine extends ArenaComponent, Interactable, Lockable {

    int getMaxBuys();

    Perk getPerk();

    int getPrice();

    void setPrice(int price);

    Sign getSign();
}
