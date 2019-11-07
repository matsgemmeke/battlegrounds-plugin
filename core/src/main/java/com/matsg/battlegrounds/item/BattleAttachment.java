package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.item.GunPart;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BattleAttachment extends BattleItem implements Attachment {

    private boolean toggleable;
    private GunPart gunPart;
    private Map<String, AttributeModifier> modifiers;

    public BattleAttachment(
            ItemMetadata metadata,
            ItemStack itemStack,
            GunPart gunPart,
            Map<String, AttributeModifier> modifiers,
            boolean toggleable
    ) {
        super(metadata, itemStack);
        this.gunPart = gunPart;
        this.modifiers = modifiers;
        this.toggleable = toggleable;
    }

    public GunPart getGunPart() {
        return gunPart;
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public Attachment clone() {
        return (Attachment) super.clone();
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
                .setDisplayName(metadata.getName())
                .setUnbreakable(true)
                .build();
        return true;
    }
}
