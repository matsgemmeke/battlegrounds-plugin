package com.matsg.battlegrounds.gui;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class FunctionalButton implements Button {

    private Consumer<Player> leftClick;
    private Consumer<Player> rightClick;

    public FunctionalButton(Consumer<Player> leftClick, Consumer<Player> rightClick) {
        this.leftClick = leftClick;
        this.rightClick = rightClick;
    }

    public void onLeftClick(Player player) {
        leftClick.accept(player);
    }

    public void onRightClick(Player player) {
        rightClick.accept(player);
    }
}
