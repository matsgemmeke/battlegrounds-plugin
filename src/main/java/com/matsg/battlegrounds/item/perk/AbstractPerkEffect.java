package com.matsg.battlegrounds.item.perk;

import com.matsg.battlegrounds.api.item.PerkEffect;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.lang.reflect.Method;

public abstract class AbstractPerkEffect implements PerkEffect {

    protected ItemStack itemStack;
    protected String name;

    public AbstractPerkEffect(String name, Color color) {
        this.name = name;
        this.itemStack = new ItemStackBuilder(Material.POTION)
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + name)
                .build();

        // In version 1.11 and up, set the potion's color.
        if (ReflectionUtils.getEnumVersion().getValue() >= 11) {
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            try {
                Method method = meta.getClass().getMethod("setColor");
                method.invoke(meta, color);
            } catch (Exception e) { }
            itemStack.setItemMeta(meta);
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }
}
