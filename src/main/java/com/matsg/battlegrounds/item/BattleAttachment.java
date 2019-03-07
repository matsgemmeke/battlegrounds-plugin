package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.AttributeModifier;
import com.matsg.battlegrounds.api.item.GunPart;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BattleAttachment extends BattleItem implements Attachment {

    private boolean toggleable;
    private GunPart gunPart;
    private Map<String, AttributeModifier> modifiers;
    private short durability;
    private String description;

    public BattleAttachment(String id, String name, String description, ItemStack itemStack, short durability, GunPart gunPart, Map<String, AttributeModifier> modifiers, boolean toggleable) {
        super(id, name, itemStack);
        this.description = description;
        this.durability = durability;
        this.gunPart = gunPart;
        this.modifiers = modifiers;
        this.toggleable = toggleable;
    }

    public Attachment clone() {
        return (Attachment) super.clone();
    }

    public String getDescription() {
        return description;
    }

    public GunPart getGunPart() {
        return gunPart;
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public AttributeModifier getModifier(String attribute) {
        if (modifiers.containsKey(attribute)) {
            return modifiers.get(attribute);
        }
        return null;
    }

    public boolean update() {
        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(1)
                .setDisplayName(name)
                .setDurability(durability)
                .setUnbreakable(true)
                .build();
        return true;
    }
}