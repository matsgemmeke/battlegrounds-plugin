package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.inventory.ItemStack;

public interface PerkEffect {

    ItemStack getItemStack();

    String getName();

    void apply(GamePlayer gamePlayer);

    void remove(GamePlayer gamePlayer);
}
