package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Item extends Cloneable, Comparable<Item> {

    ItemAttribute getAttribute(String id);

    Game getGame();

    Item clone();

    String getId();

    ItemStack getItemStack();

    String getName();

    void onLeftClick(Player player);

    void onRightClick(Player player);

    void onSwitch(Player player);

    void setGame(Game game);

    void setItemStack(ItemStack itemStack);

    boolean update();
}