package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.Location;
import org.bukkit.block.Block;

public interface MysteryBox extends ArenaComponent, Interactable, Lockable, Priceable, Watchable {

    /**
     * Gets the weapon that was given in the previous usage of the mystery box. Returns null
     * if the mystery box is not used yet.
     *
     * @return the current weapon of the mystery box
     */
    Weapon getCurrentWeapon();

    /**
     * Sets the weapon that was given in the previous usage of the mystery box.
     *
     * @param currentWeapon the current weapon of the mystery box
     */
    void setCurrentWeapon(Weapon currentWeapon);

    /**
     * Gets the left side block of the double chest.
     *
     * @return The left block.
     */
    Block getLeftSide();

    /**
     * Gets the right side block of the double chest.
     *
     * @return The right block.
     */
    Block getRightSide();

    /**
     * Gets the amount of rolls the mystery box has performed since its activation.
     *
     * @return the amount of rolls
     */
    int getRolls();

    /**
     * Sets the amount of rolls the mystery box has performed since its activation.
     *
     * @param rolls the amount of rolls
     */
    void setRolls(int rolls);

    /**
     * Gets the state of the mystery box or null if the mystery box is not active.
     *
     * @return the mystery box state
     */
    MysteryBoxState getState();

    /**
     * Sets the state of the mystery box.
     *
     * @param state the mystery box state
     */
    void setState(MysteryBoxState state);

    /**
     * Gets the location where the mystery box drops its items during rolls.
     *
     * @return the mystery box item drop location
     */
    Location getItemDropLocation();

    /**
     * Gets the weapon selection inside the mystery box.
     *
     * @return the mystery box weapons
     */
    Weapon[] getWeapons();

    /**
     * Gets whether the mystery box is currently active and available for use.
     *
     * @return Whether the mystery box is active.
     */
    boolean isActive();

    /**
     * Sets whether the mystery box is currently active and available for use.
     *
     * @return Whether the mystery box should be active.
     */
    void setActive(boolean active);

    /**
     * Plays a chest animation on the mystery box's location
     *
     * @param open whether to play the opening or closing animation
     */
    void playChestAnimation(boolean open);
}
