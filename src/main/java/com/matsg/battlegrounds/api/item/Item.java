package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.inventory.ItemStack;

public interface Item extends Cloneable, Comparable<Item> {

    Game getGame();

    Item clone();

    ItemStack getItemStack();

    String getName();

    void onDrop();

    void onLeftClick();

    void onPickUp(GamePlayer gamePlayer, org.bukkit.entity.Item itemEntity);

    void onRightClick();

    void onSwitch();

    void setGame(Game game);

    void setItemStack(ItemStack itemStack);

    boolean update();
}