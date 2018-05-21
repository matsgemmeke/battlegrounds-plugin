package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.item.WeaponType;

public enum FireArmType implements WeaponType {

    ASSAULT_RIFLE("item-type-assault-rifle", 1, Bullet.class, 1, false),
    HANDGUN("item-type-handgun", 1, Bullet.class, 1, false),
    LAUNCHER("item-type-launcher", 1, Lethal.class, 1, false),
    LIGHT_MACHINE_GUN("item-type-light-machine-gun", 1, Bullet.class, 1, false),
    SHOTGUN("item-type-shotgun", 5, Bullet.class, 1, false),
    SNIPER_RIFLE("item-type-sniper-rifle", 1, Bullet.class, 5, true),
    SUBMACHINE_GUN("item-type-submachine-gun", 1, Bullet.class, 1, false);

    public static FireArmType[] GUNS = new FireArmType[] { ASSAULT_RIFLE, HANDGUN, LIGHT_MACHINE_GUN, SHOTGUN, SNIPER_RIFLE, SUBMACHINE_GUN };

    private Battlegrounds plugin;
    private boolean pierceable, scope;
    private Class<? extends DamageSource> damageSourceClass;
    private int maxHits, projectileAmount;
    private String name;

    FireArmType(String path, int projectileAmount, Class<? extends DamageSource> damageSourceClass, int maxHits, boolean scope) {
        this.plugin = BattlegroundsPlugin.getPlugin();
        this.damageSourceClass = damageSourceClass;
        this.maxHits = maxHits;
        this.name = plugin.getTranslator().getTranslation(path);
        this.projectileAmount = projectileAmount;
        this.pierceable = maxHits > 1;
        this.scope = scope;
    }

    public static FireArmType getValue(String name) {
        for (FireArmType fireArmType : values()) {
            if (fireArmType.toString().equals(name)) {
                return fireArmType;
            }
        }
        return null;
    }

    public ItemSlot getDefaultItemSlot() {
        return ItemSlot.FIREARM_PRIMARY;
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

    public boolean isPierceable() {
        return pierceable;
    }
}