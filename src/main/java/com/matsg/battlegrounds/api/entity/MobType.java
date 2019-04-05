package com.matsg.battlegrounds.api.entity;

import org.bukkit.entity.EntityType;

public enum MobType {

    ZOMBIE(1, EntityType.ZOMBIE),
    HELLHOUND(2, EntityType.WOLF);

    private EntityType entityType;
    private int id;

    MobType(int id, EntityType entityType) {
        this.id = id;
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getId() {
        return id;
    }
}
