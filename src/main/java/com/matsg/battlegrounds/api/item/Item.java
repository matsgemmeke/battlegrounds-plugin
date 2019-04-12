package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import org.bukkit.inventory.ItemStack;

public interface Item extends Cloneable, Comparable<Item> {

    Game getGame();

    void setGame(Game game);

    String getId();

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);

    String getName();

    GenericAttribute getAttribute(String id);

    Item clone();

    void onLeftClick(GamePlayer gamePlayer);

    void onRightClick(GamePlayer gamePlayer);

    void onSwap(GamePlayer gamePlayer);

    void onSwitch(GamePlayer gamePlayer);

    void remove();

    void setAttribute(String id, GenericAttribute attribute);

    boolean update();
}
