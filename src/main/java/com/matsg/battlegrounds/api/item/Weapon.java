package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.player.GamePlayer;

public interface Weapon extends Item {

    Weapon clone();

    String getDescription();

    GamePlayer getGamePlayer();

    ItemSlot getItemSlot();

    WeaponType getType();

    void resetState();

    void remove();

    void setGamePlayer(GamePlayer gamePlayer);

    void setItemSlot(ItemSlot itemSlot);
}