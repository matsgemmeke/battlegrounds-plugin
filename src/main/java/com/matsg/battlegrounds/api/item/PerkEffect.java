package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.inventory.ItemStack;

public interface PerkEffect {

    void apply(GamePlayer gamePlayer);

    ItemStack getItemStack();

    String getName();

    void remove(GamePlayer gamePlayer);
}
