package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.entity.ItemFrame;

public interface WallWeapon extends ArenaComponent, Interactable, Lockable, Priceable, Watchable {

    /**
     * Gets the item frame of the wall weapon.
     *
     * @return the item frame
     */
    ItemFrame getItemFrame();

    /**
     * Gets the weapon the wall weapon sells.
     *
     * @return the wall weapon's item
     */
    Weapon getWeapon();
}
