package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import org.bukkit.inventory.ItemStack;

public interface Item extends Cloneable, Comparable<Item> {

    GenericAttribute getAttribute(String id);

    Game getGame();

    Item clone();

    String getId();

    ItemStack getItemStack();

    String getName();

    void onLeftClick(GamePlayer gamePlayer);

    void onRightClick(GamePlayer gamePlayer);

    void onSwap(GamePlayer gamePlayer);

    void onSwitch(GamePlayer gamePlayer);

    void remove();

    void setAttribute(String id, GenericAttribute attribute);

    void setGame(Game game);

    void setItemStack(ItemStack itemStack);

    boolean update();
}
