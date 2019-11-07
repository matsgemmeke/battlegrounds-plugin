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
     * @return the maximum buys per player
     */
    int getMaxBuys();

    /**
     * Sets the maximum amount of times the perk machine can be used by a single player.
     *
     * @param maxBuys the maximum buys per player
     */
    void setMaxBuys(int maxBuys);

    /**
     * Gets the perk the machine is selling.
     *
     * @return the perk of the machine
     */
    Perk getPerk();

    /**
     * Sets the perk the machine is selling.
     *
     * @param perk the perk of the machine
     */
    void setPerk(Perk perk);

    /**
     * Gets the sign being used to sells the perk items.
     *
     * @return the sign of the machine
     */
    Sign getSign();

    /**
     * Sets the sign being used to sells the perk items.
     *
     * @param sign the sign of the machine
     */
    void setSign(Sign sign);

    /**
     * Gets the sign layout of the perk machine.
     *
     * @return the sign layout
     */
    String[] getSignLayout();

    /**
     * Sets the sign layout of the perk machine.
     *
     * @param layout the sign layout
     */
    void setSignLayout(String[] layout);

    /**
     * Updates the sign block.
     *
     * @return whether the sign was updated
     */
    boolean updateSign();
}
