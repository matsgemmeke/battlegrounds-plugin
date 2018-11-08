package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Item extends Cloneable, Comparable<Item> {

    ItemAttribute getAttribute(String id);

    Game getGame();

    Item clone();

    String getId();

    ItemStack getItemStack();

    String getName();

    boolean onDrop(GamePlayer gamePlayer);

    void onLeftClick(GamePlayer gamePlayer);

    void onRightClick(GamePlayer gamePlayer);

    void onSwitch(GamePlayer gamePlayer);

    void setGame(Game game);

    void setItemStack(ItemStack itemStack);

    boolean update();
}