package com.matsg.battlegrounds.api.item;

public interface ItemFactory<T extends Item> {

    /**
     * Make a new item based on the given id.
     * This id is used to obtain item data from configuration files.
     *
     * @param id the id of the item
     * @return a new instance of the corresponding item
     */
    T make(String id);
}
