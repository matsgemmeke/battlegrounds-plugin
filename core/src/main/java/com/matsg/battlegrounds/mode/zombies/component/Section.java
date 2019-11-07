package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.game.*;

public interface Section extends ArenaComponent, ComponentWrapper, Lockable, Priceable {

    /**
     * Gets the barricade container of the section.
     *
     * @return the barricade container
     */
    ComponentContainer<Barricade> getBarricadeContainer();

    /**
     * Gets the door container of the section.
     *
     * @return The door container.
     */
    ComponentContainer<Door> getDoorContainer();

    /**
     * Gets the mob spawn container of the section.
     *
     * @return The mob spawn container.
     */
    ComponentContainer<MobSpawn> getMobSpawnContainer();

    /**
     * Gets the mystery box container of the section.
     *
     * @return The mystery box container.
     */
    ComponentContainer<MysteryBox> getMysteryBoxContainer();

    /**
     * Gets the name of the section.
     *
     * @return The section name.
     */
    String getName();

    /**
     * Gets the perk machine container of the section.
     *
     * @return The perk machine container.
     */
    ComponentContainer<PerkMachine> getPerkMachineContainer();

    /**
     * Gets the wall weapon container of the section.
     *
     * @return The wall weapon container.
     */
    ComponentContainer<WallWeapon> getWallWeaponContainer();

    /**
     * Gets whether the section is the first one unlocked.
     *
     * @return whether the section is unlocked by default
     */
    boolean isUnlockedByDefault();
}
