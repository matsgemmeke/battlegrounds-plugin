package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item, Droppable, PlayerProperty, TransactionItem {

    /**
     * Gets the usage context of the weapon.
     *
     * @return the weapon context
     */
    WeaponContext getContext();

    /**
     * Sets the usage context of the weapon.
     *
     * @param context the weapon context
     */
    void setContext(WeaponContext context);

    /**
     * Gets the item slot of the weapon.
     *
     * @return the weapon's item slot
     */
    ItemSlot getItemSlot();

    /**
     * Sets the item slot of the weapon.
     *
     * @param itemSlot the weapon's item slot
     */
    void setItemSlot(ItemSlot itemSlot);

    /**
     * Gets the item type of the item.
     *
     * @return the item's item type
     */
    ItemType getType();

    /**
     * Clones the weapon.
     *
     * @return a clone of the weapon
     */
    Weapon clone();

    /**
     * Gets whether the weapon is being used by its player.
     *
     * @return whether the weapon is in use
     */
    boolean isInUse();

    /**
     * Resets the weapon to its original state.
     */
    void resetState();

    /**
     * Supplies the weapon to its maximum amount.
     */
    void resupply();
}
