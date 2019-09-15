package com.matsg.battlegrounds.api.item;

public class ItemMetadata {

    private final String id;
    private final String description;
    private final String name;

    public ItemMetadata(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the description of the item.
     *
     * @return the item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the id of the item.
     *
     * @return the item id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the item.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }
}
