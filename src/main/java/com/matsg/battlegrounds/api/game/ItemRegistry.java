package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public interface ItemRegistry {

    void addItem(Item item);

    void clear();

    Item getItem(GamePlayer gamePlayer, ItemStack itemStack);

    Item getItemIgnoreMetadata(GamePlayer gamePlayer, ItemStack itemStack);

    void interact(Item item, Action action);

    void removeItem(Item item);
}