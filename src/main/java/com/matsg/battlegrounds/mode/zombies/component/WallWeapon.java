package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.block.Chest;

public interface WallWeapon extends ArenaComponent, Interactable, Lockable, Priceable, Watchable {

    /**
     * Gets the chest block of the wall weapon.
     *
     * @return the chest block
     */
    Chest getChest();

    /**
     * Gets the weapon the wall weapon sells.
     *
     * @return the wall weapon's item
     */
    Weapon getWeapon();
}
