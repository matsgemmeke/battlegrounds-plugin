package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.util.MessageHelper;

public enum FirearmType implements ItemType {

    ASSAULT_RIFLE("item-type-assault-rifle", ItemSlot.FIREARM_PRIMARY, 1, Bullet.class, 1, false),
    HANDGUN("item-type-handgun", ItemSlot.FIREARM_SECONDARY, 1, Bullet.class, 1, false),
    LAUNCHER("item-type-launcher", ItemSlot.FIREARM_SECONDARY, 1, Lethal.class, 1, false),
    LIGHT_MACHINE_GUN("item-type-light-machine-gun", ItemSlot.FIREARM_PRIMARY, 1, Bullet.class, 1, false),
    SHOTGUN("item-type-shotgun", ItemSlot.FIREARM_PRIMARY, 5, Bullet.class, 1, false),
    SNIPER_RIFLE("item-type-sniper-rifle", ItemSlot.FIREARM_PRIMARY, 1, Bullet.class, 5, true),
    SUBMACHINE_GUN("item-type-submachine-gun", ItemSlot.FIREARM_PRIMARY,1, Bullet.class, 1, false);

    public static FirearmType[] GUNS = new FirearmType[] { ASSAULT_RIFLE, HANDGUN, LIGHT_MACHINE_GUN, SHOTGUN, SNIPER_RIFLE, SUBMACHINE_GUN };

    private boolean pierceable, scope;
    private Class<? extends DamageSource> damageSourceClass;
    private int maxHits, projectileAmount;
    private ItemSlot itemSlot;
    private MessageHelper messageHelper;
    private String name;

    FirearmType(String path, ItemSlot itemSlot, int projectileAmount, Class<? extends DamageSource> damageSourceClass, int maxHits, boolean scope) {
        this.damageSourceClass = damageSourceClass;
        this.itemSlot = itemSlot;
        this.maxHits = maxHits;
        this.messageHelper = new MessageHelper();
        this.projectileAmount = projectileAmount;
        this.pierceable = maxHits > 1;
        this.scope = scope;
        this.name = messageHelper.create(path);
    }

    public ItemSlot getDefaultItemSlot() {
        return itemSlot;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public String getName() {
        return name;
    }

    public int getProjectileAmount() {
        return projectileAmount;
    }

    public Class<? extends DamageSource> getDamageSourceClass() {
        return damageSourceClass;
    }

    public boolean hasScope() {
        return scope;
    }

    public boolean hasSubTypes() {
        return true;
    }

    public boolean isPierceable() {
        return pierceable;
    }

    public boolean isRemovable() {
        return true;
    }
}