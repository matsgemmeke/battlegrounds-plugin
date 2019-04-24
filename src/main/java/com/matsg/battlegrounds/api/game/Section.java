package com.matsg.battlegrounds.api.game;

public interface Section extends ArenaComponent, ComponentWrapper, Lockable, Priceable {

    /**
     * Gets all components that the section has. This collection is immutable. If a component needs to be added to the
     * section then it must use its specific container.
     *
     * @return All container components.
     */
    Iterable<ArenaComponent> getComponents();

    /**
     * Gets the door container of the section.
     *
     * @return The door container.
     */
    ComponentContainer<Door> getDoorContainer();

    /**
     * Gets the item chest container of the section.
     *
     * @return The item chest container.
     */
    ComponentContainer<ItemChest> getItemChestContainer();

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
}
