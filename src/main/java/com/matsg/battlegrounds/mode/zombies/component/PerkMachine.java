package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.Interactable;
import com.matsg.battlegrounds.api.game.Lockable;
import com.matsg.battlegrounds.api.game.Priceable;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import org.bukkit.block.Sign;

public interface PerkMachine extends ArenaComponent, Interactable, Lockable, Priceable {

    /**
     * Gets the maximum amount of times the perk machine can be used by a single player.
     *
     * @return The maximum buys per player.
     */
    int getMaxBuys();

    /**
     * Gets the perk the machine is selling.
     *
     * @return The perk of the machine.
     */
    Perk getPerk();

    /**
     * Gets the sign being used to sells the perk items.
     *
     * @return The sign of the machine.
     */
    Sign getSign();

    /**
     * Gets the sign layout of the perk machine.
     *
     * @return The sign layout.
     */
    String[] getSignLayout();

    /**
     * Sets the sign layout of the perk machine.
     *
     * @param layout The sign layout.
     */
    void setSignLayout(String[] layout);

    /**
     * Updates the sign block.
     *
     * @return Whether the sign was updated.
     */
    boolean updateSign();
}
