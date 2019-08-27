package com.matsg.battlegrounds.api.entity;

import org.bukkit.entity.EntityType;

public enum BattleEntityType {

    PLAYER(1, "player", EntityType.PLAYER),
    ZOMBIE(2, "zombie", EntityType.ZOMBIE),
    HELLHOUND(3, "hellhound", EntityType.WOLF);

    private EntityType entityType;
    private int id;
    private String name;

    BattleEntityType(int id, String name, EntityType entityType) {
        this.id = id;
        this.name = name;
        this.entityType = entityType;
    }

    /**
     * Gets the bukkit entity type of this entity type.
     *
     * @return the bukkit entity type
     */
    public EntityType getBukkitEntityType() {
        return entityType;
    }

    /**
     * Gets the id of the entity type.
     *
     * @return the entity type's id
     */
    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }
}
