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

    public EntityType getEntityType() {
        return entityType;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }
}
