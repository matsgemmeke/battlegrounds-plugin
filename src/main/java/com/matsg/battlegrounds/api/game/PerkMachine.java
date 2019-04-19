package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.Perk;
import org.bukkit.block.Sign;

public interface PerkMachine extends ArenaComponent, Interactable, Lockable, Priceable {

    int getMaxBuys();

    Perk getPerk();

    Sign getSign();

    String[] getSignLayout();

    void setSignLayout(String[] layout);

    boolean updateSign();
}
